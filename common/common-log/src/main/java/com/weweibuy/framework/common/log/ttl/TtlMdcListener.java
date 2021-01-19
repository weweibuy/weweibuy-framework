package com.weweibuy.framework.common.log.ttl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import org.slf4j.TtlMDCAdapter;

/**
 * @author durenhao
 * @date 2020/3/14 13:38
 **/
public class TtlMdcListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

    @Override
    public void start() {
        // 替换MDC实现
        if (userTtl()) {
            this.addInfo("load TtlMDCAdapter...");
            TtlMDCAdapter.getInstance();
        }
    }


    private boolean userTtl() {
        try {
            Class.forName("com.alibaba.ttl.TransmittableThreadLocal");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return Boolean.getBoolean("common.log.ttl.enable");
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext loggerContext) {
    }

    @Override
    public void onReset(LoggerContext loggerContext) {
    }

    @Override
    public void onStop(LoggerContext loggerContext) {
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}
