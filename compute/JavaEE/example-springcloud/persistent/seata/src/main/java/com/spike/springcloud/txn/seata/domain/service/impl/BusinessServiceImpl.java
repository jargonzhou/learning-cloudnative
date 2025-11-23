package com.spike.springcloud.txn.seata.domain.service.impl;

import com.spike.springcloud.txn.seata.domain.service.BusinessService;
import com.spike.springcloud.txn.seata.domain.service.OrderService;
import com.spike.springcloud.txn.seata.domain.service.StorageService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);

    public static final String USER_ID = "U100001";
    public static final String COMMODITY_CODE = "C00321";

    @Resource
    private StorageService storageService;
    @Resource
    private OrderService orderService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    private final Random random = new Random();

    @Override
    @GlobalTransactional(timeoutMills = 300000, name = "spring-seata-tx") // 全局事务
    public void purchase(String userId,
                         String commodityCode,
                         int orderCount,
                         boolean mockFail) {
        LOGGER.info("purchase begin ... xid: " + RootContext.getXID());
        storageService.deduct(commodityCode, orderCount);
        orderService.create(userId, commodityCode, orderCount);
        // 模拟失败
        if (mockFail) {
            throw new RuntimeException("txn exception mock!");
        }
    }

    @PostConstruct
    public void initData() {
        jdbcTemplate.update("delete from account_tbl");
        jdbcTemplate.update("delete from order_tbl");
        jdbcTemplate.update("delete from stock_tbl");
        // 账户余额: 10000
        jdbcTemplate.update("insert into account_tbl(user_id,money) values('" + USER_ID + "','10000') ");
        // 库存: 100
        jdbcTemplate.update(
                "insert into stock_tbl(commodity_code,count) values('" + COMMODITY_CODE + "','100') ");
    }

}