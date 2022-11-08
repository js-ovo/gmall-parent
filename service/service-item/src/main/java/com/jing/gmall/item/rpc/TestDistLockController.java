package com.jing.gmall.item.rpc;

import com.jing.gmall.common.result.Result;
import com.jing.gmall.item.component.RedisDistLock;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/inner/rpc/item")
public class TestDistLockController {

    @Autowired
    private RedisDistLock redisDistLock;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Value("${server.port}")
    String port;

    final Logger log = LoggerFactory.getLogger(TestDistLockController.class);





    @GetMapping("/incr")
    // 分布式 redisson
    public Result lock(){

        // 获取锁
        RLock lock = redissonClient.getLock("redisson");

        // 加锁
        lock.lock();
        //锁住之后如果没释放锁,每1/3的看门狗时间重置过期时间,默认过期时间30000ms
        lock.lock(50, TimeUnit.SECONDS);
        // 锁住之后如果在 设置时间没有释放锁,redis中自动过期,不进行续费
        //阻塞式锁,如果其他线程没抢到锁就会一直等待

        boolean b = lock.tryLock();// 尝试获取锁,如果获取返回true 否则返回false  默认释放时间30s
        try {
            lock.tryLock(5, TimeUnit.SECONDS); // 尝试获取锁,如果获取返回true,如果没有获取则 在指定时间内一直获取,超过指定时间就离开 默认 30秒 自动续期
            lock.tryLock(5,10,TimeUnit.SECONDS); // 在指定时间内尝试获取锁,获取锁后,持续时间指定秒  不会续期
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            // 获取值
            String value = redisTemplate.opsForValue().get("hh");
            // 修改值
            String i = Integer.valueOf(value) + 1 + "";
            // 保存到redis
            redisTemplate.opsForValue().set("hh",i);
        } finally {
            // 解锁
            lock.unlock();
        }

        return Result.ok();
    }


    /***
     * CountDownLatch 锁
     * @return
     */
    @GetMapping("/count")
    public Result countDownLatch(){
        RCountDownLatch countDown = redissonClient.getCountDownLatch("countDown");

        // 设置 限制数 7
        countDown.trySetCount(7);

        try {
            // 等待收集够七颗
            countDown.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Result.ok(port + "召唤神龙");
    }

    @GetMapping("/down")
    public Result down(){
        RCountDownLatch countDown = redissonClient.getCountDownLatch("countDown");
        long count = countDown.getCount();
        log.info("count => {}",count);
        countDown.countDown();
        return Result.ok(port + "收集到第"+ count+ "颗龙珠!");
    }

    /**
     * 信号量
     * @return
     */
    @GetMapping("/semaphore")
    public Result semaphore(){
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        try {
            semaphore.acquire(); // 获取信号量 信号量-1  获取不到 就阻塞等待其他线程释放 之后再获取

            if (semaphore.tryAcquire()){ //尝试获取 获取不到就立即离开
                TimeUnit.SECONDS.sleep(5);
                semaphore.release();
                log.info("执行完成,释放");
            } else {
                log.info("抢锁失败,立即离开");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Result.ok(port);
    }

    @GetMapping("/init")
    public void init(){
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        semaphore.trySetPermits(10);
    }







    // 自定义分布式锁
    public Result mylock(){
        redisDistLock.lock();
        try {
            String value = redisTemplate.opsForValue().get("hh");
            String i = Integer.valueOf(value) + 1 + "";
            redisTemplate.opsForValue().set("hh",i);
        } finally {
            redisDistLock.unlock();
        }

        return Result.ok();
    }
}
