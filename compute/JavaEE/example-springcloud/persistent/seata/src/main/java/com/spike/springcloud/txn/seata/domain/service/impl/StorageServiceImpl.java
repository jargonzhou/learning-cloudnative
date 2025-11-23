package com.spike.springcloud.txn.seata.domain.service.impl;

import com.spike.springcloud.txn.seata.domain.model.Storage;
import com.spike.springcloud.txn.seata.domain.service.StorageService;
import com.spike.springcloud.txn.seata.infra.dao.StorageMapper;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    @Resource
    private StorageMapper storageMapper;

    @Override
    public void deduct(String commodityCode, int count) {
        LOGGER.info("Stock Service Begin ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting inventory SQL: update stock_tbl set count = count - {} where commodity_code = {}", count,
                commodityCode);

        Storage stock = storageMapper.findByCommodityCode(commodityCode);
        stock.setCount(stock.getCount() - count);
        storageMapper.updateById(stock);
        LOGGER.info("Stock Service End ... ");

    }

}