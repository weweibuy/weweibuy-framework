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
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement groupByElement = buildGroupByElement();
        addAtRightPosition(element, groupByElement);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement groupByElement = buildGroupByElement();
        addAtRightPosition(element, groupByElement);
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }

    private XmlElement buildGroupByElement() {
        XmlElement groupByElement = new XmlElement("if");
        groupByElement.addAttribute(new Attribute("test", "groupByClause != null"));
        groupByElement.addElement(new TextElement("group by ${groupByClause}"));
        return groupByElement;
    }

    private void addAtRightPosition(XmlElement rootElement, XmlElement ifGroupByElement) {
        List<Element> elements = rootElement.getElements();
        Integer position = null;
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (element instanceof XmlElement && "if".equals(((XmlElement) element).getName())) {
                Element subElement = ((XmlElement) element).getElements().get(0);
                if (subElement instanceof XmlElement && "include".equals(((XmlElement) subElement).getName())) {
                    List<Attribute> attributeList = ((XmlElement) subElement).getAttributes();
                    if (attributeList != null && !attributeList.isEmpty()) {
                        Attribute attribute = attributeList.get(0);
                        if ("refid".equals(attribute.getName()) && "Example_Where_Clause".equals(attribute.getValue())) {
                            position = i;
                            break;
                        }
                    }
                }
            }
        }
        if (position != null) {
            rootElement.addElement(position + 1, ifGroupByElement);
        } else {
            warnings.add("itfsw:插件" + this.getClass().getTypeName() + "插件无法匹配到正确的sql位置");
        }


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
                "this.setGroupByClause(sb.toString());",
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
