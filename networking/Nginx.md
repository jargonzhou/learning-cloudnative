# Nginx
* https://nginx.org/
* https://nginx.org/en/books.html

## Architecture
* https://blog.nginx.org/blog/inside-nginx-how-we-designed-for-performance-scale 2015
* https://aosabook.org/en/v2/nginx.html


解决的问题: 使用复杂的事件驱动架构处理大量的并发连接.

进程模型: 
- master进程: 处理特权操作(读取配置, 绑定端口, 创建子进程), 处理外界信号
- worker进程: 处理网络连接, 读写磁盘内容, 与上游upstream服务器通信.
  - 每个CPU核运行一个worker进程.
  - 每个worker进程是单线程的, worker进程之间使用共享内存通信: 共享缓存数据, 会话持久数据等.
- helper进程
  - cache manager process: 周期性运行, 保持缓存符合配置的大小.
  - cache loader process: 启动时加载磁盘中缓存到内存, 然后退出.

请求处理流程:
- master进程建立需要的socket监听listenfd, fork多个worker进程.
- worker进程在注册listenfd读事件前抢accept_mutex, 抢到的进程注册listenfd读事件, 在读事件处理中调用accept接受连接.
- worker进程读取请求, 解析请求, 处理请求, 产生响应返回给客户端, 最后断开连接.


事件处理流程: 异步非阻塞方式处理请求
- 事件类型: 网络事件, 信号, 定时器

网络事件
- 系统调用: select/poll/epoll/kqueue
- 一个线程监控多个事件, 查询是否有事件发生是阻塞的但可以设置超时时间, 在超时时间内如果有事件准备好就返回.
- 事件发生代表未处理的请求, 单个线程需要在不同的请求间切换, 这种切换没有代价(上下文切换).

信号: 中断程序的执行, 改变状态后继续执行. 需要系统调用是可重入的.
- `SIGHUP`: `nginx -s reload`. master进程fork新的worker进程, 立即处理连接和请求, 给旧worker进程发信号通知其退出.

定时器: epoll_wait等的超时时间参数.


## Nginx Ingress Controller
* https://docs.nginx.com/nginx-ingress-controller/
* https://github.com/kubernetes/ingress-nginx

> It is built around the Kubernetes **Ingress** resource, using a **ConfigMap** to store the controller configuration.