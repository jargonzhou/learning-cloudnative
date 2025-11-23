package com.spike.springcloud.txn.seata.domain.service;

public interface BusinessService {

    /**
     * 用户订购商品
     *
     * @param userId        用户ID
     * @param commodityCode 商品编号
     * @param orderCount    订购数量
     * @param mockFail      是否模拟失败
     */
    void purchase(String userId, String commodityCode, int orderCount, boolean mockFail);
}