package com.qushihan.check_work_system.clazz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 删除班级状态枚举类
 */
@AllArgsConstructor
public enum DeleteClazzStatus {

    /**
     * 删除成功
     */
    DELETE_SUCCESS(0, "删除成功")
    ;

    @Getter
    private Integer code;

    @Getter
    private String message;
}
