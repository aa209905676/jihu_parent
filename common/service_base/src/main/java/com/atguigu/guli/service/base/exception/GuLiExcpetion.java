package com.atguigu.guli.service.base.exception;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import lombok.Data;

@Data
public class GuLiExcpetion  extends RuntimeException{
    private Integer code;

    public GuLiExcpetion(String message,Integer code) {
        super(message);
        this.code = code;
    }

    public GuLiExcpetion(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuLiExcpetion{" +
                "code=" + code +
                "message=" + this.getMessage()+
                '}';
    }
}
