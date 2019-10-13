package com.kochun.wxmp.back.shiro.cache;

import com.kochun.wxmp.back.shiro.JwtUtil;
import com.kochun.wxmp.common.Constant;
import com.kochun.wxmp.common.utils.PropertiesUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 重写Shiro的Cache保存读取
 * 转载请注明出处，更多技术文章欢迎大家访问我的个人博客站点：https://www.doufuplus.com
 *
 * @author 丶doufu
 * @date 2019/08/03
 */
public class CustomCache<K, V> implements Cache<K, V> {


    private RedisTemplate<String, Object> redisTemplate;

    public CustomCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存的key名称获取为shiro:cache:account
     * @param key
     * @return java.lang.String
     * @author dolyw.com
     * @date 2018/9/4 18:33
     */
    private String getKey(Object key) {
        return Constant.PREFIX_SHIRO_CACHE + JwtUtil.getClaim(key.toString(), Constant.ACCOUNT);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if(!redisTemplate.hasKey(this.getKey(key))){
            return null;
        }
        return redisTemplate.opsForValue().get(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 读取配置文件，获取Redis的Shiro缓存过期时间
        PropertiesUtil.readProperties("config.properties");
        String shiroCacheExpireTime = PropertiesUtil.getProperty("shiroCacheExpireTime");
        // 设置Redis的Shiro缓存
        try {
            redisTemplate.opsForValue().set(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime), TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if(!redisTemplate.hasKey(this.getKey(key))){
            return null;
        }
        redisTemplate.delete(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        //Objects.requireNonNull(JedisUtil.getJedis()).flushDB();
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        //Long size = Objects.requireNonNull(JedisUtil.getJedis()).dbSize();
        Set<String> keys = redisTemplate.keys("*");
        return keys.size();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
        Set<String> keys = redisTemplate.keys("*");
        return keys;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            values.add(redisTemplate.opsForValue().get(this.getKey(key)));
        }
        return values;
    }
}
