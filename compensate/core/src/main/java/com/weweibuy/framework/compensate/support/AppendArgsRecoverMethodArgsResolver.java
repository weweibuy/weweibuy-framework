package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.interfaces.RecoverMethodArgsResolver;

/**
 * @author durenhao
 * @date 2020/2/15 22:19
 **/
public class AppendArgsRecoverMethodArgsResolver implements RecoverMethodArgsResolver {

    @Override
    public boolean match(String compensateKey) {
        return true;
    }

    @Override
    public Object[] resolver(Object returnArg, Object[] inputArg) {
        if (returnArg == null) {
            return inputArg;
        }
        if (inputArg == null || inputArg.length == 0) {
            return new Object[]{returnArg};
        }
        Object[] objects = new Object[inputArg.length + 1];
        for (int i = 0; i < inputArg.length; i++) {
            objects[i] = inputArg[i];
        }
        objects[inputArg.length] = returnArg;
        return objects;
    }
}
