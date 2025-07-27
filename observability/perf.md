# perf
* https://perfwiki.github.io/main/
* [perf Examples](https://www.brendangregg.com/perf.html)
 [perf doc in Linux code](https://github.com/torvalds/linux/blob/master/tools/perf/Documentation/perf.txt)

the perf Linux profiler, which has also been called Performance Counters for Linux (PCL), Linux perf events (LPE), or perf_events

The pref_events facility is used by perf command for sampling and tracing, and it was added to Linux 2.6.21 in 2009.

事件驱动的可观察性工具:
![](https://www.brendangregg.com/perf_events/perf_events_map.png)


# Installation
## Ubuntu

```shell
sudo apt-get install linux-tools-common linux-tools-generic linux-tools-`uname -r`
```
## WSL

```shell
!perf --version
%env ROOT_PWD=xxx
!echo $ROOT_PWD | sudo -S apt-get install linux-tools-5.15.153.1-microsoft-standard-WSL2
```

* [Is there any method to run perf under WSL?](https://stackoverflow.com/questions/60237123/is-there-any-method-to-run-perf-under-wsl)

Method 1:

```
sudo apt install build-essential flex bison libssl-dev libelf-dev
git clone --depth=1 https://github.com/microsoft/WSL2-Linux-Kernel.git
cd WSL2-Linux-Kernel/tools/perf
make
# use perf in that folder

%cd ~/WSL2-Linux-Kernel/tools/perf/
!./perf --version
```

# Method 2: with linux-tools-generic

```shell
!apt info linux-tools-generic
!/usr/lib/linux-tools-5.15.0-25/perf --version
```
