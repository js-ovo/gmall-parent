package com.jing.gmall.item.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 手动实现分布式锁
 */
@Component
@Slf4j
public class RedisDistLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 加锁代码
     *
     * 问题1: 系统正在运行中,当一个线程抢到锁之后,突然系统断电,这个锁一直存在Redis中,导致其他线程一直处于等待状态系统卡住
     * 解决: 设置 锁的过期时间
     *
     * 问题2: 当一个线程抢到锁后,对锁设置过期时间.这是两步操作 加锁 = 得到锁 + 设置过期时间, 操作不是连续的,有可能在两步之间,
     *       系统出现故障,那么依旧不会设置成功.
     * 解决: 将 得到锁 和 设置过期时间一步执行   redis支持   如果业务时间较长,锁自动过期,使用自动续期或者根据业务时间压测来设置过期时间
     */
    public void lock(){
        //stringRedisTemplate.opsForValue().setIfAbsent("lock", "haha")
        while (!stringRedisTemplate.opsForValue().setIfAbsent("lock",Thread.currentThread().getId() + "", Duration.ofSeconds(30))){
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 设置锁30秒过期
        //stringRedisTemplate.expire("lock", Duration.ofSeconds(30));
    }


    /**
     * 解锁代码
     * 问题1: 假设锁的过期时间位10s,当一个线程执行完业务要进行解锁任务时时9.9s,这是发送解锁命令,删除redis中对应的key,但是数据在传输
     *        过程中会有延迟,当命令到达redis后,此时redis中的这条线程设置key已经过期消失,但是其他线程在消失的瞬间又重新设置了key,而这时
     *        就会移除原本属于其他线程的key,而别的线程还正在执行业务,这是不可取的
     * 解决:  加锁时,给锁一个标志,在解锁时,判断是否是当前线程加锁,如果是就删除,不是的话就不进行操作
     *
     *
     *
     *
     * 问题2: 在判断到是自己的锁时,然后向redis中发送删除命令,但是在这期间自己的锁刚好又过期了,这时依旧删除的是别人的锁
     * 解决: 判断是自己的锁和删除锁是原子命令,一起执行  redis不支持该命令,使用lua脚本
     *
     */
    public void unlock(){
        // 问题1 解决 问题2 未解决

        /*String s = stringRedisTemplate.opsForValue().get("lock");
        if ((Thread.currentThread().getId() + "").equals(s)){
            stringRedisTemplate.delete("lock");
            log.info("自己的锁,删除成功 => {}",Thread.currentThread().getId());
        } else {
            log.info("不是自己的锁,删除失败 => {}",Thread.currentThread().getId());
        }*/
        String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                "then\n" +
                "    return redis.call(\"del\",KEYS[1])\n" +
                "else\n" +
                "    return 0\n" +
                "end";
        Long lock = stringRedisTemplate.execute(RedisScript.of(script, Long.class)
                , Arrays.asList("lock"), Thread.currentThread().getId() + "");
        if (lock > 0){
            log.info("解锁=> {}",Thread.currentThread().getId());
        }
    }
}
