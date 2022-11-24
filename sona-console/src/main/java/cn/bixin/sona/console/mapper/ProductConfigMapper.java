package cn.bixin.sona.console.mapper;
import java.util.List;
import java.util.Collection;

import cn.bixin.sona.console.domain.db.ProductConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductConfigMapper {

    ProductConfig selectByProductCode(@Param("productCode")String productCode);

    List<ProductConfig> selectByProductCodeIn(@Param("productCodeCollection")Collection<String> productCodeCollection);


}