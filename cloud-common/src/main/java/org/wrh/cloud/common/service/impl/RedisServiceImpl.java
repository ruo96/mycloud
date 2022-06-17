package org.wrh.cloud.common.service.impl;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.wrh.cloud.common.service.RedisService;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式缓存处理service
 * @Description:
 * @Modified By:
 */
@Service
// @PromethuesTiming(type = TimingType.redis)
public class RedisServiceImpl implements RedisService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addValue(String key, String value) {
        try{
            stringRedisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            LOGGER.error("[redis]>>> addValue error:{}", e);
        }
        return;
    }

    /**
     * 带过期时间的
     *
     * @param key
     * @param value
     * @param expired
     * @param timeUnit
     */
    @Override
    public void addValue(String key, String value, long expired, TimeUnit timeUnit) {
        try{
            stringRedisTemplate.opsForValue().set(key, value, expired, timeUnit);;
        }catch (Exception e){
            LOGGER.error("[redis]>>> addValue error:{}", e);
        }
        return;
    }

    @Override
    public String getValue(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try{
            return stringRedisTemplate.opsForValue().get(key);
        }catch (Exception e){
            LOGGER.error("[redis]>>> getValue error:{}", e);
            return null;
        }
    }

    @Override
    public <T> T getValue(String key, Class<T> cls) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try{
            return JSON.parseObject(stringRedisTemplate.opsForValue().get(key), cls);
        }catch (Exception e){
            LOGGER.error("[redis]>>> getValue error:{}", e);
            return null;
        }
    }

    /**
     * 添加数据到list的【顶部】
     * @param listName list的名称
     * @param value 存放的数据
     * @param expires 有效时间
     * @param tu 时间单位
     */
    @Override
    public void leftPush(String listName, String value, int expires, TimeUnit tu) {
        if (StringUtils.isAnyBlank(listName, value)) {
            return;
        }

        try{
            stringRedisTemplate.opsForList().leftPush(listName, value);
            stringRedisTemplate.expire(listName, expires, tu);
        }catch (Exception e){
            LOGGER.error("[redis]>>> leftPush error:{}", e);
        }
        return;
    }

    /**
     * 从list列表中删除指定数据
     * @param listName --list的名称
     * @param count --要删除元素的个数 【负数表示从尾部向头部删除；正数则相反】
     * @param value 要删除元素的值
     *
     * @return 被移除元素的个数
     */
    @Override
    public Long remove(String listName, long count, String value) {
        try{
            return stringRedisTemplate.opsForList().remove(listName,count, value);
        }catch (Exception e){
            LOGGER.error("[redis]>>> remove error:{}", e);
        }

        return 0L;
    }

    /**
     * 判断是否在列表中
     * @param listName 列表名称
     * @param value 元素值
     * @return Boolean
     */
    @Override
    public Boolean contains(String listName, String value) {

        try{
            List<String> list = stringRedisTemplate.opsForList().range(listName,0, -1);
            return list.contains(value);
        }catch (Exception e){
            LOGGER.error("[redis]>>> range error:{}", e);
        }

        return Boolean.FALSE;
    }

    @Override
    public String rightPop(String listName) {
        if (StringUtils.isBlank(listName)) {
            return null;
        }

        try{
            return stringRedisTemplate.opsForList().rightPop(listName);
        }catch (Exception e){
            LOGGER.error("[redis]>>> leftPush error:{}", e);
            return null;
        }
    }

    /**
     * 该操作是原子性的
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long increaseBy(String key, long value) {
        try{
            Long increment = stringRedisTemplate.opsForValue().increment(key, value);
            return increment;
        }catch (Exception e){
            LOGGER.error("[redis]>>> increaseBy error:{}", e);
            return null;
        }
    }

    @Override
    public Long increaseBy(String key, long value, int expires, TimeUnit tu) {
        try{
            Long increment = stringRedisTemplate.opsForValue().increment(key, value);
            stringRedisTemplate.expire(key, expires, tu);
            return increment;
        }catch (Exception e){
            LOGGER.error("[redis]>>> increaseBy error:{}", e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try{
            stringRedisTemplate.delete(key);
        }catch (Exception e){
            LOGGER.error("[redis]>>> delete error:{}", e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        try{
            return stringRedisTemplate.hasKey(key);
        }catch (Exception e){
            LOGGER.error("[redis]>>> hasKey error:{}", e);
            return false;
        }
    }

    @Override
    public String getString(String key) {
        try{
            return stringRedisTemplate.opsForValue().get(key);
        }catch (Exception e){
            LOGGER.error("[redis]>>> getString error:{}", e);
            return null;
        }
    }

    @Override
    public void setString(String key, String value, long expire, TimeUnit timeUnit) {
        try{
            stringRedisTemplate.opsForValue().set(key, value, expire, timeUnit);
        }catch (Exception e){
            LOGGER.error("[redis]>>> setString error:{}", e);
            return;
        }
    }

    @Override
    public void setString(String key, String value) {
        try{
            stringRedisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            LOGGER.error("[redis]>>> setString without expire error:{}", e);
            return;
        }
    }

    @Override
    public boolean hasField(String key, String field) {
        try{
            return stringRedisTemplate.opsForHash().hasKey(key,field);
        }catch (Exception e){
            LOGGER.error("[redis]>>> hasField error:{}", e);
            return false;
        }
    }

    @Override
    public void hset(String key, String field, String value){
        try{
            stringRedisTemplate.opsForHash().put(key, field, value);
        }catch (Exception e){
            LOGGER.error("[redis]>>> hset error:{}", e);
        }
        return;
    }

    @Override
    public String hget(String key, String field){
        try{
            return (String) stringRedisTemplate.opsForHash().get(key, field);
        }catch (Exception e){
            LOGGER.error("[redis]>>> hget error:{}", e);
            return null;
        }
    }

    @Override
    public boolean expireKey(String key, long time, TimeUnit timeUnit) {
        try{
            return stringRedisTemplate.expire(key, time, timeUnit);
        }catch (Exception e){
            LOGGER.error("[redis]>>> expireKey error:{}", e);
            return false;
        }
    }

    @Override
    public long getExpire(String key) {
        try {
            return stringRedisTemplate.getExpire(key);
        }catch (Exception e){

        }

        return -1;
    }

    @Override
    public boolean setnx(String bizkey, String value, final int time) {
        try{
            return stringRedisTemplate.opsForValue().setIfAbsent(bizkey, value, time, TimeUnit.SECONDS);
        }catch (Exception e){
            LOGGER.error("[redis]>>> expireKey error:{}", e);
        }

        return false;
    }

}
