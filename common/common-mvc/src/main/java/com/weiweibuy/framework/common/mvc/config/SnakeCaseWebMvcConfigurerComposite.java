package com.weiweibuy.framework.common.mvc.config;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/18 10:24
 **/
public class SnakeCaseWebMvcConfigurerComposite implements WebMvcConfigurer {

    private final List<WebMvcConfigurer> delegates = new ArrayList<>();


    public void addWebMvcConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }


    @Override
    @Nullable
    public MessageCodesResolver getMessageCodesResolver() {
        MessageCodesResolver selected = null;
        for (WebMvcConfigurer configurer : this.delegates) {
            MessageCodesResolver messageCodesResolver = configurer.getMessageCodesResolver();
            if (messageCodesResolver != null) {
                if (selected != null) {
                    throw new IllegalStateException("No unique MessageCodesResolver found: {" +
                            selected + ", " + messageCodesResolver + "}");
                }
                selected = messageCodesResolver;
            }
        }
        return selected;
    }

    @Override
    public Validator getValidator() {
        Validator selected = null;
        for (WebMvcConfigurer configurer : this.delegates) {
            Validator validator = configurer.getValidator();
            if (validator != null) {
                if (selected != null) {
                    throw new IllegalStateException("No unique Validator found: {" +
                            selected + ", " + validator + "}");
                }
                selected = validator;
            }
        }
        return selected;
    }

}
