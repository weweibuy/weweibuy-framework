package com.weweibuy.framework.common.core.exception;


import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;

import static com.weweibuy.framework.common.core.utils.ResponseCodeUtils.newResponseCodeAndMsg;

/**
 * 系统级别异常
 *
 * @author durenhao
 * @date 2019/5/18 23:34
 **/
public class SystemException extends RuntimeException {

    private ResponseCodeAndMsg codeAndMsg;

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String code, String msg) {
        super(msg);
        this.codeAndMsg = newResponseCodeAndMsg(code, msg);
    }

    public SystemException(String msg) {
        super(msg);
        this.codeAndMsg = newResponseCodeAndMsg(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public SystemException(String msg, Throwable cause) {
        super(msg, cause);
        this.codeAndMsg = newResponseCodeAndMsg(CommonErrorCodeEum.UNKNOWN_EXCEPTION.getCode(), msg);
    }

    public SystemException(ResponseCodeAndMsg codeAndMsg) {
        super(codeAndMsg.getMsg());
        this.codeAndMsg = codeAndMsg;
    }

    public SystemException(ResponseCodeAndMsg codeAndMsg, Throwable cause) {
        super(codeAndMsg.getMsg(), cause);
        this.codeAndMsg = codeAndMsg;
    }


    public ResponseCodeAndMsg getCodeAndMsg() {
        return codeAndMsg;
    }
}

