package cn.bixin.sona.console.service.impl;

import cn.bixin.sona.console.domain.db.ProductConfig;
import cn.bixin.sona.console.mapper.ProductConfigMapper;
import cn.bixin.sona.console.service.ProductConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@Service
public class ProductConfigServiceImpl implements ProductConfigService {

    @Resource
    private ProductConfigMapper productConfigMapper;


    @Override
    public ProductConfig selectByProductCode(String productCode) {
        return productConfigMapper.selectByProductCode(productCode);
    }

    @Override
    public List<ProductConfig> selectByProductCodes(Collection<String> productCodeCollection) {
        return productConfigMapper.selectByProductCodeIn(productCodeCollection);
    }
}
