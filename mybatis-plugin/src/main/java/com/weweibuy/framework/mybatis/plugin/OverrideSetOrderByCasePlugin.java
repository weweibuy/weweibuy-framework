package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import com.itfsw.mybatis.generator.plugins.utils.FormatTools;
import com.itfsw.mybatis.generator.plugins.utils.JavaElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * @author durenhao
 * @date 2023/2/25 20:51
 **/
public class OverrideSetOrderByCasePlugin extends BasePlugin {


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

        List<Method> methods = topLevelClass.getMethods();
        for (int i = 0; i < methods.size(); i++) {
            Method method = methods.get(i);
            if (method.getName().equals("setOrderByClause")) {
                List<String> bodyLines = method.getBodyLines();
                bodyLines.set(0 ," this.orderByClause = "
                        + "SqlUtils.containsSqlInjectionAndThrow(orderByClause);");
            }
        }
    }


    private void updateSqlField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        topLevelClass.addImportedType("com.weweibuy.framework.common.db.utils.SqlUtils");

        // 添加offset和rows字段
        Field offsetField = JavaElementGeneratorTools.generateField(
                "updateSql",
                JavaVisibility.PROTECTED,
                FullyQualifiedJavaType.getStringInstance(),
                null
        );
        commentGenerator.addFieldComment(offsetField, introspectedTable);
        topLevelClass.addField(offsetField);

        Method getterMethod = JavaElementGeneratorTools.generateGetterMethod(offsetField);

        Method setterMethod = JavaElementGeneratorTools.generateMethod(
                "set" + FormatTools.upFirstChar(offsetField.getName()),
                JavaVisibility.PUBLIC,
                null,
                new Parameter(offsetField.getType(), offsetField.getName())
        );
        JavaElementGeneratorTools.generateMethodBody(setterMethod, "this." + offsetField.getName() + " = "
                + "SqlUtils.containsSqlInjectionAndThrow(" + offsetField.getName() + ");");


        commentGenerator.addGeneralMethodComment(setterMethod, introspectedTable);
        commentGenerator.addGeneralMethodComment(getterMethod, introspectedTable);
        FormatTools.addMethodWithBestPosition(topLevelClass, setterMethod);
        FormatTools.addMethodWithBestPosition(topLevelClass, getterMethod);

    }

}
