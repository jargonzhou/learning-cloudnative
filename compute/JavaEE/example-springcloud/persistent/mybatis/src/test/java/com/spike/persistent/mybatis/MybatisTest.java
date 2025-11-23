package com.spike.persistent.mybatis;

import com.spike.persistent.mybatis.entity.User;
import com.spike.persistent.mybatis.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

// Test with H2.
public class MybatisTest {
    private static final Logger LOG = LoggerFactory.getLogger(MybatisTest.class);

    @Test
    public void regular() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            int createSchemaResult = userMapper.createSchema();
            LOG.info("Create schema result: {}", createSchemaResult);

            User toInsertUser = new User();
            toInsertUser.setId(1);
            toInsertUser.setUserName("hello");
            int insertResult = userMapper.insert(toInsertUser);
            LOG.info("Insert result: {}", insertResult);

            User user = userMapper.selectById(1);
            LOG.info("User: {}", user);
        } catch (Exception e) {
            LOG.error("", e);
        }
    }
}
