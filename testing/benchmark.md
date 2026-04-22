# Benchmark
- [Jim Gray. The Benchmark Handbook, Second Edition, 1993](http://jimgray.azurewebsites.net/benchmarkhandbook/toc.htm)

指标
 - 延迟量级: [GitHub - donnemartin/system-design-primer: Learn how to design large-scale systems. Prep for the system design interview. Includes Anki flashcards.](https://github.com/donnemartin/system-design-primer#latency-numbers-every-programmer-should-know)
- 延迟量级动态变化图: [Numbers Every Programmer Should Know By Year](https://people.eecs.berkeley.edu/~rcs/research/interactive_latency.html)

可视化
- State of the Art of Performance Visualization

# Apache JMeter
* https://jmeter.apache.org/

> The **Apache JMeter™** application is open source software, a 100% pure Java application designed to load test functional behavior and measure performance. It was originally designed for testing Web Applications but has since expanded to other test functions.

- [使用JMeter进行负载测试——终极指南 ](http://www.importnew.com/13876.html)

Books:
- Apache JMeter: A Practical Beginner's Guide to Automated Testing and Performance Measurement for Your Websites by Emily H. Halili (Packt Publishing)
- Performance Testing with JMeter, 2nd Edition by Bayo Erinle (Packt Publishing)
- JMeter Cookbook by Bayo Erinle (Packt Publishing)

# OS
- [Linux常用性能调优工具索引](https://blog.yufeng.info/archives/2524)
- [Linux Tracing Technologies](https://www.kernel.org/doc/html/latest/trace/index.html)
	- [ftrace - Function Tracer](https://www.kernel.org/doc/html/v5.0/trace/ftrace.html)

- [sysctl](https://wiki.archlinux.org/title/Sysctl)
> sysctl is a tool for examining and changing *kernel parameters* at runtime. sysctl is implemented in *procfs*, the virtual process file system at `/proc/`.

- [nmon](http://nmon.sourceforge.net/pmwiki.php)
> nmon is short for **N**igel's performance **Mon**itor for Linux on POWER, x86, x86_64, Mainframe & now ARM (Raspberry Pi)
- [Install NMON - CentOS 64bit](https://gist.github.com/sunggun-yu/4416430)

- [cpustat](https://github.com/uber-common/cpustat)

> cpustat is a tool for Linux systems to measure performance. You can think of it like a fancy sort of top that does different things. This project is motived by Brendan Gregg's USE Method and tries to expose CPU utilization and saturation in a helpful way.

- perf
- BPF

- [DTrace](https://dtrace.org/)
> DTrace is a performance analysis and troubleshooting tool that is included by default with various operating systems, including Solaris, Mac OS X and FreeBSD. A Linux port is in development.


- [SystemTap](https://sourceware.org/systemtap/)
> SystemTap provides free software (GPL) infrastructure to simplify the gathering of information about the running Linux system. This assists diagnosis of a performance or functional problem. SystemTap eliminates the need for the developer to go through the tedious and disruptive instrument, recompile, install, and reboot sequence that may be otherwise required to collect data.
>
> SystemTap provides a simple command line interface and scripting language for writing instrumentation for a live running kernel plus user-space applications. We are publishing samples, as well as enlarging the internal "tapset" script library to aid reuse and abstraction.
>
> Among other tracing/probing tools, SystemTap is the tool of choice for complex tasks that may require live analysis, programmable on-line response, and whole-system symbolic access. SystemTap can also handle simple tracing jobs.


- [lmbench](https://lmbench.sourceforge.net/)
> lmbench is a suite of simple, portable, ANSI/C microbenchmarks for UNIX/POSIX. In general, it measures two key features: latency and bandwidth. lmbench is intended to give system developers insight into basic costs of key operations.

  - [new Code](https://github.com/intel/lmbench)
  - [lmbench: Portable Tools for Performance Analysis](https://www.usenix.org/legacy/publications/library/proceedings/sd96/full_papers/mcvoy.pdf): Larry McVoy, Carl Staelin.Proceedings of the USENIX 1996 Annual Technical Conference, San Diego, California, 1996.

# Network
- [HTTP(S) benchmark tools, testing/debugging, & restAPI (RESTful)](https://github.com/denji/awesome-http-benchmark)
- [Web Framework Benchmarks](https://www.techempower.com/benchmarks)


## wrk
- https://github.com/wg/wrk
> wrk is **a modern HTTP benchmarking tool** capable of generating significant load when run on a single multi-core CPU. It combines a multithreaded design with scalable event notification systems such as epoll and kqueue.

Command Line Options
```shell
# /home/zhoujiagen/wrk-4.2.0
$ ./wrk
Usage: wrk <options> <url>
  Options:
    -c, --connections <N>  Connections to keep open
    -d, --duration    <T>  Duration of test
    -t, --threads     <N>  Number of threads to use

    -s, --script      <S>  Load Lua script file
    -H, --header      <H>  Add header to request
        --latency          Print latency statistics
        --timeout     <T>  Socket/request timeout
    -v, --version          Print version details

  Numeric arguments may include a SI unit (1k, 1M, 1G)
  Time arguments may include a time unit (2s, 2m, 2h)
```
examples:
```shell
wrk -t 10 -c50 -d40s http://localhost:8080/hello
```

upload file
- https://github.com/playframework/prune/blob/master/src/main/resources/com/typesafe/play/prune/assets/wrk_upload.lua#L1-L36
- https://gist.github.com/tonytonyjan/d2a612f2b3f37837fc4d5c1409ac0b1e


## [gatling](https://gatling.io/)
> Gatling is a powerful load-testing solution for applications, APIs, and microservices.


## ab - Apache HTTP server benchmarking tool
* https://httpd.apache.org/docs/2.4/programs/ab.html

> `ab` is a tool for benchmarking your Apache Hypertext Transfer Protocol (HTTP) server. It is designed to give you an impression of how your current Apache installation performs. This especially shows you how many requests per second your Apache installation is capable of serving.


- [install on Ubuntu](https://documentation.ubuntu.com/server/how-to/web-services/install-apache2/index.html)
```shell
sudo apt install apache2
# /etc/apache2/
sudo systemctl status apache2.service
```

## Nmap
- [Home](https://nmap.org/): the Network Mapper
- [What is Nmap and How to Use it – A Tutorial for the Greatest Scanning Tool of All Time](https://www.freecodecamp.org/news/what-is-nmap-and-how-to-use-it-a-tutorial-for-the-greatest-scanning-tool-of-all-time/)
> [!note] Nmap
> Nmap ("Network Mapper") is a free and open source utility for network discovery and security auditing

# Database

- TPC-A
- TPC-C
- TPC-E

## sysbench
- [Code](https://github.com/akopytov/sysbench)

> sysbench is a scriptable multi-threaded benchmark tool based on LuaJIT. It is most frequently used for database benchmarks, but can also be used to create arbitrarily complex workloads that do not involve a database server.
>
> sysbench comes with the following bundled benchmarks:
>- `oltp_*.lua`: a collection of OLTP-like database benchmarks
>- `fileio`: a filesystem-level benchmark
>- `cpu`: a simple CPU benchmark
>- `memory`: a memory access benchmark
>- `threads`: a thread-based scheduler benchmark
>- `mutex`: a POSIX mutex benchmark

misc:

- [Stress Testing in CentOS: Using Sysbench and Stress Commands](https://medium.com/@chachia.mohamed/stress-testing-in-centos-using-sysbench-and-stress-commands-2c4530122c45)
- [Percona-Lab/sysbench-tpcc](https://github.com/Percona-Lab/sysbench-tpcc): Sysbench scripts to generate a tpcc-like workload for MySQL and PostgreSQL
	- [Percona-Lab/tpcc-mysql](https://github.com/Percona-Lab/tpcc-mysql): archived on Sep 25, 2020.

Alternatives:
- [BenchmarkSQL](https://benchmarksql.readthedocs.io/en/latest/): Java implementation
	- [Code](https://sourceforge.net/projects/benchmarksql/)
		- Github
			- [pingcap/benchmarksql](https://github.com/pingcap/benchmarksql): 5.0
			- [petergeoghegan/benchmarksql](https://github.com/petergeoghegan/benchmarksql): 5.1, Unmaintained
				- [wieck/benchmarksql](https://github.com/wieck/benchmarksql): V6
				- [pgsql-io/benchmarksql](https://github.com/pgsql-io/benchmarksql): V6
