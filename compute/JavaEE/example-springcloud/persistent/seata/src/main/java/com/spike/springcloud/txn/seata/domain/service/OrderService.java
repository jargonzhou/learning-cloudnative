package com.spike.springcloud.txn.seata.domain.service;

public interface OrderService {

    /**
     * 创建订单
     *
     * @param userId        用户ID
     * @param commodityCode 商品编号
     * @param orderCount    订购数量
     */
    void create(String userId, String commodityCode, int orderCount);
}