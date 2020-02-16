package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.annotation.EnableCompensate;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * @author durenhao
 * @date 2020/2/13 21:24
 **/
public class AbstractCompensateConfig implements ImportAware {

    @Nullable
    protected AnnotationAttributes enableCompensate;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableCompensate = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableCompensate.class.getName(), false));
        if (this.enableCompensate == null) {
            throw new IllegalArgumentException(
                    "@EnableCompensate is not present on importing class " + importMetadata.getClassName());
        }
    }

    public AnnotationAttributes getEnableCompensate() {
        return enableCompensate;
    }
}
