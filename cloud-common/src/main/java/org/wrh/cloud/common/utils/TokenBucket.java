package org.wrh.cloud.common.utils;

/**
 * @author wuruohong
 * @date 2022-06-19 0:00
 * 每秒产生指定令牌的方法之一，每次在获取的时候判断是否要先填充
 *
 * 可以像下面这样创建一个令牌桶（桶大小为 100，且每秒生成 100 个令牌）:
 * TokenBucket limiter = new TokenBucket(100, 100, 1000);
 *
 * 从上面的代码片段可以看出，令牌桶算法的实现非常简单也非常高效，仅仅通过几个变量的运算就实现了完整的限流功能。核心逻辑在于 refill() 这个方法，
 * 在每次消费令牌时，计算当前时间和上一次填充的时间差，并根据填充速度计算出应该填充多少令牌。在重新填充令牌后，再判断请求的令牌数是否足够，如果不够，返回 false，如果足够，则减去令牌数，并返回 true。 *
 * 在实际的应用中，往往不会直接使用这种原始的令牌桶算法，一般会在它的基础上作一些改进，比如，填充速率支持动态调整，令牌总数支持透支，基于 Redis 支持分布式限流等，不过总体来说还是符合令牌桶算法的整体框架
 */
public class TokenBucket {

    private final long capacity;
    private final double refillTokensPerOneMillis;
    private double availableTokens;
    private long lastRefillTimestamp;

    public TokenBucket(long capacity, long refillTokens, long refillPeriodMillis) {
        this.capacity = capacity;
        this.refillTokensPerOneMillis = (double) refillTokens / (double) refillPeriodMillis;
        this.availableTokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    synchronized public boolean tryConsume(int numberTokens) {
        refill();
        if (availableTokens < numberTokens) {
            return false;
        } else {
            availableTokens -= numberTokens;
            return true;
        }
       }


    private void refill() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > lastRefillTimestamp) {
            long millisSinceLastRefill = currentTimeMillis - lastRefillTimestamp;
            double refill = millisSinceLastRefill * refillTokensPerOneMillis;
            this.availableTokens = Math.min(capacity, availableTokens + refill);
            this.lastRefillTimestamp = currentTimeMillis;
        }
    }

}
