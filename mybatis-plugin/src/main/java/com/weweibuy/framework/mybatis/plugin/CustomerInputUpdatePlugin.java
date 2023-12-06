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
 * 增加一个自定义的更新
 * 如:   create_time = now()
 * amount = amount + 1
 *
 * @author durenhao
 * @date 2023/2/25 20:51
 **/
public class CustomerInputUpdatePlugin extends BasePlugin {

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        addAndRightPosition(element.getElements());
        return super.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(
            XmlElement element, IntrospectedTable introspectedTable) {
        addAndRightPosition(element.getElements());
        return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }


    private XmlElement buildGroupByElement() {
        XmlElement updateSqlElement = new XmlElement("if");
        updateSqlElement.addAttribute(new Attribute("test", "example.updateSql != null"));
        updateSqlElement.addElement(new TextElement("${example.updateSql},"));
        return updateSqlElement;
    }


    private void addAndRightPosition(List<Element> topElements) {
        topElements.stream()
                .filter(e -> e instanceof XmlElement && "set".equals(((XmlElement) e).getName()))
                .map(e -> ((XmlElement) e).getElements())
                .filter(e -> e != null && !e.isEmpty())
                .findFirst()
                .ifPresent(e -> e.add(buildGroupByElement()));
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        updateSqlField(topLevelClass, introspectedTable);
        addCustomerUpdateMethodToExample(topLevelClass, introspectedTable);
        return true;
    }

    private void addCustomerUpdateMethodToExample(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        // 添加updateSql
        Method updateSqlMethod = JavaElementGeneratorTools.generateMethod(
                "updateSql",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "updateSqlClause")
        );
        commentGenerator.addGeneralMethodComment(updateSqlMethod, introspectedTable);

        updateSqlMethod = JavaElementGeneratorTools.generateMethodBody(
                updateSqlMethod,
                "this.setUpdateSql(updateSqlClause);",
                "return this;"
        );
        FormatTools.addMethodWithBestPosition(topLevelClass, updateSqlMethod);
        logger.debug("itfsw(Example增强插件):" + topLevelClass.getType().getShortName() + "增加方法updateSqlClause");

        // 添加orderBy
        Method orderByMethod1 = JavaElementGeneratorTools.generateMethod(
                "updateSql",
                JavaVisibility.PUBLIC,
                topLevelClass.getType(),
                new Parameter(FullyQualifiedJavaType.getStringInstance(), "updateSqlClauses", true)
        );

        commentGenerator.addGeneralMethodComment(orderByMethod1, introspectedTable);
        orderByMethod1 = JavaElementGeneratorTools.generateMethodBody(
                orderByMethod1,
                "StringBuffer sb = new StringBuffer();",
                "for (int i = 0; i < updateSqlClauses.length; i++) {",
                "sb.append(updateSqlClauses[i]);",
                "if (i < updateSqlClauses.length - 1) {",
                "sb.append(\" , \");",
                "}",
                "}",
                "this.setUpdateSql(sb.toString());",
                "return this;"
        );

        FormatTools.addMethodWithBestPosition(topLevelClass, orderByMethod1);
        logger.debug("itfsw(Example增强插件):" + topLevelClass.getType().getShortName() + "增加方法updateSqlClauses(String ... updateSqlClauses)");
    }


    private void updateSqlField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        boolean match = topLevelClass.getImportedTypes().stream()
                .anyMatch(f -> f.getFullyQualifiedName().equals("com.weweibuy.framework.common.db.utils.SqlUtils"));
        if (!match) {
            topLevelClass.addImportedType("com.weweibuy.framework.common.db.utils.SqlUtils");
        }


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
