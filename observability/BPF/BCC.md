# BCC
* https://github.com/iovisor/bcc
* [bcc Reference Guide](https://github.com/iovisor/bcc/blob/master/docs/reference_guide.md): 参考指南.
* [bcc Python Developer Tutorial](https://github.com/iovisor/bcc/blob/master/docs/tutorial_bcc_python_developer.md): Python开发教程.

> BPF Compiler Collection (BCC)
>
> BCC is a toolkit for creating efficient kernel tracing and manipulation programs, and includes several useful tools and examples. It makes use of extended BPF (Berkeley Packet Filters), formally known as eBPF, a new feature that was first added to Linux 3.15. Much of what BCC uses requires Linux 4.1 and above.
>
> BCC makes BPF programs easier to write, with kernel instrumentation in C (and includes a C wrapper around LLVM), and front-ends in Python and lua. It is suited for many tasks, including performance analysis and network traffic control.

## Features

Kernel Space:

- dynamic instrumentation, kernel-level: kprobes
- dynamic instrumentation, user-level: uprobes
- static tracing, kernel-level: tracepoints
- timed sampling events: BPF with `perf_event_open()`
- PMC events: BPF with `perf_event_open()`
- filtering: via BPF programs
- debug output: `bpf_trace_printk()`
- per-event output: `bpf_perf_event_output()`
- basic variables: global and per-thread variables, via BPF maps
- associative arrays: via BPF maps
- frequency counting: via BPF maps
- histograms: power-of-two, linear, custom, via BPF maps
- timestamps and time deltas: `bpf_ktime_get_ns()` and BPF programs
- stack trace, kernel: BPF stackmap
- stack trace, user: BPF stackmap
- overwrite ring buffers: `perf_event_attr.write_backward`
- low-overhead instrumentation: BP JIT, BPF map summarizes
- production safe: BPF verifier

User Space:

- static tracing, user-level: SystemTap-stype USDT probes, via uprobes
- debug output: Python with `BPF.trace_pipe()` and `BPF_.trace_fields()`
- per-event output: `BPF_PERF_OUTPUT` macro and `BPF.open_perf_buffer()`
- interval output: `BPF.get_table()` and `table.clear()`
- histogram printing: `table.print_log2_hist()`
- C struct navigation, kernel-level: BCC rewriter maps to `bpf_probe_read()`
- symbol resolution, kernel-level: `ksym()`, `ksymaddr()`
- symbol resolution, user-level: `usymaddr()`
- debuginfo symbol resolution support
- BPF tracepoint support: via `TRACEPOINT_PROBE`
- BPF stack trace support: `BPF_STACK_TRACE`
- various other helper macros and functions
- examples: under `/examples`
- tools: under `/tools`
- tutorials: under `/docs/tutorial*.md`
- reference guide: under `/docs/reference_guide.md`


## Installation

WSL: https://github.com/iovisor/bcc/blob/master/INSTALL.md#wslwindows-subsystem-for-linux---binary

```shell
%env ROOT_PWD=xxx
!echo $ROOT_PWD | sudo -S apt-get install flex bison libssl-dev libelf-dev dwarves bc
%cd
!pwd
# KERNEL_VERSION=$(uname -r | cut -d '-' -f 1)
KERNEL_VERSION=!uname -r | cut -d '-' -f 1
KERNEL_VERSION=KERNEL_VERSION[0]
print(KERNEL_VERSION)
# !git clone --depth 1 https://github.com/microsoft/WSL2-Linux-Kernel.git -b linux-msft-wsl-$KERNEL_VERSION
!git clone --depth 1 https://github.com/microsoft/WSL2-Linux-Kernel.git -b linux-msft-wsl-{KERNEL_VERSION}
%cd WSL2-Linux-Kernel

!cp Microsoft/config-wsl .config
# CONFIG_IKHEADERS=m
!make oldconfig && make prepare
!make scripts
!make modules
!echo $ROOT_PWD | sudo -S make modules_install

# !mv /lib/modules/$KERNEL_VERSION-microsoft-standard-WSL2+/ /lib/modules/$KERNEL_VERSION-microsoft-standard-WSL2
!echo $ROOT_PWD | sudo -S mv /lib/modules/{KERNEL_VERSION}-microsoft-standard-WSL2+ /lib/modules/{KERNEL_VERSION}-microsoft-standard-WSL2
!pwd
KERNEL_VERSION=!uname -r | cut -d '-' -f 1
KERNEL_VERSION=KERNEL_VERSION[0]
print(KERNEL_VERSION)
!echo $ROOT_PWD | sudo -S mv /lib/modules/5.15.153.1-microsoft-standard-WSL2+ /lib/modules/5.15.153.1-microsoft-standard-WSL2
# !echo $ROOT_PWD | sudo -S apt-get install bpfcc-tools linux-headers-$(uname -r)
!echo $ROOT_PWD | sudo -S apt-get install bpfcc-tools
!ls /sbin | grep bpfcc
```

Fix:

```python
# /usr/lib/python3/dist-packages/bcc/table.py
from collections.abc import MutableMapping
```

```c
// /lib/modules/5.15.153.1-microsoft-standard-WSL2/build/include/linux/compiler-clang.h
#if defined(CONFIG_ARCH_USE_BUILTIN_BSWAP)
//#define __HAVE_BUILTIN_BSWAP32__
// #define __HAVE_BUILTIN_BSWAP64__
//#define __HAVE_BUILTIN_BSWAP16__
#endif /* CONFIG_ARCH_USE_BUILTIN_BSWAP */
```

## bpftool
* https://github.com/iovisor/bcc/tree/master/libbpf-tools

tool for inspection and simple manipulation of eBPF programs and maps


# BCC
```shell
%env ROOT_PWD=xxx
# !echo $ROOT_PWD | sudo -S /sbin/opensnoop-bpfcc # WHY NOT WORK???
!echo $ROOT_PWD | sudo -S /sbin/execsnoop-bpfcc
```
# 开发
```shell
%env ROOT_PWD=xxx
%cd BCC
!echo $ROOT_PWD | sudo -S python3 hello_world.py
```

TODO: more examples in [link](https://github.com/iovisor/bcc/blob/master/docs/tutorial_bcc_python_developer.md)
# 工具汇总
| Tool           | Description                                                                                                                                                                   |
| :------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| argdist        | 函数参数的频率或直方图.Trace a function and display a histogram or frequency count of its parameter values. Uses Linux eBPF/bcc.                                              |
| bashreadline   |                                                                                                                                                                               |
| biolatency     | 以直方图形式输出磁盘I/O延迟.<br/>Summarize block device I/O latency as a histogram.                                                                                           |
| biosnoop       | 输出每个磁盘I/O详情及其延迟.<br/>Trace block device I/O and print details incl. issuing PID.                                                                                  |
| biotop         |                                                                                                                                                                               |
| bitesize       |                                                                                                                                                                               |
| bpflist        | Display processes currently using BPF programs and maps.                                                                                                                      |
| btrfsdist      |                                                                                                                                                                               |
| btrfsslower    | see ext4slower                                                                                                                                                                |
| cachestat      | 每秒输出文件系统缓存的统计信息.<br/>Statistics for linux page cache hit/miss ratios. Uses Linux eBPF/bcc.                                                                     |
| cachetop       |                                                                                                                                                                               |
| capable        |                                                                                                                                                                               |
| cobjnew        |                                                                                                                                                                               |
| cpudist        | on-CPU、off-CPU任务时间的直方图.On- and off-CPU task time as a histogram.                                                                                                     |
| cpuunclaimed   |                                                                                                                                                                               |
| criticalstat   |                                                                                                                                                                               |
| dbslower       |                                                                                                                                                                               |
| dbstat         |                                                                                                                                                                               |
| dcsnoop        |                                                                                                                                                                               |
| dcstat         |                                                                                                                                                                               |
| deadlock       |                                                                                                                                                                               |
| deadlock.c     |                                                                                                                                                                               |
| drsnoop        |                                                                                                                                                                               |
| execsnoop      | 输出系统调用`execve(2)`创建的新进程.<br/>Trace new processes via exec() syscalls. Uses Linux eBPF/bcc.                                                                        |
| exitsnoop      | 跟踪进程退出, 展示其存活时间和退出原因.Trace all process termination (exit, fatal signal). Uses Linux eBPF/bcc.                                                               |
| ext4dist       |                                                                                                                                                                               |
| ext4slower     | 查看文件系统中慢的磁盘I/O.<br/>Trace slow ext4/btrfs/xfs/zfs file operations, with per-event details.                                                                         |
| filelife       |                                                                                                                                                                               |
| fileslower     |                                                                                                                                                                               |
| filetop        |                                                                                                                                                                               |
| funccount      | 函数调用事件计数.Count function, tracepoint, and USDT probe calls matching a pattern. Uses Linux eBPF/bcc.                                                                    |
| funclatency    |                                                                                                                                                                               |
| funcslower     |                                                                                                                                                                               |
| gethostlatency |                                                                                                                                                                               |
| hardirqs       | 硬件中断的耗时.Measure hard IRQ (hard interrupt) event time. Uses Linux eBPF/bcc.                                                                                             |
| inject         |                                                                                                                                                                               |
| javacalls      |                                                                                                                                                                               |
| javaflow       |                                                                                                                                                                               |
| javagc         |                                                                                                                                                                               |
| javaobjnew     |                                                                                                                                                                               |
| javastat       |                                                                                                                                                                               |
| javathreads    |                                                                                                                                                                               |
| killsnoop      |                                                                                                                                                                               |
| klockstat      |                                                                                                                                                                               |
| llcstat        | 使用PMC展示LLC(last-level cache)个各进程的命中率和未命中率.Summarize CPU cache references and misses by process. Uses Linux eBPF/bcc.                                         |
| mdflush        |                                                                                                                                                                               |
| memleak        |                                                                                                                                                                               |
| mountsnoop     |                                                                                                                                                                               |
| mysqld_qslower |                                                                                                                                                                               |
| nfsdist        |                                                                                                                                                                               |
| nfsslower      |                                                                                                                                                                               |
| nodegc         |                                                                                                                                                                               |
| nodestat       |                                                                                                                                                                               |
| offcputime     | 汇总线程阻塞及off-CPU的时间.Summarize off-CPU time by kernel stack trace. Uses Linux eBPF/bcc.                                                                                |
| offwaketime    |                                                                                                                                                                               |
| oomkill        |                                                                                                                                                                               |
| opensnoop      | 输出系统调用`open(2)`及其变种的调用情况.<br/>Trace open() syscalls. Uses Linux eBPF/bcc.                                                                                      |
| perlcalls      |                                                                                                                                                                               |
| perlflow       |                                                                                                                                                                               |
| perlstat       |                                                                                                                                                                               |
| phpcalls       |                                                                                                                                                                               |
| phpflow        |                                                                                                                                                                               |
| phpstat        |                                                                                                                                                                               |
| pidpersec      |                                                                                                                                                                               |
| profile        | CPU探查器, 用于理解消耗CPU资源的代码路径.<br/>Profile CPU usage by sampling stack traces. Uses Linux eBPF/bcc.                                                                |
| pythoncalls    |                                                                                                                                                                               |
| pythonflow     |                                                                                                                                                                               |
| pythongc       |                                                                                                                                                                               |
| pythonstat     |                                                                                                                                                                               |
| reset-trace    |                                                                                                                                                                               |
| rubycalls      |                                                                                                                                                                               |
| rubyflow       |                                                                                                                                                                               |
| rubygc         |                                                                                                                                                                               |
| rubyobjnew     |                                                                                                                                                                               |
| rubystat       |                                                                                                                                                                               |
| runqlat        | 测量CPU调度器的延迟, 称为运行队列延迟. 以直方图形式输出线程等待CPU的时间.<br/>Run queue (scheduler) latency as a histogram.                                                   |
| runqlen        | 采样CPU运行队列的长度, 以直方图形式展示等待运行的任务数量.Scheduler run queue length as a histogram.                                                                          |
| runqslower     | 列出运行队列延迟超过给定阈值的进程.Trace long process scheduling delays.                                                                                                      |
| shmsnoop       |                                                                                                                                                                               |
| slabratetop    |                                                                                                                                                                               |
| sofdsnoop      |                                                                                                                                                                               |
| softirqs       | 软件中断的耗时.Measure soft IRQ (soft interrupt) event time. Uses Linux eBPF/bcc.                                                                                             |
| solisten       |                                                                                                                                                                               |
| sslsniff       |                                                                                                                                                                               |
| stackcount     | 产生事件的调用栈计数.Count function calls and their stack traces. Uses Linux eBPF/bcc.                                                                                        |
| statsnoop      |                                                                                                                                                                               |
| syncsnoop      |                                                                                                                                                                               |
| syscount       | 系统调用计数.Summarize syscall counts and latencies.                                                                                                                          |
| tclcalls       |                                                                                                                                                                               |
| tclflow        |                                                                                                                                                                               |
| tclobjnew      |                                                                                                                                                                               |
| tclstat        |                                                                                                                                                                               |
| tcpaccept      | 输出被动TCP连接信息.<br/>Trace TCP passive connections (accept()). Uses Linux eBPF/bcc.                                                                                       |
| tcpconnect     | 输出主动TCP连接的信息.<br/>Trace TCP active connections (connect()). Uses Linux eBPF/bcc.                                                                                     |
| tcpconnlat     |                                                                                                                                                                               |
| tcpdrop        |                                                                                                                                                                               |
| tcplife        |                                                                                                                                                                               |
| tcpretrans     | 输出每个TCP重传报文.<br/>Trace or count TCP retransmits and TLPs. Uses Linux eBPF/bcc.                                                                                        |
| tcpstates      |                                                                                                                                                                               |
| tcpsubnet      |                                                                                                                                                                               |
| tcptop         |                                                                                                                                                                               |
| tcptracer      |                                                                                                                                                                               |
| tplist         | 展示内核跟踪点或USDT探针及其格式.Display kernel tracepoints or USDT probes and their formats.                                                                                 |
| trace          | 多种源(kprobes, uprobes, tracepoints, USDT probes)的每事件跟踪.Trace a function and print its arguments or return value, optionally evaluating a filter. Uses Linux eBPF/bcc. |
| ttysnoop       |                                                                                                                                                                               |
| vfscount       |                                                                                                                                                                               |
| vfsstat        |                                                                                                                                                                               |
| wakeuptime     |                                                                                                                                                                               |
| xfsdist        |                                                                                                                                                                               |
| xfsslower      | see ext4slower                                                                                                                                                                |
| zfsdist        |                                                                                                                                                                               |
| zfsslower      | see ext4slower                                                                                                                                                                |
# 所有层
## argdist

```
argdist {-C|-H} [options] probe
```

- `-C`: frequency count
- `-H`: power-of-two histogram

`probe`:

```
eventname(signature) [ :type[,type...] :expr[,expr...] [:filter] ] [#label]
```

- `eventname`, `signature`: 同[eventname](#trace), 但不支持内核函数的简写
- `type`: 汇总的值的类型
- `expr`: 汇总的表达式; 特殊的变量: `$retval`、`$latency`、`$entry(param)`
- `filter`: 可选的过滤事件的布尔表达式
- `label`: 可选的添加到输出的标记性文本

```
## 内核函数vfs_read()的返回值的直方图
argdist -H 'r::vfs_read()'
## 进程1005中用户级libc中read()的返回值的直方图
argdist -p 1005 -H 'r:c:read()' # PID=1005
## 按系统调用ID计数
argdist -C 't:raw_syscalls:sys_enter():int:args->id'
## tcp_sendmsg(...)中size参数的计数
argdist -C 'p::tcp_sendmsg(struct sock *sk, struct msghdr *msg, size_t size):u32:size'
argdist -H 'p::tcp_sendmsg(struct sock *sk, struct msghdr *msg, size_t size):u32:size'
## 进程181中libc的write(...)中fd参数的计数
argdist -p 181 -C 'p:c:write(int fd):int:fd'
## 延迟>0.1ms的进程的频率
argdist -C 'r::__vfs_read():u32:$PID:$lantency > 100000'
```

## funccount

```
funccount [options] eventname
```

`eventname`:

- `name`, `p:name`: 探查内核函数`name()`
- `lib:name`, `p:lib:name`: 探查库`lib`中用户层的函数`name()`
- `path:name`: 探查路径`path`下用户层函数`name()`
- `t:system:name`: 探查跟踪点`system:name`
- `u:lib:name`: 探查库`lib`中名称为`name`的USDT探针
- `*`: 通配符. 选项`-r`允许使用正则表达式.

```
funccount 'tcp_*'
funccount -i 1 'tcp_send*'
funccount -i 1 't:block:*'
funccount -i 1 t:sched:sched_process_fork
funccount -i 1 c:getaddrinfo
funccount 'go:os.*'
```

```
$ bpftrace -e 'k:tcp_* { @[probe] = count(); }'
Attaching 333 probes...
^C

@[kprobe:tcp_recv_timestamp]: 1
@[kprobe:tcp_write_xmit]: 1
@[kprobe:tcp_init_cwnd]: 1
@[kprobe:tcp_rate_skb_delivered]: 1
@[kprobe:tcp_wfree]: 1
......
```

## funcslower
## funclatency
## profile

```
# -a: include kernel annotation
# -f: output in folded format
profile -af 30 > profile.output
# 生成火焰图
flamegraph.pl --color=java < profile.output > profile.svg
```
## stackcount

```
stackcount [options] eventname
```

`eventname` see [eventname](#funccount)


```
stackcount t:block:block_rq_insert
stackcount ip_output
statckcount t:sched:sched_switch
stackcount t:syscalls:sys_enter_read
```

## tplist
```
$ tplist -v syscalls:sys_enter_read
syscalls:sys_enter_read
    int __syscall_nr;
    unsigned int fd;
    char * buf;
    size_t count;

```
## trace

```
trace [options] probe [probe ...]
```

`probe`:

```
eventname(signature) (boolean filter) "format string", arguments
```

`eventname`:

- `name`, `p:name`: 探查内核函数`name()`
<br>`r::name`: 探查内核函数`name()`的返回
- `lib:name`, `p:lib:name`: 探查库`lib`中用户层的函数`name()`
<br>`r:lib:name`: 探查库`lib`中用户层的函数`name()`的返回值
- `path:name`: 探查路径`path`下用户层函数`name()`
<br>`r:path:name`: 探查路径`path`下用户层函数`name()`的返回值
- `t:system:name`: 探查跟踪点`system:name`
- `u:lib:name`: 探查库`lib`中名称为`name`的USDT探针
- `*`: 通配符. 选项`-r`允许使用正则表达式.


The `format string` is based on `printf()`.

```
## fs/open.c
trace 'do_sys_open "%s", arg2'
trace 'r::do_sys_open "ret: %d", retval'

## kernel/time/hrtimer.c
trace -U 'do_nanosleep "mode: %d", arg2'
trace 'do_nanosleep(struct hrtimer_sleeper *t) "task: %x", t->task'

## pam lib
trace 'pam:pam_start "%s: %s", arg1, arg2'

## trace structs
trace 'do_nanosleep(struct hrtimer_sleeper *t) "task: %x", t->task'
trace -I 'net/sock.h' 'udpv6_sendmsg(struct sock *sk) (sk->sk_dport == 13568)'
```

# 应用

传统工具: 系统调试器.
## bashfunc
## bashfunclat
## bashreadline
## dbslower
## dbstat
## mysqld_clat
## mysqld_qslower
## ucalls
## uflow
## ugc
## uobjnew
## ustat
## uthreads
# 运行时

传统工具: 运行时调试器.
## javathreads
## jnistacks
# 系统库

传统工具: ltrace(1).
## gethostlatency
## memleak
## pmheld
## pmlock
## sslsniff
## threadssnoop
# 系统调用接口

传统工具: strace(1), perf(1).
## elfsnoop
## eperm
## execsnoop
## exitsnoop
```
$ exitsnoop
PCOMM            PID    PPID   TID    AGE(s)  EXIT_CODE
ls               1410   942    1410   0.00    0
tree             1412   942    1412   0.02    0
^C
```
## ioprofile
## killsnoop
## modsnoop
## naptime
## opensnoop

```
## 跟踪所有open()系统调用
opensnoop
## 跟踪10内的所有open()系统调用
opensnoop -d 10
```

字段:

- TIME(s): Time of the call, in seconds.
- UID: User ID
- PID: Process ID
- TID: Thread ID
- COMM: Process name
- FD: File descriptor (if success), or -1 (if failed)
- ERR: Error number (see the system's errno.h)
- FLAGS: Flags passed to open(2), in octal
- PATH: Open path


## pidpersec
## scread
## setuids
## shellsnoop
## signals
## statsnoop
## syncsnoop
## syscount

```
# 每秒的系统调用计数: top10
$ syscount -i 1
Tracing syscalls, printing top 10... Ctrl+C to quit.
[09:22:36]
SYSCALL                   COUNT
rt_sigprocmask               24
bpf                          13
select                       13
write                         9
read                          7
getpid                        6
futex                         3
gettid                        2
epoll_wait                    2
ioctl                         2

[09:22:37]
SYSCALL                   COUNT
rt_sigprocmask               16
bpf                          11
select                        9
write                         6
getpid                        4
read                          4
futex                         3
clock_nanosleep               1
```

```
$ bpftrace -e 't:syscalls:sys_enter_* { @[probe] = count(); }'
Attaching 332 probes...
^C

@[tracepoint:syscalls:sys_enter_prctl]: 1
@[tracepoint:syscalls:sys_enter_epoll_create1]: 1
@[tracepoint:syscalls:sys_enter_bind]: 1
@[tracepoint:syscalls:sys_enter_kill]: 1
@[tracepoint:syscalls:sys_enter_clone]: 1
@[tracepoint:syscalls:sys_enter_sendmmsg]: 1
@[tracepoint:syscalls:sys_enter_socket]: 1
......
```

# 内核

传统工具: Ftrace, perf(1).
## 虚拟文件系统(VFS)
### bufgrow
### cacheestat
### cachetop
### dcsnoop
### dcstat
### filelife
### fileslower
### filetop
### filetype
### fsrwstat
### icstat
### mmapfiles
### mountsnoop
### readahead
### vfscount
### vfssize
### vfsstat
### writeback
### writesync
## 文件系统
### btrfsslower, btrfsdist
### ext4slower, ext4dist
### nfsslower, nfsdist
### overlayfs
### xfsslower, xfsdist
### zfsslower, zfsdist
## 卷管理器
### mdflush
## 块设备
### bioerr
### biolatency
### biopattern
### biosnoop
### biostacks
### biotop
### bitesize
### blkthrot
### issched
### seeksize
## Sockets
### skbdrop
### skblife
### so1stbyte
### soaccept
### socketio
### socksize
### sockstat
### soconnect
### soconnlat
### sofamily
### sofdsnoop
### soprotocol
### sormem
## TCP/UDP
### tcpaccept
### tcpconnect
### tcpconnlat
### tcpdrop
### tcplife
### tcpnagle
### tcpreset
### tcpretrans
### tcpstates
### tcpsubnet
### tcpsynbl
### tcptop
### tcptracer
### tcpwin
### updconnect
## IP
### ipecn
### qdisc-fq
### superping
## 调度器
### cppunclaimed
### cpudist

```
# 运行10秒, 输出一次
$ cpudist 10 1
Tracing on-CPU time... Hit Ctrl-C to end.

     usecs               : count     distribution
         0 -> 1          : 0        |                                        |
         2 -> 3          : 0        |                                        |
         4 -> 7          : 1        |                                        |
         8 -> 15         : 2        |*                                       |
        16 -> 31         : 5        |****                                    |
        32 -> 63         : 0        |                                        |
        64 -> 127        : 5        |****                                    |
       128 -> 255        : 3        |**                                      |
       256 -> 511        : 0        |                                        |
       512 -> 1023       : 2        |*                                       |
      1024 -> 2047       : 0        |                                        |
      2048 -> 4095       : 35       |**********************************      |
      4096 -> 8191       : 28       |***************************             |
      8192 -> 16383      : 12       |***********                             |
     16384 -> 32767      : 36       |***********************************     |
     32768 -> 65535      : 27       |**************************              |
     65536 -> 131071     : 41       |****************************************|
    131072 -> 262143     : 19       |******************                      |
```

### cpuwalk
### deadlock
### mheld
### mlock
### offcpuhist
### offcputime


```
# -u     Only trace user threads (no kernel threads).
# -k     Only trace kernel threads (no user threads).
# -U     Show stacks from user space only (no kernel space stacks).
# -K     Show stacks from kernel space only (no user space stacks).
# -f     Print output in folded stack format.

offcputime -fKu 10 > offcputime.output
# 生成火焰图
flamegraph.pl < offcputime.output > offcputime.svg
```




### offwaketime
### pidnss

### runqlat

```
# 运行10秒, 输出一次
$ runqlat 10 1
Tracing run queue latency... Hit Ctrl-C to end.

     usecs               : count     distribution
         0 -> 1          : 7        |**                                      |
         2 -> 3          : 43       |*************                           |
         4 -> 7          : 63       |********************                    |
         8 -> 15         : 123      |****************************************|
        16 -> 31         : 56       |******************                      |
        32 -> 63         : 15       |****                                    |
        64 -> 127        : 3        |                                        |
       128 -> 255        : 6        |*                                       |
       256 -> 511        : 4        |*                                       |
       512 -> 1023       : 0        |                                        |
      1024 -> 2047       : 1        |                                        |

```

### runqlen


```
# 运行10秒, 输出一次
$ runqlen 10 1
Sampling run queue length... Hit Ctrl-C to end.

     runqlen       : count     distribution
        0          : 991      |****************************************|
```

### runqslower


```
$ runqslower 100
Tracing run queue latency higher than 100 us
TIME     COMM             PID           LAT(us)
09:40:00 b'kworker/u2:2'  1472              156
09:40:00 b'runqslower' 1643              162
09:40:00 b'runqslower' 1643              181
09:40:00 b'runqslower' 1643              400
09:40:01 b'systemd-journal' 1644              121
09:40:01 b'runqslower' 1643              144
09:40:01 b'runqslower' 1643              271
09:40:01 b'runqslower' 1643              151
09:40:01 b'runqslower' 1643              181
09:40:01 b'jbd2/sda2-8'   327               517
09:40:01 b'jbd2/sda2-8'   327               865
09:40:01 b'journal-offline' 1645              896
09:40:01 b'journal-offline' 1644              994
09:40:01 b'sshd'          1039             2033
```

### smpcalls


```
$ bpftrace smpcalls.bt
Attaching 8 probes...
Tracing SMP calls. Hit Ctrl-C to stop.
^C


@time_ns[__cpa_flush_tlb]:
[512, 1K)              1 |@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|

@time_ns[do_kernel_range_flush]:
[2K, 4K)               1 |@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|

@time_ns[do_flush_tlb_all]:
[1K, 2K)               1 |@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|

@time_ns[remote_function]:
[2K, 4K)               1 |@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|
[4K, 8K)               1 |@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@|
```


### softirqs


```
$ softirqs 10 1
Tracing soft irq event time... Hit Ctrl-C to end.
^C
SOFTIRQ          TOTAL_usecs
net_tx                     5
rcu                       15
block                    125
net_rx                   294
timer                   1445
```

```
$ bpftrace -e 'tracepoint:irq:softirq_entry { @[args->vec] = count(); }'
Attaching 1 probe...
^C

@[2]: 1
@[4]: 2
@[3]: 5
@[9]: 9
@[1]: 50
```




### threaded
### wakeuptime
### workq
## 虚拟内存
### brkstack
### drssnoop
### faults
### ffaults
### fmapfault
### hfaults
### kmem
### kpages
### memleak
### mmapsnoop
### numamove
### oomkill
### shmsnoop
### slabratetop
### swapin
### vmscan
# 硬件

传统工具: perf, sar, /proc计数器.
## 网络设备
### ieee80211scan
## 设备驱动器
### criticalstat
### hardirqs

```
$ hardirqs 10 1
Tracing hard irq event time... Hit Ctrl-C to end.
^C
HARDIRQ                    TOTAL_usecs
ata_piix                            98
enp0s3                             254
```

### nvmelatency
### scsilatency
### scsiresult
### ttysnoop
## CPU
### cpufreq
### llcstat
```
# 在虚拟机中无法运行
$ llcstat
perf_event_open failed: No such file or directory
Failed to attach to a hardware event. Is this a virtual machine?
```

# 其他
## capable
## kvmexits
## xenhyper