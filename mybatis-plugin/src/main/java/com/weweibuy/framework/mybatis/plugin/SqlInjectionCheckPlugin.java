package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * sql 注入校验
 * <p>
 * orderByClause 与 like 增加sql注入校验
 *
 * @author durenhao
 * @date 2023/2/25 20:51
 **/
public class SqlInjectionCheckPlugin extends BasePlugin {


    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        overrideSetOrderByClauseMethod(topLevelClass, introspectedTable);
        return true;
    }

    private void overrideSetOrderByClauseMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        boolean match = topLevelClass.getImportedTypes().stream()
                .anyMatch(f -> f.getFullyQualifiedName().equals("com.weweibuy.framework.common.db.utils.SqlUtils"));
        if (!match) {
            topLevelClass.addImportedType("com.weweibuy.framework.common.db.utils.SqlUtils");
        }

        modifySetOrderByClauseMethod(topLevelClass.getMethods());

        topLevelClass.getInnerClasses().stream()
                .filter(ic -> "GeneratedCriteria".equals(ic.getType().getShortName()))
                .forEach(ic -> modifyLikeMethod(ic.getMethods()));
    }

    private void modifyLikeMethod(List<Method> methods) {
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            String methodName = method.getName();
            if (methodName.startsWith("and") && methodName.endsWith("Like")) {
                List<String> bodyLines = method.getBodyLines();
                bodyLines.add(0, "value = SqlUtils.containsSqlInjectionForLikeAndThrow(value);");
                System.err.println(bodyLines);
            }
        }
    }

    private void modifySetOrderByClauseMethod(List<Method> methods) {
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            if (method.getName().equals("setOrderByClause")) {
                List<String> bodyLines = method.getBodyLines();
                bodyLines.set(0, " this.orderByClause = "
                        + "SqlUtils.containsSqlInjectionAndThrow(orderByClause);");
            }
        }
    }

}
