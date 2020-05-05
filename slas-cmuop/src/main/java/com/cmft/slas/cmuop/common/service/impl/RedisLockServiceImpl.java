package com.cmft.slas.cmuop.common.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmft.slas.cmuop.common.service.RedisLockService;

/**
 * redis分布式锁服务
 * 
 * @author xiaojp001
 * @date 2020/01/19
 */
@Service("redisLockService")
public class RedisLockServiceImpl implements RedisLockService {
    /*
     * 锁过期时间
     */
    private static Long LOCK_EXPIRE = 2000L;
    private static String LOCK_PREFIX = "prefix_";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean lock(String key) {
        String lock = LOCK_PREFIX + key;

        return (Boolean)redisTemplate.execute((RedisCallback<?>)connection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());

            if (acquire) {
                return true;
            } else {
                byte[] value = connection.get(lock.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long expireTime = Long.parseLong(new String(value));

                    // 如果锁已经过期
                    if (expireTime < System.currentTimeMillis()) {
                        // 重新加锁，防止死锁
                        byte[] oldValue = connection.getSet(lock.getBytes(),
                            String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    @Override
    public void unlock(String key) {
        String lock = LOCK_PREFIX + key;
        redisTemplate.delete(lock);
    }

}
