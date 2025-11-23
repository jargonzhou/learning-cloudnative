package com.spike.persistent.mybatis.mapper;


import com.spike.persistent.mybatis.entity.User;

public interface UserMapper {

    int createSchema();

    int insert(User user);

    User selectById(Integer id);
}