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
 * @author durenhao
 * @date 2023/2/25 20:51
 **/
public class CustomerUpdatePlugin extends BasePlugin {


    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return super.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.clientUpdateByExampleSelectiveMethodGenerated(method, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        List<Element> elementsList = ((XmlElement) elements.get(1)).getElements();

        XmlElement ifUpdateSqlElement = new XmlElement("if");
        ifUpdateSqlElement.addAttribute(new Attribute("test", "updateSql != null"));
        ifUpdateSqlElement.addElement(new TextElement("${updateSql} ,"));
        elementsList.add(ifUpdateSqlElement);
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);

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
                "this.updateSql = updateSqlClause;",
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
                "this.updateSql = sb.toString();",
                "return this;"
        );

        FormatTools.addMethodWithBestPosition(topLevelClass, orderByMethod1);
        logger.debug("itfsw(Example增强插件):" + topLevelClass.getType().getShortName() + "增加方法updateSqlClauses(String ... updateSqlClauses)");
    }


    private void updateSqlField(TopLevelClass topLevelClass, IntrospectedTable introspectedTable){
        // 添加offset和rows字段
        Field offsetField = JavaElementGeneratorTools.generateField(
                "updateSql",
                JavaVisibility.PROTECTED,
                FullyQualifiedJavaType.getStringInstance(),
                null
        );
        commentGenerator.addFieldComment(offsetField, introspectedTable);
        topLevelClass.addField(offsetField);

        Method setterMethod = JavaElementGeneratorTools.generateSetterMethod(offsetField);
        Method getterMethod = JavaElementGeneratorTools.generateGetterMethod(offsetField);

        commentGenerator.addGeneralMethodComment(setterMethod, introspectedTable);
        commentGenerator.addGeneralMethodComment(getterMethod, introspectedTable);
        FormatTools.addMethodWithBestPosition(topLevelClass, setterMethod);
        FormatTools.addMethodWithBestPosition(topLevelClass, getterMethod);

    }

}
