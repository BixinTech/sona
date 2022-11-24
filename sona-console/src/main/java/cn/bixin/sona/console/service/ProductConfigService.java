package cn.bixin.sona.console.service;

import cn.bixin.sona.console.domain.db.ProductConfig;

import java.util.Collection;
import java.util.List;

public interface ProductConfigService{

    ProductConfig selectByProductCode(String productCode);


    List<ProductConfig> selectByProductCodes(Collection<String> productCodeCollection);
}
