package com.spike.springcloud.txn.seata.domain.service.impl;

import com.spike.springcloud.txn.seata.domain.model.Account;
import com.spike.springcloud.txn.seata.domain.service.AccountService;
import com.spike.springcloud.txn.seata.infra.dao.AccountMapper;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    @Resource
    private AccountMapper accountMapper;

    @Override
    public void debit(String userId, int money) {
        LOGGER.info("Account Service ... xid: " + RootContext.getXID());
        LOGGER.info("Deducting balance SQL: update account_tbl set money = money - {} where user_id = {}", money,
                userId);
        Account account = accountMapper.selectByUserId(userId);
        account.setMoney(account.getMoney() - money);
        // need fix: 余额检查
        accountMapper.updateById(account);
        LOGGER.info("Account Service End ... ");
    }
}