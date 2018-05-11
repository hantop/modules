package com.fenlibao.service.pms.common.producttype.impl;

import com.fenlibao.dao.pms.common.producttype.ProductTypeMapper;
import com.fenlibao.model.pms.common.KV;
import com.fenlibao.model.pms.common.producttype.ProductType;
import com.fenlibao.service.pms.common.producttype.ProductTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品类型
 *
 * Created by chenzhixuan on 2016/4/28.
 */
@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Resource
    private ProductTypeMapper productTypeMapper;

    @Override
    public List<KV<String, String>> getProductTypes() {
        return productTypeMapper.getProductTypes();
    }

    @Override
    public ProductType getProductType(String code) {
        return productTypeMapper.getProductType(code);
    }

}
