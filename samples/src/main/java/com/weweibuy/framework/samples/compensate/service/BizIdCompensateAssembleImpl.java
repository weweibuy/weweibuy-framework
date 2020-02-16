package com.weweibuy.framework.samples.compensate.service;

import com.weweibuy.framework.compensate.interfaces.BizIdCompensateAssemble;
import com.weweibuy.framework.samples.model.Dog;
import com.weweibuy.framework.samples.model.User;
import org.springframework.stereotype.Component;

/**
 * @author durenhao
 * @date 2020/2/16 20:21
 **/
@Component
public class BizIdCompensateAssembleImpl implements BizIdCompensateAssemble {

    @Override
    public Object[] assemble(String compensateKey, String bizId) {
        if (compensateKey.equals("CompensateSimpleService_2")) {
            // 根据bizId 查询组装数据
            User user = new User();
            Dog dog = new Dog();
            user.setName("Jack");
            user.setAge(12);
            dog.setName("tom");
            dog.setAge(12);
            user.setDog(dog);
            return new Object[]{user, dog};
        }
        return new Object[0];
    }
}
