# wire

TODO:

- gRPC

# Netty

unit test: `EmbeddedChannelTest`

examples:

- `DiscardServer`
- `EchoServer`
- `TimeServer`, `TimeClient`: POJO
- codes: `AbsIntegerEncoder`, `FixedLengthFrameDecoder`, `FrameChunkDecoder`

protocols:

- websocket: `ChatServer`
  - SSL: `org.bouncycastle:bcpkix-jdk18on` - `ChatServerInitializer.Secure`
  - update ChannelPipeline: remove ChannelHandler - `TextWebSocketFrameHandler`
- udp: `LogEventBroadcaster`, `LogEventMonitor`

# DNS

- `DNSJavaTest`