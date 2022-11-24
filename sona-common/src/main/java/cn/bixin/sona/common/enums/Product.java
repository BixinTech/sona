package cn.bixin.sona.common.enums;

import org.apache.commons.lang.StringUtils;

public enum Product {

    TEST_PRODUCT("test");

    private final String code;

    Product(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Product getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (Product each : Product.values()) {
            if (each.getCode().equalsIgnoreCase(code)) {
                return each;
            }
        }

        return null;
    }

    public static Product getPlatformByName(String productCode) {
        if (StringUtils.isBlank(productCode)) {
            return null;
        }

        for (Product each : Product.values()) {
            if (each.name().equalsIgnoreCase(productCode)) {
                return each;
            }
        }

        return null;
    }
}
