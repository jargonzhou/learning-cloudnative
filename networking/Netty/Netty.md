# Netty
* https://netty.io/ Netty project * an event*driven asynchronous network application framework

![Netty高层次的组件图](https://netty.io/images/components.png)

> Netty is an NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.
>
> 'Quick and easy' doesn't mean that a resulting application will suffer from a maintainability or a performance issue. Netty has been designed carefully with the experiences earned from the implementation of a lot of protocols such as FTP, SMTP, HTTP, and various binary and text*based legacy protocols. As a result, Netty has succeeded to find a way to achieve ease of development, performance, stability, and flexibility without a compromise.

books:
* Netty in Action
* Netty权威指南

<!--
More:
- [万字长文带你深入理解netty，史上最强详解！](https://zhuanlan.zhihu.com/p/389034303?utm_id=0)
- [9.2 I/O 多路复用：select/poll/epoll](https://www.xiaolincoding.com/os/8_network_system/selete_poll_epoll.html)
-->

UML:
* [netty.uxf](./netty.uxf)
* [netty-codec-mqtt.uxf](./netty-codec-mqtt.uxf)

## Components

### Channel, EventLoop, ChannelFuture

Channel: abstract Sockets
- EmbeddedChannel
- LocalServerChannel
- NioDatagramChannel
- NioSctpChannel
- NioSocketChannel

EventLoop: abstract control flow, multithreading, concurrency
- an EventLoopGroup container 1+ EventLoops.
- **an EventLoop is bound to a single Thread for its lifetime**.
- all IO events processed by an EventLoop are handled on its dedicated Thread. - IO thread
- **a Channel is registered for its lifetime with a single EventLoop**.
- a single EventLoop may be assigned to 1+ Channels.

ChannelFuture: abstract asynchronous notification
- ChannelFutureListener: ChannelFuture#addListener()

### ChannelHandler, ChannelPipeline

ChannelHandler: container for application logic, handle inbound and outbound data. its methods are triggered by network events.
- ChannelInboundHandler
- ChannelOutboundHandler
- SimpleChannelInboundHandler: receive a decoded message and apply business logic to the data. `channelRead0(ChannelHandlerContext, T)`
- adapters: 
  - ChannelHandlerAdapter
  - ChannelInboundHandlerAdapter
  - ChannelOutboundHandlerAdapter
  - ChannelDuplexHandlerAdapter
  - encoders, decoders


ChannelPipeline: a container for a chain of ChannelHandlers, propagate the flow of inbound and outbound events along the chain.
- when a Channel is created, it is assigned its own ChannelPipeline.
- ChannelIntitializer, ServerBootstrap
- ChannelIntitializer#initChannel()

encoder, decoder
- encode the outbound message to bytes
- decode the inbound message from bytes
- MessageToByteEncoder, ByteToMessageDecoder
- ProtobufEncoder, ProtobufDecoder

### Bootstrapping

container for the configuration of an application's network layer:
- server: bind a process to a given port
  - ServerBootstrap, 2 EventLoopGroups
  - ServerChannel, Channel
- client: connect one process to another one running on a specific host at specific port
  - Bootstrap: 1 EventLoopGroup

## Transports

- OIO: io.netty.channel.socket.oio
  - `java.net`
  - OioServerSocketChannel
  - NioEventLoopGroup
- NIO: io.netty.channel.socket.nio
  - selector-base API since JDK 1.4
  - NioServerSocketChannel
- Epoll: io.netty.channel.epoll
  - Linux 2.5.44
  - Linux JDK NIP API
  - EpollServerSocketChannel
- Local: io.netty.channel.local
  - clients and server with same JVM
- Embedded: io.netty.channel.embedded
  - embed ChannelHandlers as helper classes inside other ChannelHandlers
  - unit test
  - EmbeddedChannel

io.netty.channel.Channel
- hold: ChannelPipeline, ChannelConfig
- implementations are thread-safe
- ServerChannel: tag interface
- SocketChannel
- DatagramChannel
- AbstractChannel: skeletal implementation
- methods
  - `isActive()`: meaning depend on transport. for socket, active once connected to remote peer; for datagram, active once opened.

io.netty.channel.Channel.Unsafe
- should NOT called from user-code
- implement the actual transport

## ByteBuf

ByteBuf: alternative Java NIO ByteBuffer
- readerIndex, writerIndex
  - `read`, `write`: advance the index.
  - `get`, `read`: not advance the index.

```
+-------------------+------------------+------------------+
| discardable bytes |  readable bytes  |  writable bytes  |
|                   |     (CONTENT)    |                  |
+-------------------+------------------+------------------+
|                   |                  |                  |
0      <=      readerIndex   <=   writerIndex    <=    capacity
```

- patterns
  - heap buffer: on JVM heap space, `hasArray()`
  - direct buffer: JDK 1.4 NIO, allocate memory via native calls, `!hasArray()`
  - composite buffer: CompositeByteBuf

byte level operations:
- random access indexing: `capacity()`, `getByte(int)`
- sequential access indexing
- discardable bytes: `discardReadBytes()`
- readable bytes: `isReable()`, `readByte()`
- writable bytes: `writableBytes()`, `writeInt(int)`
- index management: `markReaderIndex()/resetReaderIndex()`, `markWriterIndex()/resetWriterIndex()`, `clear()`
- search operations: `process(byte)`, `forEachByte(...)`
- derived buffers: 
  - view of ByteBuf: `duplicate()`, `slice()`, `Unpolled.unmodifiableBuffer(...)`, `oreder(ByteOrder)`, `readSlice(int)`
  - copy: `copy()`
- read/write operations: `get()/set()`, `read()/write()`
- more operations
  - `isReadable()/isWriteable()`, `readableBytes()/writableBytes()`, `capacity()`, `maxCapacity()`, `hasArray()/array()`

ByteBufHolder: A packet which is send or receive.
- `content()`
- `copy()`: deep copy
- `duplicate()`: shallow/shared copy


ByteBuf allocation
- on-demand: `ByteBufAllocator` 
  - pooling
  - channel.alloc(), ctx.alloc()
- unpooled buffers: `Unpooled` 

ByteBufUtil
- `hexdump()`
- `equals(ByteBuf, ByteBuf)`

ReferenceCounted
- `refCnt()`
- `release()`

## ChannelHandler, ChannelPipeline
Channel lifecycle:
- ChannelUnregistered: Channel created, NOT registered to an EventLoop
- ChannelRegistered: registered to an EventLoop
- ChannelActive: active(connected to its remote peer), possible to receive and send data
- ChannelInactive: NOT connected to remote peer

ChannelHandler lifecycle:
- handlerAdded: called when ChannelHander is added to a ChannelPipeline
- handlerRemoved: called when ChannelHander is removed from a ChannelPipeline
- exceptionCaught: called if an error occurs in the ChannelPipeline during processing

ChannelInboundHandler
- channedlRegistered: invoked when Channel is registered to its EventLoop and able to handle IO
- channelUnregistered
- channelActive: invoked when Channel is connected/bound and ready
- channelInactive: invoked when Channel no longer connected to its remote peer
- channelReadComplete: invoked when a read operation on Channel has completed
- channelRead: invoked if data is read from the Channel
- channelWritabilityChanged: invoked when the writability of Channel changes.
  - ensuer writes are NOT done too quickly(avoid OOM) or can resume writes when Channel become writable again
  - Channel#isWritable()
  - Channel.confg().setWriteHighWaterMark()/setWriteLowWaterMark()
- userEventTriggered: invoked when ChannelInboundHandler#fireUserEventTriggered() is called due to a POJO was passed through ChannelPipeline

ChannelOutboundHandler
- bind: invoked on request to bind Channel to a local address
- connect: invoked on request to connect Channel to the remote peer
- disconnect: invoked on request to disconnect Channel from the the remote peer
- close: invoked on request to close Channel
- deregister: invoked on request to deregister Channel from its EventLoop
- read: invoke on request to read more data from Channel
- flush: invoke on request to flush queued data to the remote peer through Channel
- write: invoked on request to write data through Channel to the remote peer

ChannelPromise: subinterface of ChannelFuture
- setSuccess
- setFailure

adapters:
- ChannelHandlerAdapter
- ChannelInboundHandlerAdapter
- ChannelOutboundHandlerAdapter

for polled ByteBuf: adjust the reference count
- ReferenceCountUtil.release
- ReferenceCounted

- inbound

When a `ChannelInboundHandler` implementation overrides `channelRead()`, it is responsible for explicitly releasing the memory associated with **pooled** `ByteBuf` instances. Netty provides a utility method for this purpose, `ReferenceCountUtil.release()`. 
Netty provides a special `ChannelInboundHandler` implementation called `SimpleChannelInboundHandler`. This
implementation will automatically release a message once it’s consumed by `channelRead0()`.

- outbound

It is the responsibility of the user to call `ReferenceCountUtil.release()` if a message is consumed or discarded and not passed to the next `ChannelOutboundHandler` in the `ChannelPipeline`. If the message reaches the actual transport layer, it will be released automatically when it’s written or the `Channel` is closed.

ResourceLeakDetector
- Whenever you act on data by calling `ChannelInboundHandler.channelRead()` or `ChannelOutboundHandler.write()`, you need to ensure that there are no resource leaks.
- sample 1% of buffer allocations
- level: `io.netty.util.ResourceLeakDetector.Level`
  - DISABLED
  - SIMPLE
  - ADVANCED: report also when the message was accessed
  - PARANOID: every access is sampled

```shell
java -Dio.netty.leakDetectionLevel=ADVANCED
```

ChannelPipeline: a chain of ChannelHandlers that intercept the inbound/outbound evnets flow through a Channel
- new Channel cread is asssigned a new ChannelPipeline, this association is permanent
- event will handled by either ChannelInboundHandler or ChannelOutboundHandler
- subsequently envent will be forwarded to the next handler by a call to a ChannelHandlerContext

methods:
- modify
```java
addFirst(String name, ChannelHandler handler)
addFirst(EventExecutorGroup group, String name, ChannelHandler handler)
```
- access: `get`, `context`, `names`
- firing events - inbound
  - fireChannelRegistered: call next ChannelInboundHandler#channelRegistered(ctx)
  - fireChannelUnregistered: call next ChannelInboundHandler#channelUnregistered(ctx)
  - fireChannelActive: call next ChannelInboundHandler#channelActive(ctx)
  - fireChannelInactive: call next ChannelInboundHandler#channelInactive(ctx)
  - fireExceptionCaught: call next ChannelInboundHandler#exceptionCaught(ctx, t)
  - fireUserEventTriggered: call next ChannelInboundHandler#userEventTriggered(ctx, o)
  - fireChannelRead: call next ChannelInboundHandler#channelRead(ctx, msg)
  - fireChannelReadComplete: call next ChannelInboundHandler#channelReadComplete(ctx)
- firing events - outbound
  - bind: bind Channel to local address. call next ChannelOutboundHandler#bind
  - connect: connect Channel to remote address. call next ChannelOutboundHandler#connect
  - disconnect: disconnect Channel. call next ChannelOutboundHandler#disconnect
  - close: close Channel. call next ChannelOutboundHandler#close
  - deregister: deregister Channel from EventLoop. call next ChannelOutboundHandler#deregister
  - flush: flush all pending writes of Channel. call next ChannelOutboundHandler#flush
  - write: write message to Channel' queue. call next ChannelOutboundHandler#write
  - writeAndFlush: write and flush
  - read: request to read more data from Channel. call next ChannelOutboundHandler#read

ChannelHandlerContext
- an association between a ChannelHandler and a ChannelPipeline, created whenenver a ChannelHandler is added to a ChannelPipeline.
- enable ChannelHandler to interact with its ChannelPipiline and other handlers
- ChannelHandler can notify the next ChannelHandler in the ChannelPipeline and even dynamically modify the ChannelPipeline it belongs to.
- API: handle events, perform IO operations.
  - some also present on Channel and ChannelPipeline: BUT start at the current associated ChannelHandler, propagate to the next ChannelHandler in the pipeline which can handle the event.

methods:
- bind: bind to SocketAddress
- channel
- close: close the channel
- connect: connect to SocketeAddress
- deregister: deregister from assigned EventExecutor
- disconnect: disconnect from remote peer
- executor: the EventExecutor that dispatch events
- fireChannelActive: trigger call to channelActive() on next ChannelInboundHandler
- fireChannelInactive: trigger call to channelInactive() on next ChannelInboundHandler
- fireChannelRead: trigger call to channelRead() on next ChannelInboundHandler
- fireChannelReadComplete: trigger channelWritabilityChanged event to next ChannelInboundHandler
- handler
- isRemoved
- name
- pipeline
- read: read data from Channel into the first inbound buffer. trigger channelRead event if successful, notify the handler channelReadComplete.
- write: write message via this instance through the pipeline

advanced uses:
- dynamic protocol change: ChannelHandlerContext#pipeline()
- cache a reference to ChannelHandlerContext
- `@Sharable`: a ChannelHandler can belong to more than one ChannelPipeline

exception handling
- inbound exceptions
  - flow through the ChannelPipeline starting at the ChannelInboundHandler where it was triggered.
  - `ChannelInboundHandler#exceptionCaught(ctx, e)`
- outbound exceptions
  - every outbound operation return ChannelFure. registered ChannelFutureListerners are notifyed of sucess or error when operation completes.
  - ChannelOutboundHandler's method parameter ChannelPromise 

## EventLoop

- threading pooling pattern

EventLoop
- powered by one Thread that never changes
- immediate or scheduled execution
- one EventLoop may be assigned to service multiple Channels
- `scheculre()`, `scheduleAtFixedRate()`
- internal task queue: if the calling Thread is not the Channel related EventLoop
- long runnint tasks: use a dedicated EventExecutor

Netty 4: all IO operations and events are handled by the Thread assigned to the EventLoop.

Netty 3: inbound events are executed in IO thread, outbound events are handled by the calling thread (IO thread or any other).

EventLoopGroup
- asynchronous transport: a EventLoop/Thread is shared among Channels
- bloking transport: each Channel gets a dedicated EventLoop

## Bootstrapping

ServerBootstrap
- group(EventLoopGroup), group(EventLoopGroup, EventLoopGroup)
- channel, channelFactory
- localAddress
- option: apply to ServerChannel's ChannelConfig
- childOption: apply to Channel's ChannelConfig
- attr: attribute on ServerChannel, set by bind()
- childAttr: attribute on accepted Channels
- handler: ServerChannel's ChannelPipeline
- childHandler: accepted Channels' ChannelPipeline
- bind: bind the ServerChannel
- clone

Bootstrap: clients, connectionless protocols
- group(EventLoopGroup)
- channel, channelFactory
- localAddress
- remoteAddress
- option
- attr
- handler
- bind: bind the Channel. - UDP
- connect: connect to the remote peer, establish the connection. - TCP
- clone

ChannelInitializer

ChannelOption

AttributeMap, `AttributeKey<T>`
- Bootstrap#attr
- Channel#attr: never return null

Unit testing

EmbeddedChannel
- writeInbound
- readInbound
- writeOutbound
- readOutbound
- finish: return true if inbound/outbound data can be read. call close()

## Codec Framework

codec:
- encoder: convert message to network byte stream. - outbound data
- decoder: convert network byte stream to message - inbound data

decoders
- ByteToMessageDecoder
  - decode: called repeated until no new items have been added to List or no more bytes are readable in ByteBuf. if List is not empty, its content are passed to next handler.
  - decodeLast: called once when Channel goes inactive, default call decode()
- ReplayingDecoder: extends ByteToMessageDecoder
  - ReplayingDecoderBuffer: not all ByteBuf operations are supported.
- MessageToMessageDecoder


More: `io.netty.handler.codec`
- LineBasedFrameDecoder
- HttpObjectDecoder
- HttpObjectAggregator

exceptions:
- TooLongFrameException

encoders
- MessageToByteEncoder
  - encode
- MessageToMessageEncoder 

abstract codec classes:
- ByteToMessageCodec
  - decode
  - decodeLast
  - encode
- MessageToMessageCodec 
  - decode
  - encode
- CombinedChannelDuplexHandler: a container for a ChannelInboundHandler and a ChannelOutboundHandler

## Provided ChannelHanders and codes

SSL/TLS
- `javax.net.ssl`, SSLContext, SSLEngine
- SslHandler

HTTP/HTTPS
- FullHttpRequest: HttpRequest, HttpContent, LastHttpContent
- FullHttpResponse: HttpResponse, HttpContent, LastHttpContent
- HttpRequestEncoder
- HttpResponseEncoder
- HttpRequestDecoder
- HttpResponseDecoder
- HttpClientCodec
- HttpServerCodec
- message aggregation: HttpObjectAggregator
- compression: HttpContentDecompressor, HttpContentCompressor
- WebSocket: WebSocketFrame
  - WebSocketServerProtocolHandler
  - TextFrameHandler: TextWebSocketFrame
  - BinaryFrameHandler: BinaryWebSocketFrame
  - ContinuationFrameHandler: ContinuationWebSocketFrame

idle connections, timeouts
- IdleStateHandler: connection idles too long
  - IdleStateEvent
  - ChannelInboundHandler#userEventTriggered
- ReadTimeoutHandler: throw exception and close Channel when no inbound data is received for specified interval
  - ReadTimeoutException
  - ChannelHandler#exceptionCaught 
- WriteTimeoutHandler: throw exception and close Channel when a write operation cannot finish in specified interval. 
  - WriteTimeoutException
  - ChannelHandler#exceptionCaught 

delimited and length-based protocols
- DelimiterBasedFrameDecoder
- LineBasedFrameDecoder
- FixedLengthFrameDecoder
- LengthFieldBasedFrameDecoder

writing big data:
- FileRegion
- DefaultFileRegion: zero-copy
- ChunkedWriteHandler: need to copy data from file system to user memory
  - ChunkedInput: ChunkedFile, ChunkedNioFile, ChunkedStream, ChunkedNioStream

serializing data
- JDK serialization
  - CompatibleObjectDecoder, CompatibleObjectEncoder, 
  - ObjectDecoder, ObjectEncoder
- JBoss Marshalling
  - CompatibleMarshallingDecoder, CompatibleMarshallingEncoder
  - MarshallingDecoder, MarshallingEncoder
- Protocol Buffers
  - ProtobufDecoder
  - ProtobufEncoder
  - ProtobufVarint32FrameDecoder

## Protocol: WebSocket
- HttpServerCodec
- ChunkedWriteHandler
- HttpObjectAggregator: FullHttpRequest
- WebSocketServerProtocolHandler
  - HandshakeComplete
- SslHandler 

## Protocol: UDP

- AddressedEnvelope, DefaultAddressedEnvelope
- DatagramPacket
- DatagramChannel, NioDatagramChannnel


<!--
# RTFSC

`D:\workspace\rtfsc\netty` Branch 4.1.
## System Properties
- [Document available system properties](https://github.com/netty/netty/issues/6305)
```shell
find . -name \*.java -print0 | xargs -0 grep -e '"io\.netty\.' | grep -v Test
# manual remove package
```

### common

- common/concurrent/DefaultPromise.java:            SystemPropertyUtil.getInt("io.netty.defaultPromise.maxListenerStackDepth", 8));
- common/concurrent/GlobalEventExecutor.java:        int quietPeriod = SystemPropertyUtil.getInt("io.netty.globalEventExecutor.quietPeriodSeconds", 1);
- common/concurrent/SingleThreadEventExecutor.java:            SystemPropertyUtil.getInt("io.netty.eventexecutor.maxPendingTasks", Integer.MAX_VALUE));
- common/internal/Hidden.java:                    "io.netty.channel.nio.NioEventLoop",
- common/internal/Hidden.java:                    "io.netty.channel.kqueue.KQueueEventLoop",
- common/internal/Hidden.java:                    "io.netty.channel.epoll.EpollEventLoop",
- common/internal/Hidden.java:                    "io.netty.util.HashedWheelTimer",
- common/internal/Hidden.java:                    "io.netty.util.HashedWheelTimer",
- common/internal/Hidden.java:                    "io.netty.util.HashedWheelTimer$Worker",
- common/internal/Hidden.java:                    "io.netty.util.concurrent.SingleThreadEventExecutor",
- common/internal/Hidden.java:                    "io.netty.buffer.PoolArena",
- common/internal/Hidden.java:                    "io.netty.buffer.PoolSubpage",
- common/internal/Hidden.java:                    "io.netty.buffer.PoolChunk",
- common/internal/Hidden.java:                    "io.netty.buffer.PoolChunk",
- common/internal/Hidden.java:                    "io.netty.buffer.AdaptivePoolingAllocator$1",
- common/internal/Hidden.java:                    "io.netty.buffer.AdaptivePoolingAllocator$1",
- common/internal/Hidden.java:                    "io.netty.handler.ssl.SslHandler",
- common/internal/Hidden.java:                    "io.netty.handler.ssl.SslHandler",
- common/internal/Hidden.java:                    "io.netty.handler.ssl.SslHandler",
- common/internal/Hidden.java:                    "io.netty.util.concurrent.GlobalEventExecutor",
- common/internal/Hidden.java:                    "io.netty.util.concurrent.GlobalEventExecutor",
- common/internal/Hidden.java:                    "io.netty.util.concurrent.SingleThreadEventExecutor",
- common/internal/Hidden.java:                    "io.netty.util.concurrent.SingleThreadEventExecutor",
- common/internal/Hidden.java:                    "io.netty.handler.ssl.ReferenceCountedOpenSslClientContext$ExtendedTrustManagerVerifyCallback",
- common/internal/Hidden.java:                    "io.netty.handler.ssl.JdkSslContext$Defaults",
- common/internal/Hidden.java:                    "io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider",
- common/internal/Hidden.java:                    "io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider",
- common/internal/Hidden.java:                    "io.netty.resolver.dns.UnixResolverDnsServerAddressStreamProvider",
- common/internal/Hidden.java:                    "io.netty.resolver.HostsFileEntriesProvider$ParserImpl",
- common/internal/Hidden.java:                    "io.netty.util.NetUtil$SoMaxConnAction",
- common/internal/Hidden.java:            builder.allowBlockingCallsInside("io.netty.util.internal.ReferenceCountUpdater",
- common/internal/Hidden.java:            builder.allowBlockingCallsInside("io.netty.util.internal.PlatformDependent", "createTempFile");
- common/internal/InternalThreadLocalMap.java:                SystemPropertyUtil.getInt("io.netty.threadLocalMap.stringBuilder.initialSize", 1024);
- common/internal/InternalThreadLocalMap.java:                SystemPropertyUtil.getInt("io.netty.threadLocalMap.stringBuilder.maxSize", 1024 * 4);
- common/internal/NativeLibraryLoader.java:        String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
- common/internal/NativeLibraryLoader.java:                "io.netty.native.deleteLibAfterLoading", true);
- common/internal/NativeLibraryLoader.java:                "io.netty.native.tryPatchShadedId", true);
- common/internal/NativeLibraryLoader.java:                "io.netty.native.detectNativeLibraryDuplicates", true);
- common/internal/NativeLibraryLoader.java:                    // Pass "io.netty.native.workdir" as an argument to allow shading tools to see
- common/internal/NativeLibraryLoader.java:                                tmpFile.getPath(), "io.netty.native.workdir");
- common/internal/ObjectCleaner.java:            max(500, getInt("io.netty.util.internal.ObjectCleaner.refQueuePollTimeout", 10000));
- common/internal/PlatformDependent.java:        long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1);
- common/internal/PlatformDependent.java:                SystemPropertyUtil.getInt("io.netty.uninitializedArrayAllocationThreshold", 1024);
- common/internal/PlatformDependent.java:                                  && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false);
- common/internal/PlatformDependent.java:        String osClassifiersPropertyName = "io.netty.osClassifiers";
- common/internal/PlatformDependent.java:            f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
- common/internal/PlatformDependent.java:        int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
- common/internal/PlatformDependent0.java:        boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
- common/internal/PlatformDependent0.java:        if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
- common/internal/PlatformDependent0.java:            unsafePropName = "io.netty.tryUnsafe";
- common/internal/PlatformDependent0.java:        return SystemPropertyUtil.getBoolean("io.netty.tryReflectionSetAccessible",
- common/internal/svm/CleanerJava6Substitution.java:@TargetClass(className = "io.netty.util.internal.CleanerJava6")
- common/internal/svm/PlatformDependent0Substitution.java:@TargetClass(className = "io.netty.util.internal.PlatformDependent0")
- common/internal/svm/PlatformDependentSubstitution.java:@TargetClass(className = "io.netty.util.internal.PlatformDependent")
- common/internal/svm/UnsafeRefArrayAccessSubstitution.java:@TargetClass(className = "io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess")
- common/internal/ThreadLocalRandom.java:        initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0);
- common/NettyRuntime.java:         * This can be overridden by setting the system property "io.netty.availableProcessors" or by invoking
- common/NettyRuntime.java:                                "io.netty.availableProcessors",
- common/NettyRuntime.java:     * can be overridden by setting the system property "io.netty.availableProcessors" or by invoking
- common/NetUtil.java:                    if (SystemPropertyUtil.getBoolean("io.netty.net.somaxconn.trySysctl", false)) {
- common/Recycler.java:        int maxCapacityPerThread = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacityPerThread",
- common/Recycler.java:                SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", DEFAULT_INITIAL_MAX_CAPACITY_PER_THREAD));
- common/Recycler.java:        DEFAULT_QUEUE_CHUNK_SIZE_PER_THREAD = SystemPropertyUtil.getInt("io.netty.recycler.chunkSize", 32);
- common/Recycler.java:        RATIO = max(0, SystemPropertyUtil.getInt("io.netty.recycler.ratio", 8));
- common/Recycler.java:        BLOCKING_POOL = SystemPropertyUtil.getBoolean("io.netty.recycler.blocking", false);
- common/Recycler.java:        BATCH_FAST_TL_ONLY = SystemPropertyUtil.getBoolean("io.netty.recycler.batchFastThreadLocalOnly", true);
- common/ResourceLeakDetector.java:    private static final String PROP_LEVEL_OLD = "io.netty.leakDetectionLevel";
- common/ResourceLeakDetector.java:    private static final String PROP_LEVEL = "io.netty.leakDetection.level"; - 资源泄露检查的等级
- common/ResourceLeakDetector.java:    private static final String PROP_TARGET_RECORDS = "io.netty.leakDetection.targetRecords";
- common/ResourceLeakDetector.java:    private static final String PROP_SAMPLING_INTERVAL = "io.netty.leakDetection.samplingInterval";
- common/ResourceLeakDetector.java:        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
- common/ResourceLeakDetector.java:            disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
- common/ResourceLeakDetectorFactory.java:                customLeakDetector = SystemPropertyUtil.get("io.netty.customResourceLeakDetector");
- common/ThreadDeathWatcher.java:        String serviceThreadPrefix = SystemPropertyUtil.get("io.netty.serviceThreadPrefix");

### buffer

- buffer/AbstractByteBuf.java:    private static final String LEGACY_PROP_CHECK_ACCESSIBLE = "io.netty.buffer.bytebuf.checkAccessible";
- buffer/AbstractByteBuf.java:    private static final String PROP_CHECK_ACCESSIBLE = "io.netty.buffer.checkAccessible";
- buffer/AbstractByteBuf.java:    private static final String PROP_CHECK_BOUNDS = "io.netty.buffer.checkBounds";
- buffer/AdaptiveByteBufAllocator.java:                "io.netty.allocator.useCachedMagazinesForNonEventLoopThreads", false);
- buffer/AdaptivePoolingAllocator.java:            "io.netty.allocator.centralQueueCapacity", NettyRuntime.availableProcessors()));
- buffer/AdaptivePoolingAllocator.java:            "io.netty.allocator.magazineBufferQueueCapacity", 1024);
- buffer/AdvancedLeakAwareByteBuf.java:    private static final String PROP_ACQUIRE_AND_RELEASE_ONLY = "io.netty.leakDetection.acquireAndReleaseOnly";
- buffer/ByteBufUtil.java:                "io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled");
- buffer/ByteBufUtil.java:        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 0);
- buffer/ByteBufUtil.java:        MAX_CHAR_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.maxThreadLocalCharBufferSize", 16 * 1024);
- buffer/PooledByteBufAllocator.java:                "io.netty.allocator.directMemoryCacheAlignment", 0);
- buffer/PooledByteBufAllocator.java:        int defaultPageSize = SystemPropertyUtil.getInt("io.netty.allocator.pageSize", 8192);
- buffer/PooledByteBufAllocator.java:        int defaultMaxOrder = SystemPropertyUtil.getInt("io.netty.allocator.maxOrder", 9);
- buffer/PooledByteBufAllocator.java:                        "io.netty.allocator.numHeapArenas",
- buffer/PooledByteBufAllocator.java:                        "io.netty.allocator.numDirectArenas",
- buffer/PooledByteBufAllocator.java:        DEFAULT_SMALL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.smallCacheSize", 256);
- buffer/PooledByteBufAllocator.java:        DEFAULT_NORMAL_CACHE_SIZE = SystemPropertyUtil.getInt("io.netty.allocator.normalCacheSize", 64);
- buffer/PooledByteBufAllocator.java:                "io.netty.allocator.maxCachedBufferCapacity", 32 * 1024);
- buffer/PooledByteBufAllocator.java:                "io.netty.allocator.cacheTrimInterval", 8192);
- buffer/PooledByteBufAllocator.java:        if (SystemPropertyUtil.contains("io.netty.allocation.cacheTrimIntervalMillis")) {
- buffer/PooledByteBufAllocator.java:            if (SystemPropertyUtil.contains("io.netty.allocator.cacheTrimIntervalMillis")) {
- buffer/PooledByteBufAllocator.java:                        "io.netty.allocator.cacheTrimIntervalMillis", 0);
- buffer/PooledByteBufAllocator.java:                        "io.netty.allocation.cacheTrimIntervalMillis", 0);
- buffer/PooledByteBufAllocator.java:                    "io.netty.allocator.cacheTrimIntervalMillis", 0);
- buffer/PooledByteBufAllocator.java:                "io.netty.allocator.useCacheForAllThreads", false);
- buffer/PooledByteBufAllocator.java:                "io.netty.allocator.disableCacheFinalizersForFastThreadLocalThreads", false);
- buffer/PooledByteBufAllocator.java:                "io.netty.allocator.maxCachedByteBuffersPerChunk", 1023);

### codec

- codec/compression/JdkZlibEncoder.java:                "io.netty.jdkzlib.encoder.maxInitialOutputBufferSize",
- codec/compression/JdkZlibEncoder.java:                "io.netty.jdkzlib.encoder.maxInputBufferSize",
- codec/compression/Snappy.java:            SystemPropertyUtil.getBoolean("io.netty.handler.codec.compression.snappy.reuseHashTable", false);
- codec/compression/ZlibCodecFactory.java:        noJdkZlibDecoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibDecoder",
- codec/compression/ZlibCodecFactory.java:        noJdkZlibEncoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibEncoder", false);
- codec-http2/http2/WeightedFairQueueByteDistributor.java:            max(1, SystemPropertyUtil.getInt("io.netty.http2.childrenMapSize", 2));

### handler

- handler/ssl/ConscryptAlpnSslEngine.java:            "io.netty.handler.ssl.conscrypt.useBufferAllocator", true);
- handler/ssl/OpenSsl.java:        if (SystemPropertyUtil.getBoolean("io.netty.handler.ssl.noOpenSsl", false)) {
- handler/ssl/OpenSsl.java:                Class.forName("io.netty.internal.tcnative.SSLContext", false,
- handler/ssl/OpenSsl.java:                    String engine = SystemPropertyUtil.get("io.netty.handler.ssl.openssl.engine", null);
- handler/ssl/OpenSsl.java:                                        "io.netty.handler.ssl.openssl.useKeyManagerFactory");
- handler/ssl/OpenSsl.java:                                            "io.netty.handler.ssl.openssl.useKeyManagerFactory", true);
- handler/ssl/ReferenceCountedOpenSslContext.java:            SystemPropertyUtil.getInt("io.netty.handler.ssl.openssl.bioNonApplicationBufferSize",
- handler/ssl/ReferenceCountedOpenSslContext.java:            SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.useTasks", true);
- handler/ssl/ReferenceCountedOpenSslContext.java:            SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.sessionCacheServer", true);
- handler/ssl/ReferenceCountedOpenSslContext.java:            SystemPropertyUtil.getBoolean("io.netty.handler.ssl.openssl.sessionCacheClient", true);
- handler/ssl/SslHandler.java:                if (classname.startsWith("io.netty.")) {
- handler/ssl/SslMasterKeyHandler.java:    public static final String SYSTEM_PROP_KEY = "io.netty.ssl.masterKeyHandler";
- handler/ssl/SslMasterKeyHandler.java:                InternalLoggerFactory.getInstance("io.netty.wireshark");
- handler/ssl/util/SelfSignedCertificate.java:            "io.netty.selfSignedCertificate.defaultNotBefore", System.currentTimeMillis() - 86400000L * 365));
- handler/ssl/util/SelfSignedCertificate.java:            "io.netty.selfSignedCertificate.defaultNotAfter", 253402300799000L));
- handler/ssl/util/SelfSignedCertificate.java:            SystemPropertyUtil.getInt("io.netty.handler.ssl.util.selfSignedKeyStrength", 2048);
- handler-ssl-ocsp/ssl/ocsp/OcspClient.java:            "io.netty.ocsp.responseSize", 1024 * 10);
- handler-ssl-ocsp/ssl/ocsp/OcspServerCertificateValidator.java:            AttributeKey.newInstance("io.netty.handler.ssl.ocsp.pipeline");


### transport

- transport/bootstrap/ChannelInitializerExtension.java:    public static final String EXTENSIONS_SYSTEM_PROPERTY = "io.netty.bootstrap.extensions";
- transport/AbstractChannelHandlerContext.java:                SystemPropertyUtil.getBoolean("io.netty.transport.estimateSizeOnSubmit", true);
- transport/AbstractChannelHandlerContext.java:                SystemPropertyUtil.getInt("io.netty.transport.writeTaskSizeOverhead", 32);
- transport/ChannelOutboundBuffer.java:            SystemPropertyUtil.getInt("io.netty.transport.outboundBufferEntrySizeOverhead", 96);
- transport/DefaultChannelId.java:        String customProcessId = SystemPropertyUtil.get("io.netty.processId");
- transport/DefaultChannelId.java:        String customMachineId = SystemPropertyUtil.get("io.netty.machineId");
- transport/MultithreadEventLoopGroup.java:                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2)); - EventLoopGroup中线程数量
- transport/nio/NioEventLoop.java:            SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
- transport/nio/NioEventLoop.java:        int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
- transport/PendingWriteQueue.java:            SystemPropertyUtil.getInt("io.netty.transport.pendingWriteSizeOverhead", 64);
- transport/pool/SimpleChannelPool.java:        AttributeKey.newInstance("io.netty.channel.pool.SimpleChannelPool");
- transport/SingleThreadEventLoop.java:            SystemPropertyUtil.getInt("io.netty.eventLoop.maxPendingTasks", Integer.MAX_VALUE));
- transport/nativeimage/ChannelHandlerMetadataUtil.java:        if (Arrays.asList(packageNames).contains("io.netty.channel")) {
- transport-classes-epoll/epoll/Epoll.java:        if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {
- transport-classes-epoll/epoll/EpollEventLoop.java:            SystemPropertyUtil.getLong("io.netty.channel.epoll.epollWaitThreshold", 10);
- transport-classes-kqueue/kqueue/KQueue.java:        if (SystemPropertyUtil.getBoolean("io.netty.transport.noNative", false)) {

### microbench

- microbench/ByteBufAccessBenchmark.java:        System.setProperty("io.netty.buffer.checkAccessible", checkAccessible);
- microbench/ByteBufAccessBenchmark.java:        System.setProperty("io.netty.buffer.checkBounds", checkBounds);
- microbench/ByteBufAccessBenchmark.java:        System.clearProperty("io.netty.buffer.checkAccessible");
- microbench/ByteBufAccessBenchmark.java:        System.clearProperty("io.netty.buffer.checkBounds");
- microbench/ByteBufZeroingBenchmark.java:        System.setProperty("io.netty.buffer.checkAccessible", checkAccessible);
- microbench/ByteBufZeroingBenchmark.java:        System.setProperty("io.netty.buffer.checkBounds", checkBounds);
- microbench/buffer/ByteBufBenchmark.java:        System.setProperty("io.netty.buffer.checkAccessible", "false");
- microbench/buffer/ByteBufBenchmark.java:        System.setProperty("io.netty.buffer.checkBounds", checkBounds);
- microbench/buffer/ByteBufCopyBenchmark.java:        System.setProperty("io.netty.buffer.bytebuf.checkAccessible", "false");
- microbench/buffer/ByteBufIndexOfBenchmark.java:        System.setProperty("io.netty.noUnsafe", Boolean.valueOf(noUnsafe).toString());
- microbench/buffer/ByteBufLastIndexOfBenchmark.java:        System.setProperty("io.netty.noUnsafe", Boolean.valueOf(noUnsafe).toString());
- microbench/buffer/HeapByteBufBenchmark.java:        System.setProperty("io.netty.buffer.bytebuf.checkBounds", checkBounds);
- microbench/buffer/HeapByteBufBenchmark.java:        unsafeBuffer = newBuffer("io.netty.buffer.UnpooledUnsafeHeapByteBuf");
- microbench/buffer/HeapByteBufBenchmark.java:        buffer = newBuffer("io.netty.buffer.UnpooledHeapByteBuf");
- microbench/buffer/Utf8EncodingBenchmark.java:        System.setProperty("io.netty.noUnsafe", Boolean.valueOf(noUnsafe).toString());
- microbench/channel/DefaultChannelIdBenchmark.java:        System.setProperty("io.netty.noUnsafe", Boolean.valueOf(noUnsafe).toString());
- microbench/AsciiStringCaseConversionBenchmark.java:        System.setProperty("io.netty.noUnsafe", Boolean.valueOf(noUnsafe).toString());

### resolver

- resolver/DefaultHostsFileEntriesResolver.java:                "io.netty.hostsFileRefreshInterval", /*nanos*/0);
- resolver-dns/dns/DefaultDnsServerAddressStreamProvider.java:    private static final String DEFAULT_FALLBACK_SERVER_PROPERTY = "io.netty.resolver.dns.defaultNameServerFallback";
- resolver-dns/dns/DnsNameResolver.java:            AttributeKey.newInstance("io.netty.resolver.dns.pipeline");
- resolver-dns/dns/DnsQueryContext.java:                SystemPropertyUtil.getLong("io.netty.resolver.dns.idReuseOnTimeoutDelayMillis", 10000);
- resolver-dns/dns/DnsResolveContext.java:            "io.netty.resolver.dns.tryCnameOnAddressLookups";
- resolver-dns/dns/DnsServerAddressStreamProviders.java:            "io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider";

### testsuite

- testsuite-shadingShadingIT.java:                "io.netty.channel.kqueue.KQueue" : "io.netty.channel.epoll.Epoll";
- testsuite-shadingShadingIT.java:        String className = "io.netty.handler.ssl.OpenSsl";

## Common
- io.netty.util.concurrent.Future
- io.netty.util.concurrent.Promise

java.util.concurrent.Future
- boolean cancel(boolean mayInterruptIfRunning)
- boolean isCancelled() // canceld before completed
- boolean isDone(): completed: due to normal termination, an exception, cancellation
- V get() throws InterruptedException, ExecutionException // CancellationException
- V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException

io.netty.util.concurrent.Future: extends java.util.concurrent.Future
- boolean isSuccess() // whether the I/O operation was completed successfully
- boolean isCancellable()
- Throwable cause()
- addListener
- removeListener
- sync
- await
- V getNow()

io.netty.channel.ChannelFuture
## Bootstrapping
- ServerBootstrap
- Bootstrap
ServerBootstrap

fileds:
- Map<ChannelOption<?>, Object> childOptions
- Map<AttributeKey<?>, Object> childAttrs
- ServerBootstrapConfig config
- EventLoopGroup childGroup
- ChannelHandler childHandler
- AbstractBootstrap
  - EventLoopGroup group
  - ChannelFactory<? extends C> channelFactory
  - SocketAddress localAddress
  - Map<ChannelOption<?>, Object> options
  - Map<AttributeKey<?>, Object> attrs
  - ChannelHandler handler
  - ClassLoader extensionsClassLoader

entry poit: io.netty.bootstrap.AbstractBootstrap#bind(java.net.SocketAddress)
- pipeline实例化: io.netty.channel.AbstractChannel#AbstractChannel(io.netty.channel.Channel)


case: NIO
- NioEventLoopGroup: extends MultithreadEventLoopGroup
  - NioEventLoop: extends SingleThreadEventLoop
- NioServerSocketChannel

```java
io.netty.bootstrap.AbstractBootstrap#bind()
\-- io.netty.bootstrap.AbstractBootstrap#doBind
\--\-- io.netty.bootstrap.AbstractBootstrap#initAndRegister
\--\--\-- io.netty.bootstrap.ChannelFactory#newChannel // 1. 创建Channel, 使用DefaultChannelPipeline
\--\--\-- io.netty.bootstrap.ServerBootstrap#init // 2. 初始化: 设置选项和属性, 添加Handler
setChannelOptions
setAttributes
pipeline addLast handler
pipeline addLast ServerBootstrapAcceptor
\--\--\-- io.netty.channel.EventLoopGroup#register(io.netty.channel.Channel) // 3. 注册Channel到EventLoop
io.netty.channel.AbstractChannel.AbstractUnsafe#register0
\--\-- io.netty.bootstrap.AbstractBootstrap#doBind0
bind localAddress // 4. 执行Socket绑定
```
Bootstrap

```java
io.netty.bootstrap.Bootstrap#connect()
\-- io.netty.bootstrap.Bootstrap#doResolveAndConnect
\--\-- io.netty.bootstrap.AbstractBootstrap#initAndRegister
io.netty.bootstrap.ChannelFactory#newChannel
io.netty.channel.EventLoopGroup#register(io.netty.channel.Channel)
\--\-- io.netty.bootstrap.Bootstrap#doResolveAndConnect0
\--\--\-- io.netty.bootstrap.Bootstrap#doConnect
io.netty.channel.ChannelOutboundInvoker#connect(java.net.SocketAddress, io.netty.channel.ChannelPromise)
```
## Channel

io.netty.channel.Channel
- id
- parent
- EventLoop
- ChannelPipeline
- ChannelConfig
- state: isOpen, isActive, isWritable
  - bytesBeforeUnwritable
  - bytesBeforeWritable
- ChannelMetadata
- localAddress
- remoteAddress
- closeFuture
- Unsafe
- ByteBufAllocator
- ops: read(), flush()

io.netty.channel.Channel.Unsafe: 用于实现实际的传输, 通常需要在IO线程中调用.
- RecvByteBufAllocator.Handle
- localAddress
- remoteAddress
- register: EventLoop, ChannelPromise
- deregister: ChannelPromise
- bind: localAddress, ChannelPromise
- connect: remoteAddress, localAddress, ChannelPromise
- disconnect: ChannelPromise
- close: ChannelPromise
- beginRead
- write: msg, ChannelPromise
- flush
- voidPromise
- ChannelOutboundBuffer

case NIO
- NioServerSocketChannel: extends AbstractNioMessageChannel implements ServerSocketChannel
  - Unsafe: io.netty.channel.nio.AbstractNioMessageChannel.NioMessageUnsafe
- NioSocketChannel: extends AbstractNioByteChannel implements SocketChannel
  - Unsafe: io.netty.channel.socket.nio.NioSocketChannel.NioSocketChannelUnsafe
## ByteBuf

- project: buffer
ByteBufAllocator

usage:
- io.netty.channel.ChannelConfig#getAllocator: io.netty.channel.ChannelOption#ALLOCATOR
- io.netty.channel.ChannelHandlerContext#alloc

system property:
- `io.netty.allocator.type`: unpooled, pooled, adaptive

## EventLoop
NioEventLoopGroup

```java
io.netty.channel.nio.NioEventLoopGroup
\-- java.nio.channels.spi.SelectorProvider#provider
\-- io.netty.channel.DefaultSelectStrategyFactory#INSTANCE
\-- io.netty.util.concurrent.DefaultEventExecutorChooserFactory#INSTANCE

io.netty.channel.nio.NioEventLoopGroup#newChild -> NioEventLoop
io.netty.channel.nio.NioEventLoop#run // run the event loop
\-- io.netty.channel.nio.NioEventLoop#processSelectedKey(java.nio.channels.SelectionKey, io.netty.channel.nio.AbstractNioChannel) // OP_CONNECT, OP_ACCEPT, OP_WRITE, OP_READ
```
## Accept

- `ServerBootstrapAcceptor#channelRead`

```
calling trace:

channelRead:207, ServerBootstrap$ServerBootstrapAcceptor (io.netty.bootstrap)
invokeChannelRead:444, AbstractChannelHandlerContext (io.netty.channel)
invokeChannelRead:420, AbstractChannelHandlerContext (io.netty.channel)
fireChannelRead:412, AbstractChannelHandlerContext (io.netty.channel)
channelRead:1410, DefaultChannelPipeline$HeadContext (io.netty.channel)
invokeChannelRead:440, AbstractChannelHandlerContext (io.netty.channel)
invokeChannelRead:420, AbstractChannelHandlerContext (io.netty.channel)
fireChannelRead:919, DefaultChannelPipeline (io.netty.channel)               // fire read event
read:97, AbstractNioMessageChannel$NioMessageUnsafe (io.netty.channel.nio)   
processSelectedKey:788, NioEventLoop (io.netty.channel.nio)
processSelectedKeysOptimized:724, NioEventLoop (io.netty.channel.nio)
processSelectedKeys:650, NioEventLoop (io.netty.channel.nio)
run:562, NioEventLoop (io.netty.channel.nio)                                 // run event loop
run:997, SingleThreadEventExecutor$4 (io.netty.util.concurrent)
run:74, ThreadExecutorMap$2 (io.netty.util.internal)
run:30, FastThreadLocalRunnable (io.netty.util.concurrent)
run:834, Thread (java.lang)

works:

1. child pipeline add Handler
2. set child channel options and attributes
3. register child channel to child event loop group
```

## Read
## Write

- Channel.write()
- ChannelHandlerContext.write()
Channel#write()

```java
io.netty.channel.ChannelOutboundInvoker#write(java.lang.Object)
io.netty.channel.AbstractChannel#write(java.lang.Object)
io.netty.channel.DefaultChannelPipeline#write(java.lang.Object) // TailContext
io.netty.channel.AbstractChannelHandlerContext#write(java.lang.Object) 

handler DefaultChannelPipeline.HeadContext?
io.netty.channel.Channel.Unsafe#write // 实际写入
```
ChannelHandlerContext#write()

```java
io.netty.channel.AbstractChannelHandlerContext#write(java.lang.Object)
```


-->


<!--
# Netty高可靠性设计
* Netty权威指南 - 23.2 Netty高可靠性设计
网络通信类故障
- 客户端连接超时: ChannelOption.CONNECT_TIMEOUT_MILLIS, 使用EventLoop.schedule监测
- 通信对端强制关闭连接: 入口ByteBuf.writeBytes(...), 异常处理AbstractNioByteChannel.NioByteUnsafe#closeOnRead
- 链路关闭: 通过AbstractNioByteChannel.NioByteUnsafe#read, SocketChannel.read返回-1表示连接已被对方关闭
- 定制IO故障: 发生IO异常时, 释放底层资源, 使用exceptionCaught(...)通知用户
链路的有效性检测: 心跳检测
- Ping-Pong型心跳, Ping-Ping型心跳
- 异常情况
  - 心跳超时: 连续n次心跳检测没有收到Pong应答或Ping请求
  - 心跳失败: 读取发送Ping请求和读取Pong应答时发生UI异常
- 空闲检测机制: 读空闲, 写空闲, 读写空闲
- WriteTimeoutHandler, ReadTimeoutHandler, IdleStateHandler, channelIdle(...)

Reactor线程的保护
- 异常处理: 捕获Throwable
- 规避NIO BUG
  - 例: epoll bug, Selector空轮询, IO线程CPU 100%. 解决方法: 检测BUG是否发生, 创建新Selector, 将问题Selector上注册的Channel转移到新Selector, 关闭问题Selector, 使用新Selector.
内存保护
- 链路总数的控制: 链路包含接收和发送缓冲区
- 单个缓冲区的上限控制: 防止非法长度或消息过大
- 缓冲区内存释放
- NIO消息发送队列的长度上限控制

缓冲区的内存泄漏保护
- 内存池PooledByteBuf, 对象池
- 在尾部的handler的channelRead(...)中释放消息的引用计数

缓冲区溢出保护
- 指定缓冲区长度上限
- 消息解码时判断消息长度
流量整形
- 主动调整流量输出速率: 根据下游的事务处理指标来控制本地流量的流出
- 将不符合流量形状的报文放入缓冲区或队列中
- 全局流量整形 GlobalTrafficShapingHanlder
  - 报文接收速率, 报文发送速率, 整形周期
  - 对ByteBuf中可读写字节数计数, 获取流量数值, 与阈值比较. 如果超过, 计算等待时间后将ByteBuf放到定时任务中缓存后续处理.
- 链路流量整形 ChannelTrafficShapingHandler
  - 以单个链路作为作用域
优雅停机接口
- JDK ShutdownHook
- EventExecutorGroup#shutdownGracefully()
- NioEventLoop#closeAll()

-->


<!--
# Questions
## TCP拆包、粘包和解决方案

因为TCP只负责数据发送，并不处理业务上的数据，所以只能在上层应用协议栈解决，目前的解决方案归纳：

1. 消息定长，每个报文的大小固定，如果数据不够，空位补空格。
2. 在包的尾部加回车换行符标识。
3. 将消息分为消息头与消息体，消息头中包含消息总长度。
4. 设计更复杂的协议。

Netty提供了多种默认的编码器解决粘包和拆包：
1. LineBasedFrameDecoder
基于回车换行符的解码器，当遇到"n"或者 "rn"结束符时，分为一组。支持携带结束符或者不带结束符两种编码方式，也支持配置单行的最大长度。
2. DelimiterBasedFrameDecoder
分隔符解码器，可以指定消息结束的分隔符，它可以自动完成以分隔符作为码流结束标识的消息的解码。回车换行解码器实际上是一种特殊的DelimiterBasedFrameDecoder解码器。
3. FixedLengthFrameDecoder
固定长度解码器，它能够按照指定的长度对消息进行自动解码,当制定的长度过大，消息过短时会有资源浪费，但是使用起来简单。
4. LengthFieldBasedFrameDecoder
通用解码器，一般协议头中带有长度字段，通过使用LengthFieldBasedFrameDecoder传入特定的参数，来解决拆包粘包。

## 内存泄漏

- [Netty堆外内存泄漏排查，这一篇全讲清楚了 - 掘金](https://juejin.cn/post/6844904036672471048)
## 流量整形

* [Netty高级功能（一）：流控和流量整形](https://www.jianshu.com/p/6c4a7cbbe2b5): 客户端流控, 服务端流量整形
* [netty(九): 流量整形](https://blog.csdn.net/qq_35688140/article/details/104399955): running example
* [Netty 那些事儿 ——— Netty实现“流量整形”原理分析及实战](https://www.jianshu.com/p/bea1b4ea8402): 流量整形实现
* [netty 的流量整形深度探险](https://www.cnblogs.com/FlyAway2013/p/14824615.html): more流量整形实现

-->