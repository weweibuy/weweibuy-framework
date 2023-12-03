package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import com.itfsw.mybatis.generator.plugins.utils.FormatTools;
import com.itfsw.mybatis.generator.plugins.utils.JavaElementGeneratorTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * 增加 group by 插件
 *
 * @author durenhao
 * @date 2023/12/3 21:34
 **/
public class GroupByClausePlugin extends BasePlugin {


    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        List<Element> elementsList = ((XmlElement) elements.get(1)).getElements();
        XmlElement ifUpdateSqlElement = new XmlElement("if");
        ifUpdateSqlElement.addAttribute(new Attribute("test", "example.updateSql != null"));
        ifUpdateSqlElement.addElement(new TextElement("${example.updateSql} ,"));
        elementsList.add(ifUpdateSqlElement);
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        groupByCaseSqlField(topLevelClass, introspectedTable);
        addCustomerGroupByMethodToExample(topLevelClass, introspectedTable);
        return true;
    }

    private void addCustomerGroupByMethodToExample(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        // 添加updateSql
        Method groupBySqlMethod = JavaElementGeneratorTools.generateMethod(
                "groupBy",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "groupByClause")
        );
        commentGenerator.addGeneralMethodComment(groupBySqlMethod, introspectedTable);

        groupBySqlMethod = JavaElementGeneratorTools.generateMethodBody(
                groupBySqlMethod,
                "this.setGroupByClause(groupByClause);",
                "return this;"
        );
        FormatTools.addMethodWithBestPosition(topLevelClass, groupBySqlMethod);
        logger.debug("itfsw(Example增强插件):" + topLevelClass.getType().getShortName() + "增加方法groupByClause");

        // 添加orderBy
        Method orderByMethod1 = JavaElementGeneratorTools.generateMethod(
                "groupBy",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "groupByClauses", true)
        );
        commentGenerator.addGeneralMethodComment(orderByMethod1, introspectedTable);
        orderByMethod1 = JavaElementGeneratorTools.generateMethodBody(
                orderByMethod1,
                "StringBuffer sb = new StringBuffer();",
                "for (int i = 0; i < groupByClauses.length; i++) {",
                "sb.append(groupByClauses[i]);",
                "if (i < groupByClauses.length - 1) {",
                "sb.append(\" , \");",
                "}",
                "}",
                "this.setGroupBy(sb.toString());",
                "return this;"
        );

        FormatTools.addMethodWithBestPosition(topLevelClass, orderByMethod1);
        logger.debug("itfsw(Example增强插件):" + topLevelClass.getType().getShortName() + "增加方法groupByClauses(String ... groupByClauses)");
    }


    private void groupByCaseSqlField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        boolean match = topLevelClass.getImportedTypes().stream()
                .anyMatch(f -> f.getFullyQualifiedName().equals("com.weweibuy.framework.common.db.utils.SqlUtils"));
        if (!match) {
            topLevelClass.addImportedType("com.weweibuy.framework.common.db.utils.SqlUtils");
        }


        // 添加groupBy字段
        Field groupByField = JavaElementGeneratorTools.generateField(
                "groupByClause",
                JavaVisibility.PROTECTED,
                FullyQualifiedJavaType.getStringInstance(),
                null
        );
        commentGenerator.addFieldComment(groupByField, introspectedTable);
        topLevelClass.addField(groupByField);

        Method getterMethod = JavaElementGeneratorTools.generateGetterMethod(groupByField);

        Method setterMethod = JavaElementGeneratorTools.generateMethod(
                "set" + FormatTools.upFirstChar(groupByField.getName()),
                JavaVisibility.PUBLIC,
                null,
                new Parameter(groupByField.getType(), groupByField.getName())
        );
        JavaElementGeneratorTools.generateMethodBody(setterMethod, "this." + groupByField.getName() + " = "
                + "SqlUtils.containsSqlInjectionAndThrow(" + groupByField.getName() + ");");


        commentGenerator.addGeneralMethodComment(setterMethod, introspectedTable);
        commentGenerator.addGeneralMethodComment(getterMethod, introspectedTable);
        FormatTools.addMethodWithBestPosition(topLevelClass, setterMethod);
        FormatTools.addMethodWithBestPosition(topLevelClass, getterMethod);

    }

}
