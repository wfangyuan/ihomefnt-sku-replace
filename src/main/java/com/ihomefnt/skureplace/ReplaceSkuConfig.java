package com.ihomefnt.skureplace;

import lombok.*;

import java.util.Set;

/**
 * 基础数据类.这里的排序和excel里面的排序一致
 *
 * @author Jiaju Zhuang
 **/
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ReplaceSkuConfig {

    private Set<Sku> configList;

    @Data
    public static class Sku {
        private Long skuId;
        private Set<Long> replaceSkuIds;
    }

}
