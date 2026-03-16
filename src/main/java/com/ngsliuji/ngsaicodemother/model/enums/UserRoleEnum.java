package com.ngsliuji.ngsaicodemother.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("普通用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
//    其中，getEnumBy؜V؜a؜lue 是؜通过؜ v؜alue 找到具体的枚举对象
//     如果枚举值特别多，؜可؜以؜ Map ؜缓存؜所有؜枚举值来加速查找，而不是遍历列表。
    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum item : values()) {
            if (item.value.equals(value)) {
                return item;
            }
        }
            return null;
        }
    }
