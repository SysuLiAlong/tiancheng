package com.tiancheng.ms.dao.mapper;

import com.tiancheng.ms.dto.ProduceDetailDTO;
import com.tiancheng.ms.entity.ProduceEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProduceMapper extends Mapper<ProduceEntity> {
    @Select("select code from produce order by create_time desc limit 1")
    String getLastProduce();

    ProduceDetailDTO getProduceDetail(@Param("produceId") Integer produceId);

    List<ProduceDetailDTO> pageQryProduceDetail(@Param("orderCode")String orderCode,
                                                @Param("orderParam") String orderParam,@Param("chargeUserName") String chargeUserName);

    @Select("select p1.* from produce p1 left join product p2 on p1.product_code = p2.code where p2.id = #{productId}")
    List<ProduceEntity> selectByProductId(@Param("productId") Integer productId);
}
