package com.weweibuy.framework.samples;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;
import java.util.ListIterator;

/**
 * @author durenhao
 * @date 2020/9/14 20:18
 **/
public class Test01 {

    @Test
    public void test01() {
    }


    public void list(List<String> stringList) {
        ListIterator<String> stringListIterator = stringList.listIterator();
        while (stringListIterator.hasNext()) {
            String next = stringListIterator.next();
            if (next.hashCode() % 2 == 0) {
                stringListIterator.remove();
            }
        }
    }

    public void updateUserInfo(String newName, String newIdentity, Long id) throws Exception {
        String oldName = "";//数据库中读出的姓名
        String oldIdentity = "";//数据库中读出的身份证号

        //  原数据库中 oldIdentity 不为空
        //  请在合适的条件下校验姓名身份证
        if ((StringUtils.isNotBlank(newIdentity))) {
            // 校验identity格式及name和identity的一致性；如果校验失败则抛出异常
            String name = StringUtils.isNotBlank(newName) ? newName : oldName;
            check(name, newIdentity);
        }
    }

    /**
     * 外部系统
     *
     * @param newName
     * @param newIdentity
     */
    public void check(String newName, String newIdentity) {
        // 查询newIdentity 姓名  如果姓名不为空且改 姓名与 newName 不一致

    }

}
