package com.spike.dataengineering.log4j2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// examples
// https://www.cnblogs.com/antLaddie/p/15904895.html
// https://zhuanlan.zhihu.com/p/36554554
public class Log4j2Test {
    private static final Logger LOG = LoggerFactory.getLogger(Log4j2Test.class);

    public static void main(String[] args) {
//        LOG.fatal(" 严重错误，一般造成系统崩溃并终止运行");
        LOG.error("错误信息，不会影响系统运行");         //默认级别
        LOG.warn("警告信息，可能会发生问题");
        LOG.info("运行信息，数据连接，网络连接，IO操作等");
        LOG.debug("调试信息，一般在开发中使用，记录程序变量传递信息等等");
        LOG.trace("追踪信息，记录程序所有的流程信息");
    }
}
