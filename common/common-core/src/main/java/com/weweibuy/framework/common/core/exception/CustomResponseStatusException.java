package com.weweibuy.framework.common.core.exception;

import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author durenhao
 * @date 2021/2/9 20:43
 **/
@Getter
public class CustomResponseStatusException extends RuntimeException {

    private final HttpStatus status;

    private final ResponseCodeAndMsg codeAndMsg;

    public CustomResponseStatusException(HttpStatus status, ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg());
        this.status = status;
        this.codeAndMsg = codeAndMsg;
    }


    public CustomResponseStatusException(Throwable cause, HttpStatus status, ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg(), cause);
        this.status = status;
        this.codeAndMsg = codeAndMsg;
    }
}
