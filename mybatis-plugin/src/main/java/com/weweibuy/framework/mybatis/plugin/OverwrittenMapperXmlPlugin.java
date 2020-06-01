package com.weweibuy.framework.mybatis.plugin;

import com.itfsw.mybatis.generator.plugins.utils.BasePlugin;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;

import java.io.File;

/**
 * 重写 mapper.xml 插件
 *
 * @author durenhao
 * @date 2020/5/30 22:03
 **/
@Slf4j
public class OverwrittenMapperXmlPlugin extends BasePlugin {


    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        String dir = sqlMap.getTargetPackage();
        if (dir.contains(".")) {
            dir = dir.replace(".", File.separator);
        }

        String fileName = sqlMap.getTargetProject() + File.separator + dir + File.separator + sqlMap.getFileName();
        File file = new File(fileName);

        if (file.exists()) {
            log.warn("Existing file {}  was overwritten ", file);
            if (!file.delete()) {
                log.warn("覆盖原有xml文件: {} 失败!", fileName);
            }
        }

        super.sqlMapGenerated(sqlMap, introspectedTable);
        return true;
    }

}
