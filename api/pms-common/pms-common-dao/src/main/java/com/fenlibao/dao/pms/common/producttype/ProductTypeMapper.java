package com.fenlibao.dao.pms.common.producttype;

import com.fenlibao.model.pms.common.KV;
import com.fenlibao.model.pms.common.producttype.ProductType;

import java.util.List;

/**
 * 产品类型
 *
 * Created by chenzhixuan on 2016/4/28.
 */
public interface ProductTypeMapper {
    List<KV<String, String>> getProductTypes();

    ProductType getProductType(String code);
}
