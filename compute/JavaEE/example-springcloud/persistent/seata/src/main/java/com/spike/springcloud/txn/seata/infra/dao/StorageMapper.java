package com.spike.springcloud.txn.seata.infra.dao;

import com.spike.springcloud.txn.seata.domain.model.Storage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StorageMapper {

    @Select("        select id, commodity_code, count\n" +
            "        from stock_tbl\n" +
            "        WHERE id = #{id}")
    Storage selectById(@Param("id") Integer id);

    @Select("        select id, commodity_code commodityCode, count\n" +
            "        from stock_tbl\n" +
            "        WHERE commodity_code = #{commodityCode}")
    Storage findByCommodityCode(@Param("commodityCode") String commodityCode);

    @Update("        update stock_tbl\n" +
            "        set count = #{count}\n" +
            "        WHERE id = #{id}")
    int updateById(Storage record);

    void insert(Storage record);

    void insertBatch(List<Storage> records);

    int updateBatch(@Param("list") List<Long> ids, @Param("commodityCode") String commodityCode);
}