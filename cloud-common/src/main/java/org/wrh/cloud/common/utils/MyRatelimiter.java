package org.wrh.cloud.common.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author wuruohong
 * @date 2022-06-18 23:37
 *https://www.cxyxiaowu.com/20518.html
 * https://zhuanlan.zhihu.com/p/435806498
 * Guava 提供了两个和限流相关的类：RateLimiter 和 SmoothRateLimiter。Guava 的 RateLimiter 基于令牌桶算法实现，不过在传统的令牌桶算法基础上做了点改进，支持两种不同的限流方式：平滑突发限流（SmoothBursty） 和 平滑预热限流（SmoothWarmingUp）。
 *
 * 下面的方法可以创建一个平滑突发限流器（SmoothBursty）：
 * RateLimiter limiter = RateLimiter.create(5);
 *
 * RateLimiter.create(5) 表示这个限流器容量为 5，并且每秒生成 5 个令牌，也就是每隔 200 毫秒生成一个。
 * 我们可以使用 limiter.acquire() 消费令牌，如果桶中令牌足够，返回 0，如果令牌不足，则阻塞等待，并返回等待的时间。我们连续请求几次：
 *
 * System.out.println(limiter.acquire());
 * System.out.println(limiter.acquire());
 * System.out.println(limiter.acquire());
 * System.out.println(limiter.acquire());
 *
 * 输出结果如下：
 * 0.0
 * 0.198239
 * 0.196083
 * 0.200609
 *
 * 可以看出限流器创建之后，初始会有一个令牌，然后每隔 200 毫秒生成一个令牌，所以第一次请求直接返回 0，后面的请求都会阻塞大约 200 毫秒。
 *
 * =====================================================================================================================
 * 另外，SmoothBursty 还具有应对突发的能力，而且 还允许消费未来的令牌，比如下面的例子：
 *
 * RateLimiter limiter = RateLimiter.create(5);
 * System.out.println(limiter.acquire(10));
 * System.out.println(limiter.acquire(1));
 * System.out.println(limiter.acquire(1));
 *
 * 会得到类似下面的输出：
 *
 * 0.0
 * 1.997428
 * 0.192273
 * 0.200616
 *
 * 限流器创建之后，初始令牌只有一个，但是我们请求 10 个令牌竟然也通过了，只不过看后面请求发现，第二次请求花了 2 秒左右的时间把前面的透支的令牌给补上了。
 *
 * Guava 支持的另一种限流方式是平滑预热限流器（SmoothWarmingUp），可以通过下面的方法创建：
 *
 * RateLimiter limiter = RateLimiter.create(2, 3, TimeUnit.SECONDS);
 * System.out.println(limiter.acquire(1));
 * System.out.println(limiter.acquire(1));
 * System.out.println(limiter.acquire(1));
 * System.out.println(limiter.acquire(1));
 * System.out.println(limiter.acquire(1));
 *
 * 第一个参数还是每秒创建的令牌数量，这里是每秒 2 个，也就是每 500 毫秒生成一个，后面的参数表示从冷启动速率过渡到平均速率的时间间隔，也就是所谓的热身时间间隔（warm up period）。我们看下输出结果：
 *
 * 0.0
 * 1.329289
 * 0.994375
 * 0.662888
 * 0.501287
 *
 * 第一个请求还是立即得到令牌，但是后面的请求和上面平滑突发限流就完全不一样了，按理来说 500 毫秒就会生成一个令牌，但是我们发现第二个请求却等了 1.3s，而不是 0.5s，
 * 后面第三个和第四个请求也等了一段时间。不过可以看出，等待时间在慢慢的接近 0.5s，直到第五个请求等待时间才开始变得正常。
 * 从第一个请求到第五个请求，这中间的时间间隔就是热身阶段，可以算出热身的时间就是我们设置的 3 秒。
 *
 * ===============================================================================================================================================================
 *
 * Bucket4j
 *
 * Bucket4j是一个基于令牌桶算法实现的强大的限流库，它不仅支持单机限流，还支持通过诸如 Hazelcast、Ignite、Coherence、Infinispan 或其他兼容 JCache API (JSR 107) 规范的分布式缓存实现分布式限流。
 *
 * 在使用 Bucket4j 之前，我们有必要先了解 Bucket4j 中的几个核心概念：
 *
 *     Bucket
 *     Bandwidth
 *     Refill
 *
 * Bucket 接口代表了令牌桶的具体实现，也是我们操作的入口。它提供了诸如 tryConsume 和 tryConsumeAndReturnRemaining 这样的方法供我们消费令牌。可以通过下面的构造方法来创建Bucket:
 *
 * Bucket bucket = Bucket4j.builder().addLimit(limit).build();
 * if(bucket.tryConsume(1)) {
 *     System.out.println("ok");
 * } else {
 *     System.out.println("error");
 * }
 *
 * Bandwidth 的意思是带宽， 可以理解为限流的规则。Bucket4j 提供了两种方法来创建 Bandwidth：simple 和 classic。下面是 simple 方式创建的 Bandwidth，表示桶大小为 10，填充速度为每分钟 10 个令牌:
 *
 * Bandwidth limit = Bandwidth.simple(10, Duration.ofMinutes(1));
 *
 * simple方式桶大小和填充速度是一样的，classic 方式更灵活一点，可以自定义填充速度，下面的例子表示桶大小为 10，填充速度为每分钟 5 个令牌:
 *
 * Refill filler = Refill.greedy(5, Duration.ofMinutes(1));
 * Bandwidth limit = Bandwidth.classic(10, filler);
 *
 * 其中，Refill 用于填充令牌桶，可以通过它定义填充速度，
 * Bucket4j 有两种填充令牌的策略：间隔策略（intervally） 和 贪婪策略（greedy）。在上面的例子中我们使用的是贪婪策略，如果使用间隔策略可以像下面这样创建 Refill：
 *
 * Refill filler = Refill.intervally(5, Duration.ofMinutes(1));
 *
 * 所谓间隔策略指的是每隔一段时间，一次性的填充所有令牌，比如上面的例子，会每隔一分钟，填充 5 个令牌，如下所示：
 * **
 * 而贪婪策略会尽可能贪婪的填充令牌，同样是上面的例子，会将一分钟划分成 5 个更小的时间单元，每隔 12 秒，填充 1 个令牌，如下所示：
 * *
 * * 在了解了 Bucket4j 中的几个核心概念之后，我们再来看看官网介绍的一些特性：
 * *     基于令牌桶算法高性能，无锁实现不存在精度问题，所有计算都是基于整型的支持通过符合 JCache API 规范的分布式缓存系统实现分布式限流
 * 支持为每个 Bucket 设置多个 Bandwidth支持同步和异步 API支持可插拔的监听 API，用于集成监控和日志不仅可以用于限流，还可以用于简单的调度
 *
 * Bucket4j 提供了丰富的文档，推荐在使用 Bucket4j 之前，先把官方文档中的 基本用法 和 高级特性 仔细阅读一遍。另外，关于 Bucket4j 的使用，
 * 推荐这篇文章 Rate limiting Spring MVC endpoints with bucket4j，这篇文章详细的讲解了如何在 Spring MVC 中使用拦截器和 Bucket4j
 * 打造业务无侵入的限流方案，另外还讲解了如何使用 Hazelcast 实现分布式限流；另外，Rate Limiting a Spring API Using Bucket4j
 * 这篇文章也是一份很好的入门教程，介绍了 Bucket4j 的基础知识，在文章的最后还提供了 Spring Boot Starter 的集成方式，结合 Spring Boot Actuator 很容易将限流指标集成到监控系统中。
 *
 * 和 Guava 的限流器相比，Bucket4j 的功能显然要更胜一筹，毕竟 Guava 的目的只是用作通用工具类，而不是用于限流的。使用 Bucket4j
 * 基本上可以满足我们的大多数要求，不仅支持单机限流和分布式限流，而且可以很好的集成监控，搭配 Prometheus 和 Grafana 简直完美。
 * 值得一提的是，有很多开源项目譬如 JHipster API Gateway 就是使用 Bucket4j 来实现限流的。
 *
 * Bucket4j 唯一不足的地方是它只支持请求频率限流，不支持并发量限流，另外还有一点，虽然 Bucket4j 支持分布式限流，
 * 但它是基于 Hazelcast 这样的分布式缓存系统实现的，不能使用 Redis，这在很多使用 Redis 作缓存的项目中就很不爽，所以我们还需要在开源的世界里继续探索。
 *
 * ====================================================================================================================================================
 * Resilience4j
 *
 * Resilience4j 是一款轻量级、易使用的高可用框架。用过 Spring Cloud 早期版本的同学肯定都听过 Netflix Hystrix，Resilience4j 的设计灵感就来自于它。
 * 自从 Hystrix 停止维护之后，官方也推荐大家使用 Resilience4j 来代替 Hystrix。
 * * *
 * Resilience4j 的底层采用 Vavr，这是一个非常轻量级的 Java 函数式库，使得 Resilience4j 非常适合函数式编程。
 * Resilience4j 以装饰器模式提供对函数式接口或 lambda 表达式的封装，提供了一波高可用机制：重试（Retry）、熔断（Circuit Breaker）、
 * 限流（Rate Limiter）、限时（Timer Limiter）、隔离（Bulkhead）、缓存（Caceh） 和 降级（Fallback）。
 * 我们重点关注这里的两个功能：限流（Rate Limiter） 和 隔离（Bulkhead），
 *
 * Rate Limiter 是请求频率限流，Bulkhead 是并发量限流。 *
 * Resilience4j 提供了两种限流的实现：SemaphoreBasedRateLimiter 和 AtomicRateLimiter。
 * SemaphoreBasedRateLimiter 基于信号量实现，用户的每次请求都会申请一个信号量，并记录申请的时间，申请通过则允许请求，申请失败则限流，
 * 另外有一个内部线程会定期扫描过期的信号量并释放，很显然这是令牌桶的算法。
 *
 * AtomicRateLimiter 和上面的经典实现类似，不需要额外的线程，在处理每次请求时，根据距离上次请求的时间和生成令牌的速度自动填充。关于这二者的区别可以参考文章 Rate Limiter Internals in Resilience4j。
 * Resilience4j 也提供了两种隔离的实现：SemaphoreBulkhead 和 ThreadPoolBulkhead，通过信号量或线程池控制请求的并发数，具体的用法参考官方文档，这里不再赘述。
 *
 * 下面是一个同时使用限流和隔离的例子：
 *
 * // 创建一个 Bulkhead，最大并发量为 150
 *  BulkheadConfig bulkheadConfig = BulkheadConfig.custom()
 *      .maxConcurrentCalls(150)
 *      .maxWaitTime(100)
 *      .build();
 *  Bulkhead bulkhead = Bulkhead.of("backendName", bulkheadConfig);
 *
 *  // 创建一个 RateLimiter，每秒允许一次请求
 *  RateLimiterConfig rateLimiterConfig = RateLimiterConfig.custom()
 *     .timeoutDuration(Duration.ofMillis(100))
 *     .limitRefreshPeriod(Duration.ofSeconds(1))
 *     .limitForPeriod(1)
 *     .build();
 * RateLimiter rateLimiter = RateLimiter.of("backendName", rateLimiterConfig);
 *
 * // 使用 Bulkhead 和 RateLimiter 装饰业务逻辑
 * Supplier<String> supplier = () -> backendService.doSomething();
 * Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
 *   .withBulkhead(bulkhead)
 *   .withRateLimiter(rateLimiter)
 *   .decorate();
 *
 * // 调用业务逻辑
 * Try<String> try = Try.ofSupplier(decoratedSupplier);
 * assertThat(try.isSuccess()).isTrue();
 *
 * Resilience4j 在功能特性上比 Bucket4j 强大不少，而且还支持并发量限流。不过最大的遗憾是，Resilience4j 不支持分布式限流
 *
 * ==============================================================================================================================================
 *
 * 可以看出，限流技术在实际项目中应用非常广泛，大家对实现自己的限流算法乐此不疲，新算法和新实现层出不穷。但是找来找去，目前还没有找到一款开源项目完全满足我的需求。 *
 * 我的需求其实很简单，需要同时满足两种不同的限流场景：请求频率限流和并发量限流，并且能同时满足两种不同的限流架构：单机限流和分布式限流。
 * 下面我们就开始在 Spring Cloud Gateway 中实现这几种限流，通过前面介绍的那些项目，我们取长补短，基本上都能用比较成熟的技术实现，
 * 只不过对于最后一种情况，分布式并发量限流，网上没有搜到现成的解决方案，在和同事讨论了几个晚上之后，想出一种新型的基于双窗口滑动的限流算法，
 * 我在这里抛砖引玉，欢迎大家批评指正，如果大家有更好的方法，也欢迎讨论
 *
 * 在文章一开始介绍 Spring Cloud Gateway 的特性时，我们注意到其中有一条 Request Rate Limiting，说明网关自带了限流的功能，
 * 但是 Spring Cloud Gateway 自带的限流有很多限制，譬如不支持单机限流，不支持并发量限流，而且它的请求频率限流也是不尽人意，这些都需要我们自己动手来解决。
 *
 *
 *
 */
public class MyRatelimiter {

    private final RateLimiter limiter = RateLimiter.create(2);
    
    @Test
    public void Test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            if (!limiter.tryAcquire()) {
                System.out.println(LocalDateTime.now().toString() + " CAN NOT GET LOCK");
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.println(LocalDateTime.now().toString() + " GET LOCK");
            }
        }
    }

    @Test
    public void Test1() throws InterruptedException {

        TimeUnit.SECONDS.sleep(5);
        for (int i = 0; i < 10; i++) {
            if (!limiter.tryAcquire()) {
                System.out.println(LocalDateTime.now().toString() + " CAN NOT GET LOCK");
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.println(LocalDateTime.now().toString() + " GET LOCK");
            }
        }
    }
}
