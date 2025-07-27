# OS Performance
* Gregg, Brendan. Systems Performance: Enterprise and the Cloud. 2nd Edition. 2021. Pearson.
* Gregg, Brendan. BPF Performance Tools. 2020. Pearson.


Android:
* [Perfetto](https://github.com/google/perfetto): Perfetto is a production-grade open-source stack for performance instrumentation and trace analysis. It offers services and libraries and for recording system-level and app-level traces, native + java heap profiling, a library for analyzing traces using SQL and a web-based UI to visualize and explore multi-GB traces.

# 方法论
循环诊断

```
 --------------------
 |                  ^
 v                  |
假设 -> 仪器检验 -> 数据
```
USE方法(Utilization, Saturation, Errors)

对所有的资源, 查看它的使用率(utilization)、饱和度(saturation)和错误(errors):
- 使用率: 在给定的时间间隔内, 资源用于服务工作的时间百分比.
- 饱和度: 资源不能再服务更多的额外工作的程度, 通常有等待队列.
- 错误: 错误事件的个数.
工作负载特征归纳

通过回答问题:
- 负载是由谁产生的: 进程ID、用户ID、远端的IP地址?
- 负载为什么会被调用: 代码路径、堆栈跟踪?
- 负载的特征是什么: IOPS、吞吐量、方向类型(读取/写入)、包含变动(标准方法)?
- 负载是怎样随时间变化的, 有日常模式吗?
延时分析

检查完成一项操作所用的时间, 然后把时间再分成小的时间段, 再对有最大延时的时间段做再次的划分, 最后定位并量化问题的根本原因.

例: MySQL的请求延时分析

1. 存在请求延时问题吗? - 是的
2. 请求时间大量的花在CPU上吗? - 不在CPU上
3. 不花在CPU上的时间在等待什么? - 文件系统I/O
4. 文件系统的I/O时间是花在磁盘I/O还是锁竞争上? - 磁盘I/O
5. 磁盘I/O时间主要是随机寻址的时间还是数据传输的时间? - 数据传输时间
# 建模
扩展定律

Amdahl扩展定律: 早期的扩展特性是竞争, 主要是对串行的资源或工作负载的竞争

$$
C(N) = \frac{N}{1} + \alpha (N - 1), 0 \le \alpha \le 1
$$

- $C(N)$: 容量;
- $N$: 扩展的维度, 如CPU数量或用户负载;
- $\alpha$: 系数, 表示串行的程序/偏离线性扩展的程度.

通用扩展定律(Universal Scalability Law):

$$
C(N) = \frac{N}{1} + \alpha (N - 1) + \beta N (N - 1)
$$

- $C(N)$、$N$、$\alpha$: 与Amdahl扩展定律一致;
- $\beta$: 处理延时的一致性系数, 为$0$时该定律变成Amdahl扩展定律.
排队理论(Queuing Theory)

用数学方法研究带有队列的系统, 提供了对队列长度、等待时间/延时、基于时间的使用率的分析方法.

Little's定律: 系统请求的平均数量$L$由平均到达率$\lambda$乘以平均服务时间$W$得到

$$
L = \lambda W
$$

排队系统的要素:

- 到达过程$\texttt{A}$: 描述请求到达排队系统的间隔时间, 这个时间间隔可以是随机的、固定的, 或是一个过程, 如泊松分布;
- 服务时间分布$\texttt{S}$: 描述服务中心的服务时间, 可以是确定性分布、指数型分布等;
- 服务中心数量$\texttt{c}$.

Kendall标记法: $\texttt{A/S/c}$, 例:

- $\texttt{M/M/1}$: 马尔科夫指数分布到达, 马尔科夫指数分布服务时间, 一个服务中心;
- $\texttt{M/M/c}$: 与$\texttt{M/M/1}$一样, 但有多个服务中心;
- $\texttt{M/G/1}$: 马尔科夫到达, 一般分布的服务时间, 一个服务中心;
- $\texttt{M/D/1}$: 马尔科夫到达, 固定时间的服务时间, 一个服务中心.
# 维度和分析目标
## 应用性能分析

- 有哪些应用请求, 请求的延迟
- 处理应用请求的时间消耗在哪里
- 为什么应用on-CPU
- 为什么应用阻塞、切换CPU
- 应用执行了哪些I/O操作, 代码路径是什么
- 应用阻塞在哪些锁上, 阻塞多长时间
- 应用使用了哪些内核资源, 为什么使用

## CPU性能分析

- 各进程的CPU利用率
- 上下文切换频率
- 运行队列长度
- 创建了哪些新进程, 它们的存活时间
- 为什么系统时间比较长, 是哪些系统调用造成的, 它们在做什么事情
- 每次唤醒后线程on-CPU的时间
- 线程在运行队列中等待的时间
- 运行队列的最大长度
- 运行队列在CPU间是否是平衡的
- 为什么线程自愿离开CPU, 离开多长时间
- 哪些软/硬IRQ在消耗CPU
- 当其它CPU的运行队列中有工作时, 是否有空闲的CPU
- 应用请求的LLC(last-level cache)命中率

## 磁盘性能分析

- 各进程的IOPS、平均延迟、队列长度
- 有哪些I/O请求, I/O的大小
- 请求时间、队列中时间
- 有哪些比较大的延迟
- 延迟分布是否是多模态的
- 是否有磁盘错误
- 发送了哪些SCSI指令
- 是否有超时

## 文件系统性能分析



- 有哪些类型的文件系统请求, 各类型有多少次请求
- 读操作的字节数
- 异步写操作的数量
- 文件负载的访问模式: 随机的还是顺序的
- 访问了哪些文件, 被哪些进程或代码路径访问, 访问的字节数和访问次数
- 发生了哪些文件系统错误, 各类型有多少次, 是由哪些进程产生的
- 文件系统延迟的原因: 是磁盘, 代码路径还是锁
- 文件系统延迟的分布
- 数据缓存、指令缓存的命中率、未命中率
- 读操作的页缓存命中率
- 预取或预读是否有效, 是否需要调整

## 操作系统内核性能分析



- 为什么线程离开CPU, 离开了多长时间
- off-CPU的线程在等待什么事件
- 谁在使用内核SLAB分配器
- NUMA架构下, 内核是否在移动页
- 有哪些工作队列事件, 延迟是多少
- 对内核开发者: 哪些函数被调用, 实际参数和返回值是什么, 延迟是多少

## 编程语言性能分析



- 调用了哪些函数
- 函数使用的实际参数
- 函数的返回值, 是否是错误
- 产生事件的代码路径(栈跟踪)
- 函数消耗的时间

## 内存性能分析



- 物理内存和虚拟内存的使用情况
- 页操作的频率
- 为什么进程的物理内存(RSS)在持续增长
- 哪些代码路径导致页错误, 是哪个文件
- 哪些进程在阻塞等待换入
- 系统创建了哪些内存映射
- 发生OOM杀死进程时的系统状态
- 哪些应用代码路径在分配内存
- 应用分配的对象的类型
- 是否有内存分配一段时间后没有释放(指示出内存泄漏)

## 网络性能分析



- 发生了哪些socket I/O, 为什么会发生, 用户层栈是什么
- 哪些进程创建了新的TCP会话
- 是否发生socket、TCP或IP层错误
- TCP窗口大小, 是否有零大小传输
- 不同栈层的I/O大小
- 网络栈是否丢弃了报文, 为什么丢弃
- TCP连接中的延迟: 第一个字节的延迟, 整个存活期间的延迟
- 内核的网络栈内部的延迟
- 报文在qdisc队列中、网络驱动器队列中的时间
- 使用了哪些高层协议

## 安全分析



- 有哪些进程在执行
- 哪些进程建立了哪些网络连接
- 进程请求了哪些系统权限
- 系统中是否出现了权限拒绝错误
- 内核/用户函数是否用特定的参数执行

## 虚拟化性能分析



容器:

- 每个容器的运行队列延迟
- 调度器是否在同一个CPU上切换容器
- 是否遇到CPU或磁盘的软限制

Hypervisor:

- 虚拟化硬件资源的性能如何
- 如果使用了半虚拟化(paravirtualization), hypercall的延迟是多少
- 被偷的CPU时间的频率和持续时间
- hypervisor中断回调是否影响应用

# Linux 60-Second Analysis
## uptime: 查看平均负载
!uptime
## dmesg: 查看最后几条系统消息
!dmesg | tail
## vmstat: 虚拟内存统计信息
# 每秒输出虚拟内存统计信息. Report virtual memory statistics
!vmstat 1
## mpstat: 查看每个CPU的统计时间
!mpstat -P ALL 1 1
## pidstat: 查看每个进程的CPU使用情况
!pidstat 1 1
## iostat: 查看存储设备I/O情况
!iostat -xz 1 1
## free: 查看可用内存
!free -m
## sar: 查看网络设备情况
!sar -n DEV 1 1
# 查看TCP情况.
!sar -n TCP,ETCP 1 1
## top: 查看系统和进程汇总信息
!top -n 1
# sysstat: system performance tools for Linux
%env ROOT_PWD=xxx
!echo $ROOT_PWD | sudo -S apt install sysstat
!apt info sysstat
# Linux Tracing Technologies
* https://docs.kernel.org/trace/index.html
## Probes
* [which kprobe hooks can I attach eBPF programs to?](https://stackoverflow.com/questions/67766320/which-kprobe-hooks-can-i-attach-ebpf-programs-to)
!cat /proc/kallsyms
- kprobe, kretprobe
  - `/sys/kernel/debug/kprobes/`: `blacklist`, `enabled`, `list`

- profile, interval

跨所有CPU的基于时间的采样, (单个CPU)的基于时间的报告:

```
profile:hz:rate     // Hertz(events per second)
profile:s:rate      // seconds
profile:ms:rate     // milliseconds
profile:us:rate     // microseconds
interval:s:rate
interval:ms:rate
```

- software, hardware

- tracepoint
  - [Notes on Analysing Behaviour Using Events and Tracepoints](https://www.kernel.org/doc/html/latest/trace/tracepoint-analysis.html): Linux Kernel doc
  - [Using the Linux Kernel Tracepoints, by Mathieu Desnoyers](https://www.kernel.org/doc/Documentation/trace/tracepoints.txt)
  - `/sys/kernel/debug/tracing/events/`

```
# 查看所有可能的事件
$ find /sys/kernel/debug/tracing/events -type d

# 使用PCL(Performance Counters for Linux)
$ perf list 2>&1 | grep Tracepoint
```

- uprobe, uretprobe

用户层动态函数(返回)探查:

```
uprobe:binary_path:function_name
uprobe:library_path:function_name
uretprobe:binary_path:function_name
uretprobe:library_path:function_name

// 参数arg0, arg1, ..., argN
// 返回值retval
```

- usdt


用户级静态定义的跟踪:

```
usdt:binary_path:probe_name
usdt:libraty_path:probe_name
usdt:binary_path:probe_namespace:probe_name
usdt:library_path:probe_namespace:probe_name

bpftrace -l 'usdt:/usr/bin/python3'
```
%env ROOT_PWD=xxx
!echo $ROOT_PWD | sudo -S find /sys/kernel/debug/tracing/events -type d | grep syscalls
# Flame Graph
- [The Flame Graph](https://queue.acm.org/detail.cfm?id=2927301): an ACMQ article.
- [Flame Graphs visualize profiled code](https://github.com/brendangregg/FlameGraph): on Github.
## Flame Graph Explained and Interpretation

火焰图有如下特征:
- 调用栈用一列矩形框表示, 每个矩形框表示一个函数(栈帧).
- y轴展示栈深度, 根栈帧在底部, 叶栈帧在顶部. 顶部的矩形框表示收集调用栈时on-CPU的函数, 在其下的矩形框表示它的祖先.
- x轴横跨调用栈集合, 不是按时间先后展示的, 从左到右的顺序没有特殊的含义. 从左到右将函数名称按字典序排列. 当水平相邻的函数是相同的时合并矩形框.
- 每个函数矩形框的宽度展示它在调用栈中或在其祖先函数中出现的频次 .
- 如果矩形框的宽度足够大, 会展示函数的名称.
- 每个矩形框的背景色不是重要的, 按暖色调随机选取.
- 可能跨越单个线程、多个线程、多个应用或多个主机.
- 调用栈可以从多个探查目标中收集, 其宽度可以表示度量值而不是抽样数量. 例如, 一个探查器可以度量线程被阻塞的时间和它的调用栈. 这可以可视化为火焰图, x轴横跨整个被阻塞时间, 火焰图展示了阻塞代码路径.


按如下方式解读火焰图:
- 顶层的边展示了收集调用栈时在CPU上运行的函数.
- 沿着顶层边查找大的高原, 它展示了在这次探查中运行频率较高的调用栈.
- 从上往下读函数的祖先.
- 函数的矩形框的宽度可以直接用来比较: 宽的表示在探查中出现频率较高, 值得首先考察.
- 按时间采样调用栈探查CPU时, 如果一个函数的矩形框较宽, 可能是因为它每个函数调用消耗更多CPU或者这个函数被更频繁的调用.
- 火焰图中的分支, 表现为单个函数上两个或多个大的塔, 对分析很有用. 它们可以指出隶属于一个逻辑组的代码, 也可能是由条件语句产生的.

解读示例:

[Interpretation Example](https://dl.acm.org/cms/attachment/da59940d-0241-44f3-a32e-1e8c6fbbf0c4/gregg6.png)

```
     +----+----+----+
     |      g()     |
+----+----+----+----+
| e()|      f()     |
+----+----+----+----+----+----+
|           d()               |
+----+----+----+----+----+----+----+----+
|           c()               |   i()   |
+----+----+----+----+----+----+----+----+
|           b()               |   h()   |
+----+----+----+----+----+----+----+----+
|                 a()                   |
+----+----+----+----+----+----+----+----+
```

- 顶层边表示`g()`最常on-CPU.
- `d()`较宽, 但它暴露出的顶层边on-CPU最少.
- `b()`和`c()`在采样中没有直接on-CPU.
- `g()`之下的函数表示它的祖先: `g()`被`f()`调用, `f()`被`d()`调用, 等等.
- 视觉上`b()`与`h()`的宽度表示on-CPU的`b()`代码路径几乎是`h()`代码路径的四倍. 实际on-CPU的函数是它们调用的函数.
- 代码中主要的分支在`a()`调用`b()`和`h()`时.

## MySQL

- [MySQL Profiler Output as a Flame Graph](https://queue.acm.org/downloads/2016/Gregg4.svg)

 1 Capture stack with Linux perf_events: PID 181, 60 second, 99 Hertz
```shell
perf record -F 99 -p 181 -a -g -- sleep 60
perf script > out.perf
``` 
 2 fold stack for Linux perf_event "perf script" output
```shell
./stackcollapse-perf.pl ../mysql-out.perf > ../mysql-out.folded
```
 3 render a SVG
```shell
./flamegraph.pl ../mysql-out.folded > ../mysql.svg
```

## Redis
```shell
sudo apt-get install redis
systemctl status redis

ps -ef | grep redis | grep -v grep
redis       5619       1  0 15:33 ?        00:00:00 /usr/bin/redis-server 127.0.0.1:6379

ps -ef | grep redis | grep -v grep | awk '{print $2}'
```

```shell
redis-benchmark
```

```shell
# `ps -ef | grep redis | grep -v grep | awk '{print $2}'`
sudo /usr/lib/linux-tools/5.4.0-190-generic/perf record -F 99 -p 5619 -a -g -- sleep 60
sudo /usr/lib/linux-tools/5.4.0-190-generic/perf script > redis.perf
```

```
git clone https://github.com/brendangregg/FlameGraph.git
cd FlameGraph

./stackcollapse-perf.pl ../redis.perf > ../redis.folded

./flamegraph.pl ../redis.folded > ../redis.svg
```

cleanup

```shell
sudo systemctl stop redis
sudo apt remove redis
```