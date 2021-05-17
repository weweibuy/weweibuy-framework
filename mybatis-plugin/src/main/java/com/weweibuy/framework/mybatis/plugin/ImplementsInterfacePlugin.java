package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 实现接口插件
 *
 * @author durenhao
 * @date 2021/5/17 21:16
 **/
public class ImplementsInterfacePlugin extends BasePlugin {

    public static final String INTERFACE_PROPERTY = "interfaces";

    private List<String> interfaceList;

    @Override
    public boolean validate(List<String> warnings) {
        // 获取配置的目标package
        Properties properties = getProperties();
        String property = properties.getProperty(INTERFACE_PROPERTY);
        if (StringUtils.isBlank(property)) {
            warnings.add("ImplementsInterfacePlugin插件的实现接口(interfaces)！");
            return false;
        }
        this.interfaceList = Arrays.stream(property.split(","))
                .collect(Collectors.toList());
        return super.validate(warnings);
    }

    /**
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addInterface(topLevelClass, introspectedTable);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addInterface(topLevelClass, introspectedTable);
        return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.addInterface(topLevelClass, introspectedTable);
        return super.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable);
    }


    private void addInterface(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        Set<FullyQualifiedJavaType> superInterfaceTypes = topLevelClass.getSuperInterfaceTypes();
        Set<String> superInterfaceSet = superInterfaceTypes.stream()
                .map(FullyQualifiedJavaType::getFullyQualifiedName)
                .collect(Collectors.toSet());

        List<String> addInterfaceList = interfaceList.stream()
                .filter(i -> !superInterfaceSet.contains(i))
                .collect(Collectors.toList());

        addInterfaceList.forEach(i -> {
            FullyQualifiedJavaType listType = new FullyQualifiedJavaType(i);
            topLevelClass.addSuperInterface(listType);
            topLevelClass.addImportedType(i);
        });

    }

}
