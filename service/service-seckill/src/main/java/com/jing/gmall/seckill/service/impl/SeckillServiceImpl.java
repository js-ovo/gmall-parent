package com.jing.gmall.seckill.service.impl;

import com.jing.gmall.common.config.mq.service.MqService;
import com.jing.gmall.common.constant.MqConst;
import com.jing.gmall.common.constant.RedisConst;
import com.jing.gmall.common.execption.GmallException;
import com.jing.gmall.common.result.ResultCodeEnum;
import com.jing.gmall.common.util.DateUtil;
import com.jing.gmall.common.util.Jsons;
import com.jing.gmall.common.util.MD5;
import com.jing.gmall.common.utils.HttpRequestUtils;
import com.jing.gmall.msg.SeckillQueueMsg;
import com.jing.gmall.order.entity.OrderInfo;
import com.jing.gmall.seckill.entity.SeckillGoods;
import com.jing.gmall.seckill.service.SeckillGoodsService;
import com.jing.gmall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用来处理秒杀业务员
 */
@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillGoodsService seckillGoodsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MqService mqService;

    private final Map<Long,SeckillGoods> localCache = new ConcurrentHashMap<>();
    /**
     * 上架当天要参与秒杀的商品
     * @param date
     */
    @Override
    public void upSeckillGoos(String date) {
        // 从数据库查出 当天要参与秒杀的品
        List<SeckillGoods> goods = seckillGoodsService.getSeckillGoods(date);

        // 查看 本地缓存中是否有之前的数据 如果有将之前的缓存删除
        if (! localCache.isEmpty()) {
            localCache.clear();
        }
        // 放入二级缓存 Redis中
        goods.forEach(sku -> {
            stringRedisTemplate.opsForHash().put(RedisConst.SECKILL_GOODS_KEY + date,sku.getSkuId().toString(), Jsons.toJsonStr(sku));
            // 本地缓存
            localCache.put(sku.getSkuId(),sku);
        });
        // 设置过期时间
        stringRedisTemplate.expire(RedisConst.SECKILL_GOODS_KEY + date,2, TimeUnit.DAYS);
        log.info("上架当天参与秒杀的商品完成,时间:{}",date);
    }

    @Override
    public List<SeckillGoods> getSeckillGoods(String data) {
        // 先从本地缓存中拿
        List<SeckillGoods> seckillGoods = new ArrayList<>(localCache.values());
        if (seckillGoods.isEmpty()){
            // 本地缓存为空 查询二级缓存
            List<Object> values = stringRedisTemplate.opsForHash().values(RedisConst.SECKILL_GOODS_KEY + data);
            if (! values.isEmpty()){
                log.info("二级缓存命中");
                seckillGoods = values.stream().map(item -> Jsons.json2Obj(item.toString(),SeckillGoods.class))
                        .sorted(Comparator.comparing(SeckillGoods::getStartTime))
                        .collect(Collectors.toList());
                // 同步给一级缓存
                seckillGoods.forEach(item -> localCache.put(item.getSkuId(),item));
            }
        }
        return seckillGoods;
    }


    /**
     * 获取秒杀商品的详情信息
     * @param skuId
     * @return
     */
    @Override
    public SeckillGoods getSeckillDetail(Long skuId) {
        // 去本地缓存查找
        SeckillGoods seckillGoods = localCache.get(skuId);
        if (seckillGoods != null){
            log.info("本地一级缓存命中");
            return seckillGoods;
        }
        log.info("正在同步二级缓存");
        String date = DateUtil.formatDate(new Date(),"yyyy-MM-dd");
        // 同步二级缓存
        List<SeckillGoods> goodsList = getSeckillGoods(date);
        return localCache.get(skuId);
    }

    @Override
    public String generateSeckillCode(Long skuId) {


        // 判断用户是否登录
        Long userId = HttpRequestUtils.getUserId();
        if (userId == null){
            throw new GmallException(ResultCodeEnum.LOGIN_AUTH);
        }

        // 判断是否在秒杀时间内
        SeckillGoods detail = getSeckillDetail(skuId);
        Date currentDate = new Date();
        Date startTime = detail.getStartTime();
        Date endTime = detail.getEndTime();
        if (currentDate.before(startTime)){
            throw new GmallException(ResultCodeEnum.SECKILL_NO_START);
        }

        if (currentDate.after(endTime)){
            throw new GmallException(ResultCodeEnum.SECKILL_END);
        }

        // 验证库存
        if (detail.getStockCount() <= 0) {
            throw new GmallException(ResultCodeEnum.SECKILL_FINISH);
        }

        // 生成 秒杀码

        // TODO 每个商品 49 同一个用户，同一天，只能买一个。
        String date = DateUtil.formatDate(currentDate, "yyyy-MM-dd");

        String code = MD5.encrypt(skuId + "_" + userId + "_" + date);

        // 保存到Redis中 前端秒杀时进行验证  如果不存在就设置
        stringRedisTemplate.opsForValue().setIfAbsent(RedisConst.SECKILL_CODE_KEY + code,"0",1,TimeUnit.DAYS);

        return code;
    }

    @Override
    public void saveSeckillOrder(Long skuId, String code) {
        // 校验参数
        // 用户是否登录
        Long userId = HttpRequestUtils.getUserId();
        if (userId == null) {
            throw new GmallException(ResultCodeEnum.LOGIN_AUTH);
        }
        // 是否在秒杀时间
        SeckillGoods seckillDetail = getSeckillDetail(skuId);
        Date currentDate = new Date();
        if (seckillDetail.getStartTime().after(currentDate)){
            throw new GmallException(ResultCodeEnum.SECKILL_NO_START);
        }
        if (seckillDetail.getEndTime().before(currentDate)){
            throw new GmallException(ResultCodeEnum.SECKILL_END);
        }
        // 验证 秒杀码
        String date = DateUtil.formatDate(currentDate,"yyyy-MM-dd");
        String seckillCode = MD5.encrypt(skuId + "_" + userId + "_" + date);
        if (! seckillCode.equals(code)){
            // 秒杀码错误
            throw new GmallException(ResultCodeEnum.SECKILL_ILLEGAL);
        }
        // 验证 redis中是否有该秒杀码,保证 是服务器生成的合法验证码
        Boolean hasKey = stringRedisTemplate.hasKey(RedisConst.SECKILL_CODE_KEY + seckillCode);
        if (!hasKey){
            // 不是服务器生成的秒杀码。 非法请求
            throw new GmallException(ResultCodeEnum.SECKILL_ILLEGAL);
        }


        //开始扣内存
        Long increment = stringRedisTemplate.opsForValue().increment(RedisConst.SECKILL_CODE_KEY + seckillCode);
        if (increment > 1){
            // 说明之前已经扣过一次了  每个用户 每个商品只能购买一次     第一次 生成 秒杀码 为0   抢到后是1
            // 不做任何处理
        } else {
            // 向队列发送消息准备扣减库存
            // 在发送消息之前扣减
            seckillDetail.setStockCount(seckillDetail.getStockCount() - 1);
            // 如果有库存  发送消息
            if (seckillDetail.getStockCount() < 0){
                throw new GmallException(ResultCodeEnum.SECKILL_FINISH);
            }
            // 说明 之前所有验证都正确  开始排队扣减库存
            SeckillQueueMsg seckillQueueMsg = new SeckillQueueMsg();
            seckillQueueMsg.setSkuId(skuId);
            seckillQueueMsg.setUserId(userId);
            seckillQueueMsg.setSeckillCode(code);
            mqService.convertAndSend(MqConst.SECKILL_EVENT_EXCHANGE,MqConst.SECKILL_QUEUE_RK,seckillQueueMsg);
        }



    }

    @Override
    public ResultCodeEnum checkOrderStatus(Long userId, Long skuId) {
        String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
        //生成秒杀码
        String code = MD5.encrypt(skuId + "_" + userId + "_" + date);
        // 去redis中查看是否存在秒杀订单
        String json = stringRedisTemplate.opsForValue().get(RedisConst.SECKILL_ORDER_KEY + code);
        log.info("json");
        if (! StringUtils.isEmpty(json)){
            if ("false".equals(json)){
                // 扣减库存失败 没有库存了
                throw new GmallException(ResultCodeEnum.SECKILL_FINISH);
            }
            OrderInfo orderInfo = Jsons.json2Obj(json, OrderInfo.class);
            // 判断是否有收货地址  有的话就是已经下过订单了   没有就是秒杀成功没有下单
            if (StringUtils.isEmpty(orderInfo.getConsignee())){
                return ResultCodeEnum.SECKILL_SUCCESS;  // 抢单
            }
            return ResultCodeEnum.SECKILL_ORDER_SUCCESS; // 下单
        }
        String str = stringRedisTemplate.opsForValue().get(RedisConst.SECKILL_CODE_KEY + code);
        if (StringUtils.isEmpty(str)){  // 没有对应的秒杀码
            return ResultCodeEnum.SECKILL_ILLEGAL;
        }
        if (Long.parseLong(str) >= 1){
            return ResultCodeEnum.SECKILL_RUN; // 排队中
        }
        return ResultCodeEnum.SERVICE_ERROR;
    }
}
