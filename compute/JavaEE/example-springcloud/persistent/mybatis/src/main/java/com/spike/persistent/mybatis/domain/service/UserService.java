package com.spike.persistent.mybatis.domain.service;

import com.spike.persistent.mybatis.dao.entity.Users;
import com.spike.persistent.mybatis.dao.mapper.autogen.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
public class UserService {
    @Autowired
    public UsersMapper usersMapper;

    @Transactional
    public Integer postUser() {

        Users user = Users.builder()
                .email("user@example.com")
                .password("xxx")
                .build();
        usersMapper.insert(user);
        Integer id = user.getId();

        // 事务状态
        System.err.println(TransactionAspectSupport.currentTransactionStatus().getTransactionName());

        user.setPassword("yyy");
        usersMapper.updateByPrimaryKey(user);

        return id;
    }
}
