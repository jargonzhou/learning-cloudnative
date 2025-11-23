package com.spike.springcloud.txn.seata.infra.dao;

import com.spike.springcloud.txn.seata.domain.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper {

    @Insert("        insert into order_tbl (user_id, commodity_code, count, money)\n" +
            "        values (#{userId}, #{commodityCode}, #{count},\n" +
            "        #{money})")
    int insert(Order record);

}