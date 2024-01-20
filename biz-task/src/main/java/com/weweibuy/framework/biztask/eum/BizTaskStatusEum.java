package com.weweibuy.framework.biztask.eum;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author durenhao
 * @date 2024/1/19 21:25
 **/
@Getter
@RequiredArgsConstructor
public enum BizTaskStatusEum {

    WAIT_EXEC(1, "待执行"),

    EXEC_ING(2, "执行中"),

    SUCCESS(3, "执行成功"),

    FAIL(4, "执行失败"),

    ;

    private final Integer code;

    private final String name;


    public static BizTaskStatusEum fromCodeOrThrow(Integer code) {
        return fromCode(code).orElseThrow(() -> Exceptions.business("任务状态错误"));
    }

    public static Optional<BizTaskStatusEum> fromCode(Integer code) {
        return Arrays.stream(BizTaskStatusEum.values())
                .filter(a -> a.getCode().equals(code))
                .findFirst();
    }

}
