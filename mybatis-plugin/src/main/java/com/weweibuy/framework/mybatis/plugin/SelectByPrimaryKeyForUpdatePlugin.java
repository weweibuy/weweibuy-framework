
package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.*;
import com.itfsw.mybatis.generator.plugins.utils.hook.ISelectOneByExamplePluginHook;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * for update 插件
 */
public class SelectByPrimaryKeyForUpdatePlugin extends BasePlugin {
    public static final String METHOD_SELECT_PRIMARY_KEY_FOR_UPDATE = "selectByPrimaryKeyForUpdate";  // 方法名
    public static final String METHOD_SELECT_BY_PRIMARY_KEY_WITHBLOBS_FOR_UPDATE = "selectByPrimaryKeyWithBLOBsForUpdate";  // 方法名
    private XmlElement selectByPrimaryKeyForUpdateEle;
    private XmlElement selectByPrimaryKeyWithBLOBsForUpdateEle;

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
        // bug:26,27
        this.selectByPrimaryKeyForUpdateEle = null;
        this.selectByPrimaryKeyWithBLOBsForUpdateEle = null;
    }


    /**
     * Java Client Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType baseRecordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType("java.util.List");
        listType.addTypeArgument(baseRecordType);

        Method selectMethod = JavaElementGeneratorTools.generateMethod(
                METHOD_SELECT_BY_PRIMARY_KEY_WITHBLOBS_FOR_UPDATE,
                JavaVisibility.DEFAULT,
                listType,
                new Parameter(new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType()), "id")
        );
        commentGenerator.addGeneralMethodComment(selectMethod, introspectedTable);

        // hook
        if (PluginTools.getHook(ISelectOneByExamplePluginHook.class).clientSelectOneByExampleWithBLOBsMethodGenerated(selectMethod, interfaze, introspectedTable)) {
            // interface 增加方法
            FormatTools.addMethodWithBestPosition(interfaze, selectMethod);
            logger.debug("itfsw(SelectByPrimaryKeyForUpdate插件):" + interfaze.getType().getShortName() + "增加selectByPrimaryKeyWithBLOBsForUpdate方法。");
        }
        return super.clientSelectByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * Java Client Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {

        FullyQualifiedJavaType baseRecordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType("java.util.List");
        listType.addTypeArgument(baseRecordType);

        Method selectMethod = JavaElementGeneratorTools.generateMethod(
                METHOD_SELECT_PRIMARY_KEY_FOR_UPDATE,
                JavaVisibility.DEFAULT,
                listType,
                new Parameter(new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType()), "id")
        );
        commentGenerator.addGeneralMethodComment(selectMethod, introspectedTable);

        // hook
        if (PluginTools.getHook(ISelectOneByExamplePluginHook.class).clientSelectOneByExampleWithoutBLOBsMethodGenerated(selectMethod, interfaze, introspectedTable)) {
            // interface 增加方法
            FormatTools.addMethodWithBestPosition(interfaze, selectMethod);
            logger.debug("itfsw(SelectByPrimaryKeyForUpdate插件):" + interfaze.getType().getShortName() + "增加selectByPrimaryKeyForUpdate方法。");
        }
        return super.clientSelectByExampleWithoutBLOBsMethodGenerated(selectMethod, interfaze, introspectedTable);
    }

    /**
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // ------------------------------------ selectOneByExample ----------------------------------
        // 生成查询语句
        XmlElement selectOneElement = new XmlElement("select");
        // 添加注释(!!!必须添加注释，overwrite覆盖生成时，@see XmlFileMergerJaxp.isGeneratedNode会去判断注释中是否存在OLD_ELEMENT_TAGS中的一点，例子：@mbg.generated)
        commentGenerator.addComment(selectOneElement);

        // 添加ID
        selectOneElement.addAttribute(new Attribute("id", METHOD_SELECT_BY_PRIMARY_KEY_WITHBLOBS_FOR_UPDATE));
        // 添加返回类型
        selectOneElement.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        // 添加参数类型
        selectOneElement.addAttribute(new Attribute("parameterType", introspectedTable.getPrimaryKeyType()));
        selectOneElement.addElement(new TextElement("select"));

        XmlElement ifDistinctElement = new XmlElement("if");
        ifDistinctElement.addAttribute(new Attribute("test", "distinct"));
        ifDistinctElement.addElement(new TextElement("distinct"));
        selectOneElement.addElement(ifDistinctElement);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            selectOneElement.addElement(new TextElement(sb.toString()));
        }
        selectOneElement.addElement(XmlElementGeneratorTools.getBaseColumnListElement(introspectedTable));

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        selectOneElement.addElement(new TextElement(sb.toString()));
        selectOneElement.addElement(XmlElementGeneratorTools.getExampleIncludeElement(introspectedTable));

        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "orderByClause != null"));  //$NON-NLS-2$
        ifElement.addElement(new TextElement("order by ${orderByClause}"));
        selectOneElement.addElement(ifElement);

        // 只查询一条
        selectOneElement.addElement(new TextElement("for update"));

        this.selectByPrimaryKeyForUpdateEle = selectOneElement;
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    /**
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 生成查询语句
        XmlElement selectOneWithBLOBsElement = new XmlElement("select");
        // 添加注释(!!!必须添加注释，overwrite覆盖生成时，@see XmlFileMergerJaxp.isGeneratedNode会去判断注释中是否存在OLD_ELEMENT_TAGS中的一点，例子：@mbg.generated)
        commentGenerator.addComment(selectOneWithBLOBsElement);

        // 添加ID
        selectOneWithBLOBsElement.addAttribute(new Attribute("id", METHOD_SELECT_PRIMARY_KEY_FOR_UPDATE));
        // 添加返回类型
        selectOneWithBLOBsElement.addAttribute(new Attribute("resultMap", introspectedTable.getResultMapWithBLOBsId()));
        // 添加参数类型
        selectOneWithBLOBsElement.addAttribute(new Attribute("parameterType", introspectedTable.getPrimaryKeyType()));
        // 添加查询SQL
        selectOneWithBLOBsElement.addElement(new TextElement("select"));

        XmlElement ifDistinctElement = new XmlElement("if");
        ifDistinctElement.addAttribute(new Attribute("test", "distinct"));
        ifDistinctElement.addElement(new TextElement("distinct"));
        selectOneWithBLOBsElement.addElement(ifDistinctElement);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            selectOneWithBLOBsElement.addElement(new TextElement(sb.toString()));
        }

        selectOneWithBLOBsElement.addElement(XmlElementGeneratorTools.getBaseColumnListElement(introspectedTable));
        selectOneWithBLOBsElement.addElement(new TextElement(","));
        selectOneWithBLOBsElement.addElement(XmlElementGeneratorTools.getBlobColumnListElement(introspectedTable));

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        selectOneWithBLOBsElement.addElement(new TextElement(sb.toString()));
        selectOneWithBLOBsElement.addElement(XmlElementGeneratorTools.getExampleIncludeElement(introspectedTable));

        XmlElement ifElement1 = new XmlElement("if");
        ifElement1.addAttribute(new Attribute("test", "orderByClause != null"));  //$NON-NLS-2$
        ifElement1.addElement(new TextElement("order by ${orderByClause}"));
        selectOneWithBLOBsElement.addElement(ifElement1);

        // 只查询一条
        selectOneWithBLOBsElement.addElement(new TextElement(" for update"));


        this.selectByPrimaryKeyWithBLOBsForUpdateEle = selectOneWithBLOBsElement;
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        if (selectByPrimaryKeyForUpdateEle != null) {
            // hook
            if (PluginTools.getHook(ISelectOneByExamplePluginHook.class).sqlMapSelectOneByExampleWithoutBLOBsElementGenerated(document, selectByPrimaryKeyForUpdateEle, introspectedTable)) {
                // 添加到根节点
                FormatTools.addElementWithBestPosition(document.getRootElement(), selectByPrimaryKeyForUpdateEle);
                logger.debug("itfsw(SelectByPrimaryKeyForUpdate插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加sselectByPrimaryKeyForUpdate方法。");
            }
        }

        if (selectByPrimaryKeyWithBLOBsForUpdateEle != null) {
            // hook
            if (PluginTools.getHook(ISelectOneByExamplePluginHook.class).sqlMapSelectOneByExampleWithBLOBsElementGenerated(document, selectByPrimaryKeyWithBLOBsForUpdateEle, introspectedTable)) {
                // 添加到根节点
                FormatTools.addElementWithBestPosition(document.getRootElement(), selectByPrimaryKeyWithBLOBsForUpdateEle);
                logger.debug("itfsw(SelectByPrimaryKeyForUpdate插件):" + introspectedTable.getMyBatis3XmlMapperFileName() + "增加selectByPrimaryKeyWithBLOBsForUpdate方法。");
            }
        }

        return true;
    }


    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.clientSelectByPrimaryKeyMethodGenerated(method, topLevelClass, introspectedTable);
    }
}
