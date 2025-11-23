package com.spike.reactor;

import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <pre> 核心类:
 * @see Mono
 * @see Flux
 *
 * @see reactor.core.Disposable
 * @see Disposables#swap()
 * @see reactor.core.Disposables#composite(Disposable...)
 * </pre>
 *
 * <pre> 测试工具:
 * @see reactor.test.StepVerifier
 * @see reactor.test.publisher.TestPublisher
 * @see reactor.test.subscriber.TestSubscriber
 * </pre>
 *
 * <pre> Reactor内容:
 * 创建 {@link ReactorCreationTest}
 * 订阅 {@link ReactorSubscribeTest}
 * 转换操作符 {@link ReactorTransformationTest}
 * 处理错误 {@link ReactorErrorsTest}
 * 背压 {@link ReactorBackpressureTest}
 * 冷/热流 {@link ReactorHotAndColdTest}
 * 时间 {@link ReactorTimingTest}
 * Sinks {@link ReactorSinksTest}
 * 调度器 {@link ReactorSchedulerTest}
 * 上下文 {@link ReactorContextTest}
 * </pre>
 */
public class ReactorTest {

    //
    // operators:
    // transform existing sequence
    // peek at the sequence's processing
    // split and join Flux sequences
    // work with time
    // return data synchronously
    //

    //
    // internals:
    // macro-fusion: assemble time, aim to replace one operator with another
    // micro-fusion: runtime, aim to reuse of shared resources
    //  reactor.core.Fuseable
    //

}
