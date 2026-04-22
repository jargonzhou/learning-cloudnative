# Patterns

books:
* Gamma, Erich / Helm, Richard / Johnson, Ralph / Vlissides, John. **Design Patterns: Elements of Reusable Object-Oriented Software**. 1997. Oxford University Press. - UML: workbench\UML\GoF
* POSA: Pattern-Oriented Software Architecture
  * Volume 1 A System of Pattern
  * Volume 2 Patterns for Concurrent and Networked Objects
  * Volume 3 Patterns for Resource Management
  * Volume 4 A Pattern Language for Distributed Computing
  * Volume 5 On Patterns and Pattern Languages

# SOLID

设计模式的六大原则(SOLID)

总原则: **开闭原则**(**O**pen Closed Principle): 一个软件实体, 如类、模块和函数应该对扩展开放, 对修改关闭. 

1. **单一职责原则**(**S**ingle Responsibility Principle): 一个类应该只有一个发生变化的原因. 
2. **里氏替换原则**(**L**iskov Substitution Principle): 所有引用基类的地方必须能透明地使用其子类的对象. 
3. **依赖倒置原则**(**D**ependence Inversion Principle): 上层模块不应该依赖底层模块, 它们都应该依赖于抽象. 抽象不应该依赖于细节, 细节应该依赖于抽象.
4. **接口隔离原则**(**I**nterface Segregation Principle): 客户端不应该依赖它不需要的接口. 类间的依赖关系应该建立在最小的接口上. 
5. **迪米特法则**(**最少知道原则**)(Law of Demeter): 只与你的直接朋友交谈, 不跟“陌生人”说话. 一个类对自己依赖的类知道的越少越好
6. **合成复用原则**(Composite Reuse Principle): 尽量使用对象组合/聚合, 而不是继承关系达到软件复用的目的. 

# GoF

*创建型*模式（Creational Pattern）：对类的实例化过程进行了抽象，能够将软件模块中对象的创建和对象的使用分离。

1. *工厂模式*（Factory Pattern）
    在工厂模式中，我们在创建对象时不会对客户端暴露创建逻辑，并且是通过使用一个共同的接口来指向新创建的对象。
2. *抽象工厂模式*（Abstract Factory Pattern）
    围绕一个超级工厂创建其他工厂
3. *单例模式*（Singleton Pattern）
    保证一个类仅有一个实例，并提供一个访问它的全局访问点。
4. *建造者模式*（Builder Pattern）
    使用多个简单的对象一步一步构建成一个复杂的对象; 将一个复杂的构建与其表示相分离.
5. *原型模式*（Prototype Pattern）
    实现了一个原型接口，该接口用于创建当前对象的克隆

*结构型*模式（Structural Pattern）：关注于对象的组成以及对象之间的依赖关系，描述如何将类或者对象结合在一起形成更大的结构，就像搭积木，可以通过简单积木的组合形成复杂的、功能更为强大的结构。
    
1. *适配器模式*（Adapter Pattern）
    作为两个不兼容的接口之间的桥梁
2. *装饰器模式*（Decorator Pattern）
    创建了一个装饰类，用来包装原有的类，并在保持类方法签名完整性的前提下，提供了额外的功能
3. *代理模式*（Proxy Pattern）
    创建具有现有对象的对象，以便向外界提供功能接口
4. *外观模式*（Facade Pattern）
    隐藏系统的复杂性，并向客户端提供了一个客户端可以访问系统的接口
5. *桥接模式*（Bridge Pattern）
    提供抽象化和实现化之间的桥接结构，来实现二者的解耦
6. *组合模式*（Composite Pattern）
    依据树形结构来组合对象，用来表示部分以及整体层次
7. *享元模式*（Flyweight Pattern）
    支持大量细粒度的对象

*行为型*模式（Behavioral Pattern）：关注于对象的行为问题，是对在不同的对象之间划分责任和算法的抽象化；不仅仅关注类和对象的结构，而且重点关注它们之间的相互作用。

1. *策略模式*（Strategy Pattern）
    一个类的行为或其算法可以在运行时更改
2. *模板模式*（Template Pattern）
    定义一个操作中的算法的骨架，而将一些步骤延迟到子类中
3. *观察者模式*（ObserverPattern）
    定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新
4. *迭代器模式*（Iterator Pattern）
    用于顺序访问集合对象的元素，不需要知道集合对象的底层表示
5. *责任链模式*（Chain of Responsibility Pattern）
    为请求创建了一个接收者对象的链
6. *命令模式*（Command Pattern）
    将一个请求封装成一个对象，从而使您可以用不同的请求对客户进行参数化
7. *备忘录模式*（Memento Pattern）
    在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态
8. *状态模式*（State Pattern）
    类的行为是基于它的状态改变的
9. *访问者模式*（Visitor Pattern）
    将数据结构与数据操作分离
10. *中介者模式*（Mediator Pattern）
    降低多个对象和类之间的通信复杂性
11. *解释器模式*（Interpreter Pattern）
    给定一个语言，定义它的文法表示，并定义一个解释器，这个解释器使用该标识来解释语言中的句子

# Reactor
* [wikipedia](https://en.wikipedia.org/wiki/Reactor_pattern)
* Schmidt, Douglas C. **Reactor: An Object Behavioral Pattern forDemultiplexing and Dispatching Handles for Synchronous Events**. 1995.

> Reactor设计模式处理由一个或多个客户端并发生成的服务请求. 
> 
> 每个应用中的服务由几个方法组成, 用单独的事件处理器表示服务, 事件处理器负责分发(dispatch)服务相关的请求. 
>
> 一个初始的分发器负责分发事件处理器, 并管理事件处理的注册.
>
> 服务请求的分离由一个同步的事件分离器(demultiplexer)执行.

> Reactor/Dispatcher/Notifier

terms:
* reactor: 反应器
* multiplexer: 多路复用器
* demultiplexer: 信号分离器
* dispatcher: 分发器
* notifier: 通知器

处理问题: C10K problem, 并发事件处理问题
* 网络socket
* 硬件IO
* 文件系统/数据库访问
* IPC(Inter Process Communication)
* message passing systems

组件结构:
* 句柄: 一个特定IO/数据请求的标识符和接口. 通常是socket, 文件描述符等.
* 信号分离器(Demultiplexer): 一个可以有效的监控句柄状态的事件通知器, 然后通知其他子系统一个相关状态变更. 通常是`select()`系统调用, 也可以是epoll, kqueue, IOCP.
* 分发器: 响应式应用的实际事件循环(event loop), 维护事件处理器的注册, 在事件发生时调用相应的处理器.
* 事件处理器: 请求处理器, 是处理一类服务请求的特定逻辑. 建议动态注册为回调. 不使用多线程的reactor默认在与分发器相同的线程中调用请求处理器.
* 事件处理器接口: 一个表示通用的事件处理器的属性和方法的抽象接口.

<img src="https://upload.wikimedia.org/wikipedia/commons/2/2a/Reactor_Pattern_-_UML_2_Component_Diagram.svg" width="800"/>

初始化时序:
* 初始化分发器/事件循环.
* 注册事件处理器.
* 打开句柄: 关联分发器和信号分离器.
* 开始事件循环: 等待事件发生.

运行时序:
* (客户端)通过句柄传递请求.
* 信号分离器检测到句柄状态变化, 通知分发器有事件发生.
* 分发器调用事件处理器: 携带句柄参数.
* 事件处理器读写句柄, 处理请求.
* 分发器开始下一次事件循环.


application: Netty
* Handle: Channel
  * 打开句柄: 注册到Selector(NIO), 注册到EventLoop.
* Dispatcher: NioEventLoop, EpollEventLoop
  * 绑定到固定的一个线程.
* Demultiplexer: Selector, `io.netty.channel.epoll.Native#newEpollCreate`
* Event Handler: ChannelHandler

<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/ReactorPattern_-_UML_2_Sequence_Diagram.svg/771px-ReactorPattern_-_UML_2_Sequence_Diagram.svg.png" width="800"/>

* Doug Lea. **Scalable IO in Java**. [link](http://gee.cs.oswego.edu/dl/cpjslides/nio.pdf), [link](https://cdn.jsdelivr.net/gh/fuxyzz/cdn/files/nio.pdf)

网络服务的基本结构
- 读取请求, 解码请求, 处理请求的服务, 编码响应, 发送响应

方案: 
- 每个处理器有自己的线程
- 事件驱动
  - Reactor单线程版本: reactor/dispatch和acceptor共用一个线程.
- 多线程
  - 工作者线程: 将非IO处理卸载到其他线程中执行
    - IO处理: acceptor, 读取请求, 发送响应. 非IO处理: 解码请求, 处理请求的服务, 编码响应.
    - 线程池: 工作者线程, 任务队列.
  - 多个Reactor线程:  
    - mainReactor: 处理acceptor, 成功建立连接后注册到subReactor. - 处理接入认证
    - subReactor: 处理读取请求, 发送响应, 将非IO处理卸载到工作者线程中处理.

`java.nio`:
- Bufffer, ByteBuffer
- Channel, SelectableChannel, SocketChannel, ServerSocketChannel, FileChannel
- Selector, SelectionKey

# Proactor
* [wikipedia](https://en.wikipedia.org/wiki/Proactor_pattern)
* Pyarali, Irfan / Tim Harrison / Schmidt, Douglas C. / Jordan, Thomas D. **Proactor: An Object Behavioral Pattern for Demultiplexing**. 1997.

> 本文中的前摄器模式描述了如何组织应用和系统以高效的利用操作系统支持的异步机制.
> 
> 当应用调用一个异步操作时, 操作系统代为执行操作. 这允许应用有多个同时运行的操作, 而不需要有对应数量的线程.
> 
> 因此, 前摄器模式简化了并发编程, 通过较少的线程和利用操作系统异步操作支持, 提供性能.

terms:
* proactor: 前摄器
* completion handler: 完成处理器

前摄器模式是同步的reactor模式的异步版本变种.

组件结构:
* Asynchronous Operation: 异步操作. 由操作系统代为执行, 从应用视角该操作是异步的. 
  * 例: Async_Read, Async_Write, Async_Accpet
* Proactive Initiator: 前摄初始化器, 是应用发起异步操作的实体, 向异步操作处理器中注册完成处理器和完成分发器.
  * 例: Web服务器应用的主线程
* Asynchronous Operation Processor: 异步操作处理器, 通常由操作系统实现.
* Completion Dispatcher: 异步操作完成事件分发器. 异步操作完成时, 异步操作处理器将应用通知委托给分发器.
  * 例: 通知队列
* Completion Handler: 异步操作完成事件处理器.
  * 例: Acceptor, HTTP Handler

运行时序:
* 应用使用前摄初始化器发起异步操作, 创建/指定相应的句柄, 并向异步操作处理器注册完成处理器和完成分发器.
* 异步操作处理器执行异步操作.
* 异步操作完成时, 异步操作处理器调用对应的完成分发器, 传递异步操作的结果和对应的完成处理器.
* 完成分发器调用完成处理器, 传递异步操作的结果.

<img src="https://upload.wikimedia.org/wikipedia/commons/6/61/Proactor.VSD_SequenceDiagram.png" width="800"/>


