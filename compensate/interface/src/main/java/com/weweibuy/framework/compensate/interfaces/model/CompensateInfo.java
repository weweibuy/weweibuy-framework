package com.weweibuy.framework.compensate.interfaces.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 补偿信息
 *
 * @author durenhao
 * @date 2020/2/13 20:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompensateInfo {

    /**
     * 补偿key
     */
    private String compensateKey;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 方法参数
     */
    private String methodArgs;

    /**
     * 补偿类型  {@link BuiltInCompensateType}
     */
    private String compensateType;

    /**
     * 下一次触发时间
     */
    private LocalDateTime nextTriggerTime;


    public static class CompensateInfoBuilder {

        private String compensateKey;

        private String bizId;

        private String methodArgs;

        private String compensateType;

        private LocalDateTime nextTriggerTime;

        public CompensateInfoBuilder compensateKey(String compensateKey) {
            this.compensateKey = compensateKey;
            return this;
        }

        public CompensateInfoBuilder bizId(String bizId) {
            this.bizId = bizId;
            return this;
        }

        public CompensateInfoBuilder methodArgs(String methodArgs) {
            this.methodArgs = methodArgs;
            return this;
        }

        public CompensateInfoBuilder nextTriggerTime(LocalDateTime nextTriggerTime) {
            this.nextTriggerTime = nextTriggerTime;
            return this;
        }

        public CompensateInfoBuilder compensateType(String compensateType) {
            this.compensateType = compensateType;
            return this;
        }

        public CompensateInfo build() {
            return new CompensateInfo(compensateKey, bizId, methodArgs, compensateType, nextTriggerTime);
        }

    }

}
