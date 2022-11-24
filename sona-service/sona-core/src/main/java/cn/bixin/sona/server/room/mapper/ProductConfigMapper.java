package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.ProductConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductConfigMapper {

    ProductConfig findByProductCode(@Param("productCode") String productCode);

    ProductConfig findByShortCode(@Param("shortCode") String shortCode);

    int insertSelective(ProductConfig record);

}
