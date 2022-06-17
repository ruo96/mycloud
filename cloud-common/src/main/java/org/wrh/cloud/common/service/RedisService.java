package org.wrh.cloud.common.service;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式缓存处理service
 * @Description:
 * @Modified By:
 */
public interface RedisService {
    void addValue(String key, String value);
    /**
     * 带过期时间的
     *
     * @param key
     * @param value
     * @param expired
     * @param timeUnit
     */
    void addValue(String key, String value, long expired, TimeUnit timeUnit);

    public String getValue(String key);

    public <T> T getValue(String key, Class<T> cls);

    /**
     * 添加数据到list的【顶部】
     * @param listName list的名称
     * @param value 存放的数据
     * @param expires 有效时间
     * @param tu 时间单位
     */
    void leftPush(String listName, String value, int expires, TimeUnit tu);

    /**
     * 从list列表中删除指定数据
     * @param listName --list的名称
     * @param count --要删除元素的个数 【负数表示从尾部向头部删除；正数则相反】
     * @param value 要删除元素的值
     *
     * @return 被移除元素的个数
     */
    Long remove(String listName, long count, String value);

    /**
     * 判断是否在列表中
     * @param listName 列表名称
     * @param value 元素值
     * @return Boolean
     */
    Boolean contains(String listName, String value);

    public String rightPop(String listName);

    /**
     * 该操作是原子性的
     *
     * @param key
     * @param value
     * @return
     */
    Long increaseBy(String key, long value);

    Long increaseBy(String key, long value, int expires, TimeUnit tu);

    void delete(String key);

    boolean hasKey(String key);

    String getString(String key);

    void setString(String key, String value, long expire, TimeUnit timeUnit);

    void setString(String key, String value);

    boolean hasField(String key, String field);

    void hset(String key, String field, String value);

    String hget(String key, String field);

    boolean expireKey(String key, long time, TimeUnit timeUnit);

    long getExpire(String key);

    boolean setnx(String bizkey, String value, final int time);
}
