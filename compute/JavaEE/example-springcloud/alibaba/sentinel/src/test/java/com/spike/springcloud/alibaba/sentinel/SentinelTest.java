package com.spike.springcloud.alibaba.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 资源:
 * @see Entry
 * @see SphU#entry(String) - 抛出异常方式
 * @see SphU#asyncEntry(String) - 异步调用方式
 * @see com.alibaba.csp.sentinel.SphO - 返回布尔值方式
 * @see SentinelResource - 注解方式
 * @see Entry#exit()
 *
 * Entry的功能插槽:
 * - 资源的调用路径: 树状结构, 限流降级的操作目标
 * @see com.alibaba.csp.sentinel.slots.nodeselector.NodeSelectorSlot
 * - 资源的统计信息和调用者信息, 多维度限流降级的目标: 资源响应时间RT, QPS, 线程数, block数量, 异常数.
 * @see com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot
 * - 运行时监控指标信息: clusterNode, 调用者origin, 资源ID的defaultnode, 入口.
 * @see com.alibaba.csp.sentinel.slots.statistic.StatisticSlot
 * - 流量控制: 根据限流规则和之前slot的统计状态
 * @see com.alibaba.csp.sentinel.slots.block.flow.FlowSlot
 *  - 热点参数
 * @see com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowSlot
 * - 黑白名单控制: 根据黑麦名单配置和调用者信息
 * @see com.alibaba.csp.sentinel.slots.block.authority.AuthoritySlot
 * - 熔断降级: 根据统计信息和规则. 资源的平均响应时间RT, 异常比率.
 * @see com.alibaba.csp.sentinel.slots.block.degrade.DegradeSlot
 * - 控制总的入口流量: 根据系统状态. 只对入口流量起作用.
 * @see com.alibaba.csp.sentinel.slots.system.SystemSlot
 * @see com.alibaba.csp.sentinel.EntryType#IN
 *
 * SPI接口扩展Slot Chain
 * @see com.alibaba.csp.sentinel.slotchain.ProcessorSlot
 *
 * 数据结构:
 * - 资源调用路径: 上下文name, 调用方origin
 * @see com.alibaba.csp.sentinel.context.ContextUtil#enter(String, String)
 * - 资源请求token: 输入名称name
 * @see SphU#entry(String)
 * - 资源调用路径 + 输入名称
 * @see com.alibaba.csp.sentinel.node.DefaultNode
 * - 资源的统计信息 + 调用者origin
 * @see com.alibaba.csp.sentinel.node.ClusterNode
 * - 滑动窗口
 * @see com.alibaba.csp.sentinel.slots.statistic.base.LeapArray
 * windowIntervalMs: 滑动窗口的时间长度, 默认1000ms
 * sampleCount: 滑动窗口中格子数量, 默认为2.
 * @see com.alibaba.csp.sentinel.node.SampleCountProperty#SAMPLE_COUNT
 * @see com.alibaba.csp.sentinel.node.IntervalProperty#INTERVAL
 *
 * 规则:
 * - 流量控制规则
 * @see FlowRule
 * - 熔断降级规则
 * @see com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule
 * - 系统保护规则
 * @see com.alibaba.csp.sentinel.slots.system.SystemRule
 * - 访问控制规则
 * @see com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule
 * - 热点规则
 * @see com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule
 * - 查询/更改
 * http://localhost:8719/getRules?type=<XXXX>
 *     flow, degrade, system
 * http://localhost:8719/getParamRules
 * - 持久化
 * https://sentinelguard.io/zh-cn/docs/dynamic-rule-configuration.html
 * - 生效
 * http://localhost:8719/cnode?id=<资源名称>
 * 日志: https://sentinelguard.io/zh-cn/docs/logs.html
 *
 * 异常:
 * @see BlockException#isBlockException(Throwable)
 * - 业务异常统计
 * @see com.alibaba.csp.sentinel.Tracer
 * - 回调
 * @see com.alibaba.csp.sentinel.slots.statistic.StatisticSlotCallbackRegistry
 * @see com.alibaba.csp.sentinel.slotchain.ProcessorSlotEntryCallback
 * @see com.alibaba.csp.sentinel.slotchain.ProcessorSlotExitCallback
 * </pre>
 */
public class SentinelTest {

    static {
        System.setProperty("project.name", "sentinel-test");
        // others: sentinel.properties
    }

    @Test
    public void quickStart() {
        // flow rules
        initFlowRules();

        // delay
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            // ignore
        }

        // loop
        while (true) {
            try (Entry entry = SphU.entry("HelloWorld")) {
                // business
                System.out.println("hello world");
            } catch (BlockException e) {
                // handle blocking
                System.err.println("blocked: " + e);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // ignore
            }
        }
    }

    @Test
    public void quickStartAnnotation() {
        for (int i = 0; i < 10; i++) {
            helloWorld();
        }
    }

    @SentinelResource("HelloWorldMethod") // need Spring AOP or AspectJ
    public void helloWorld() {
        System.out.println("hello world method");
    }

    private void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setResource("HelloWorld");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS); // QPS
        flowRule.setCount(5);
        rules.add(flowRule);

        FlowRuleManager.loadRules(rules);
    }
}
