# BPF
* [eBPF - wikipedia](https://en.wikipedia.org/wiki/EBPF)

* bpftrace Programming - BPF Performance Tools Chapter 5
* Linux Observability with BPF
  * Chapter 5. BPF Utilities
    * bpftool: `/home/zhoujiagen/WSL2-Linux-Kernel/tools/bpf/bpftool`
    * bpftrace: https://github.com/bpftrace/bpftrace
    * kubectl-trace: https://github.com/iovisor/kubectl-trace/
    * ebpf_exporter: https://github.com/cloudflare/ebpf_exporter
  * Chapter 6. Linux Networking and BPF - Networking
  * Chapter 7. Express Data Path - Networking
  * Chapter 8. Linux Kernel Security, Capabilities, and Seccomp - Security

# 术语
* benchmark: 基准. 通过执行工作负载实验修改系统的状态.
* instrumentation: 插桩.
  * dynamic instrumentation: 可能存在被插桩的函数重命名问题. 
    * kprobes, uprobes
  * static instrumentation: 有稳定的事件名称.
    * tracepoints, USDT(user-level statically defined tracing)
* observability: 可观测性. 通过观察理解系统, 包括跟踪工具, 采样工具, 基于固定计数器的工具, 但不包括基准工具.
* profiling: 性能分析/剖析/轮廓.
* sampling: 采样. 取目标度量的子集输出目标的粗粒度概貌, 又称创建轮廓(profiling).
* snooping: 窥探.
* tracing: 跟踪. 基于事件的记录, 是BPF工具使用的插桩类型.
  * `strace(1)`
  * `tcpdump(8)` 
  * `execsnoop` ...


Technology terms:
* Stack Trace Walking
* Flame Graphs
* Event Sources
* kprobes: Kprobes enables you to dynamically break into any kernel routine and collect debugging and performance information non-disruptively. You can trap at almost any kernel code address, specifying a handler routine to be invoked when the breakpoint is hit.<br/>Kernel Probes (Kprobes): https://www.kernel.org/doc/html/latest/trace/kprobes.html
* uprobes
* Tracepoints: A tracepoint placed in code provides a hook to call a function (probe) that you can provide at runtime. A tracepoint can be “on” (a probe is connected to it) or “off” (no probe is attached).<br/>Using the Linux Kernel Tracepoints: https://www.kernel.org/doc/html/latest/trace/tracepoints.html
* USDT(User-level Statically Defined Tracing)
* Dynamic USDT
* PMC(Performance Monitoring Counters)
* perf_events
* Event tracing: Tracepoints can be used without creating custom kernel modules to register probe functions using the event tracing infrastructure.<br/>Event Tracing: https://www.kernel.org/doc/html/latest/trace/events.html


## Stack Trace Walking

- frame pointer-based stack walk

栈帧链表的头部总是可以在寄存器中(x86_64 RBP)找到, 返回地址在固定偏移量处.

- debuginfo

调试信息文件: 包含ELF调试信息文件(DWARF格式).

- LBR: last branch record

Intel处理器的特性: 在硬件缓冲区中记录包括函数调用分支在内的分支.

- ORC-based statck walk: Oops Rewind Capability

一种新的调试信息格式.


# Architecture
* [BPF Architecture](https://docs.cilium.io/en/latest/reference-guides/bpf/architecture/): in cilium

Figure 2-1 BPF tracing technologies - BPF Performance Tools
* instruction set
* maps: key/value stores
* helper functions: to interact with and leverage kernel functionality
* tail calls: for calling into other BPF programs
* security hardening primitives
* a pseudo file system: pin objects(map, program)
* infrastructure for allowing BPF to be offloaded (ex. to a network card)

clang编译C到BPF目标文件, 加载入内核.

使用BPF的内核子系统:
* tc: 在网络栈的后续阶段执行, 可以访问更多的元数据和内核核心功能.
* XDP: 在最早的网络驱动器阶段附加, 接收到每个报文时触发BPF程序的运行.
* tracing: kprobes, uprobes, tracepoints.

## Instructions

- Classic BPF: [filter.h](https://github.com/torvalds/linux/blob/master/include/uapi/linux/filter.h) and [bpf_common.h](https://github.com/torvalds/linux/blob/master/include/uapi/linux/bpf_common.h)
- Extended BPF: [bpf.h](https://github.com/torvalds/linux/blob/master/include/uapi/linux/bpf.h) and [bpf_common.h](https://github.com/torvalds/linux/blob/master/include/uapi/linux/bpf_common.h)
## bpf system call
## /sys/fs/bpf

# 工具
## core
* BPF in Linux kernel: 内核中BPF运行时, 包括指令集, 存储对象和辅助函数, 以及解释器, JIT编译器, 验证器(verifier).
* BCC: 提供了C语言的内核插桩, Python和lua前端.
* bpftrace: 用于Linux的高级跟踪语言. 使用LLVM作为后端将脚本编译为eBPF字节码, 使用libbpf和bcc与Linux BPF子系统和已有的跟踪能力交互.
  * Linux已有的跟踪能力: kprobes, uprobes, tracepoints等.
* [ply](https://github.com/iovisor/ply): 轻量级的Linux动态跟踪器, 有较少的外部依赖.
* [libbcc](https://github.com/iovisor/bcc/blob/master/src/cc/libbcc.pc.in): BCC Program library
* [libbpf](https://github.com/libbpf/libbpf): libbpf is a C-based library containing a BPF loader that takes compiled BPF object files and prepares and loads them into the Linux kernel. libbpf takes the heavy lifting of loading, verifying, and attaching BPF programs to various kernel hooks, allowing BPF application developers to focus only on BPF program correctness and performance.
## related

* LLVM
* kprobes
* uprobes
* tracepoints
* perf(1): Linux性能工具集.
* Ftrace: Ftrace is an internal tracer designed to help out developers and designers of systems to find what is going on inside the kernel. It can be used for debugging or analyzing latencies and performance issues that take place outside of user-space.<br/>ftrace - Function Tracer: https://www.kernel.org/doc/html/latest/trace/ftrace.html
* Dynamic instrumentation: DTrace, SystemTap, BCC, bpftrace, ...
* LTT: first Linux tracer in 1999
* Dprobes: 2000, lead to kprobes
* DTrace: 一个包括了编程语言和工具的观测框架. 通过称为探针的指令点, 可以观察所有用户级和内核级的代码. http://dtrace.org/blogs/about/ https://github.com/dtrace4linux
* SystemTap: 对用户级和内核级的代码提供静态和动态跟踪能力: 静态探针使用tracepoint, 动态探针使用kprobes, 用户级别的探针使用uprobes. https://sourceware.org/systemtap/
* ktap: for VM-based tracers
## misc

* strace: 基于Linux系统的系统调用跟踪.
  - [strace: linux syscall tracer](https://strace.io/)
  - [strace(1) — Linux manual page](https://man7.org/linux/man-pages/man1/strace.1.html)
* oprofile: Linux系统剖析
* `/proc`: 提供内核统计信息的文件系统接口.
* `/sys`: sysfs文件系统, 为内核统计提供一个基于目录的结构.
* blktrace: 块I/O跟踪.
* tcpdump: 网络包跟踪, 使用了libpcap库. http://man7.org/linux/man-pages/man1/tcpdump.1.html
* pmap: 进程的内存段和使用统计.
* [KUtrace](https://github.com/dicksites/KUtrace): KUtrace is an extremely low-overhead Linux kernel tracing facility for observing all the execution time on all cores of a multi-core processor, nothing missing, while running completely unmodified user programs written in any computer language. It has been used in live datacenters (x86 processors) and in real-time autonomous driving (ARM processors) to understand long-standing performance mysteries. The design goal of KUtrace is to reveal the root cause(s) of unexpected delayed responses in real-time transactions or database processing while having such low overhead that it does not distort the system under test.


* ping: send ICMP ECHO_REQUEST to network hosts http://man7.org/linux/man-pages/man8/ping.8.html
* nicstat: print network traffic statistics
* dstat: versatile tool for generating system resource statistics Dstat is a versatile replacement for **vmstat**, **iostat** and **ifstat**.
* ifstat: eport InterFace STATistics Ifstat is a little tool to report interface activity, just like iostat/vmstat do for other system statistics.
* netstat: 网络接口的统计, TCP/IP栈的统计. Print network connections, routing tables, interface statistics, masquerade connections, and multicast memberships [http://man7.org/linux/man-pages/man8/netstat.8.html](http://man7.org/linux/man-pages/man8/netstat.8.html)
* pidstat: Report statistics for Linux tasks. http://man7.org/linux/man-pages/man1/pidstat.1.html
* btrace, blktrace: perform live tracing for block devices http://man7.org/linux/man-pages/man8/btrace.8.html generate traces of the i/o traffic on block devices http://man7.org/linux/man-pages/man8/blktrace.8.html
* iotop: simple top-like I/O monitor http://man7.org/linux/man-pages/man8/iotop.8.html
* slabtop: display kernel slab cache information in real time http://man7.org/linux/man-pages/man1/slabtop.1.html
## BPF工具
![](https://www.brendangregg.com/BPF/bpf_performance_tools.png)