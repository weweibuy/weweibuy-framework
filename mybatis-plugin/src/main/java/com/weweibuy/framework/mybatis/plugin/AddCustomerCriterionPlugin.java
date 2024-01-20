package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import com.itfsw.mybatis.generator.plugins.utils.FormatTools;
import com.itfsw.mybatis.generator.plugins.utils.JavaElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * 增加一个自定义的where条件
 *
 * Criteria.addCustomerCriterion(String condition)
 *
 * @author durenhao
 * @date 2024/1/20 10:01
 **/
public class AddCustomerCriterionPlugin extends BasePlugin {


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

        addCustomerCriterionMethod(topLevelClass, introspectedTable);
    }

    private void addCustomerCriterionMethod(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // Criteria

        List<InnerClass> innerClasses = topLevelClass.getInnerClasses();
        InnerClass criteria = innerClasses.stream()
                .filter(i -> i.getType().getFullyQualifiedName().equals("Criteria"))
                .findFirst()
                .orElse(null);
        if (criteria == null) {
            return;
        }

        // 添加andCustomerCriterion方法
        Method andCustomerCriterion = JavaElementGeneratorTools.generateMethod(
                "addCustomerCriterion",
                JavaVisibility.PUBLIC,
                criteria.getType(),
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "condition")
        );
        commentGenerator.addGeneralMethodComment(andCustomerCriterion, introspectedTable);

        andCustomerCriterion = JavaElementGeneratorTools.generateMethodBody(
                andCustomerCriterion,
                "condition = SqlUtils.containsSqlInjectionForLikeAndThrow(condition);",
                "this.addCriterion(condition);",
                "return this;"
        );
        FormatTools.addMethodWithBestPosition(criteria, andCustomerCriterion);

    }


}
