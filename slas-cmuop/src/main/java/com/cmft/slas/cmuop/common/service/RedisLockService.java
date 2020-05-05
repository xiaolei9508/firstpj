 package com.cmft.slas.cmuop.common.service;

 public interface RedisLockService {
    /**
     * 加锁
     *
     * @param key
     * @return
     */
    boolean lock(String key);

    /**
     * 解锁
     * 
     * @param key
     */
    void unlock(String key);
}
