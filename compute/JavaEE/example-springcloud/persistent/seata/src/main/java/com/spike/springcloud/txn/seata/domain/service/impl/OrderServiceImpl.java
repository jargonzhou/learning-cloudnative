package com.spike.springcloud.txn.seata.domain.service.impl;

import com.spike.springcloud.txn.seata.domain.model.Order;
import com.spike.springcloud.txn.seata.domain.service.AccountService;
import com.spike.springcloud.txn.seata.domain.service.OrderService;
import com.spike.springcloud.txn.seata.infra.dao.OrderMapper;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private AccountService accountService;
    @Resource
    private OrderMapper orderMapper;

    @Override
    public void create(String userId, String commodityCode, int count) {
        LOGGER.info("Order Service Begin ... xid: " + RootContext.getXID());
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        int orderMoney = calculate(commodityCode, count);
        order.setMoney(orderMoney);

        orderMapper.insert(order);
        accountService.debit(userId, orderMoney);
    }

    private int calculate(String commodityCode, int count) {
        return 200 * count;
    }

}