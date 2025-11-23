package com.spike.springcloud.txn.seata.infra.dao;

import com.spike.springcloud.txn.seata.domain.model.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AccountMapper {

    @Select("        select id, user_id, money\n" +
            "        from account_tbl\n" +
            "        WHERE user_id = #{userId}")
    Account selectByUserId(@Param("userId") String userId);

    @Update("        update account_tbl\n" +
            "        set money = #{money}\n" +
            "        where id = #{id}")
    int updateById(Account record);

}