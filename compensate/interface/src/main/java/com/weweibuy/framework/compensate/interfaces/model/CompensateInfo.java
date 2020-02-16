package com.weweibuy.framework.compensate.interfaces.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2020/2/13 20:09
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompensateInfo {

    private String compensateKey;

    private String bizId;

    private String methodArgs;

    /**
     * 下一次触发时间
     */
    private LocalDateTime nextTriggerTime;


    public static class CompensateInfoBuilder {

        private String compensateKey;

        private String bizId;

        private String methodArgs;

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

        public CompensateInfo build() {
            return new CompensateInfo(compensateKey, bizId, methodArgs, nextTriggerTime);
        }

    }

}
