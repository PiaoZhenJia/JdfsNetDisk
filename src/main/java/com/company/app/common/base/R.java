package com.company.app.common.base;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
@NoArgsConstructor
@Data
@ApiModel("通用返回结果对象")
public class R<T> {
    // 状态
    private Integer status = 200;
    // 提示内容
    private String message = "success";
    // 业务结果数据
    private T dataValue = null;

    public R(Integer status) {
        this.status = status;
    }

    public R(String message) {
        this.message = message;
    }

    public R(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public R setDataValue(T dataValue) {
        this.dataValue = dataValue;
        return this;
    }
}

