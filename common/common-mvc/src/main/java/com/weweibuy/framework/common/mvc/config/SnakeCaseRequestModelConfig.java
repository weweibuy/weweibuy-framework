package com.weweibuy.framework.common.mvc.config;

import com.weweibuy.framework.common.mvc.resolver.SnakeCaseRequestParamResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.validation.ValidatorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/18 10:17
 **/
@Configuration
public class SnakeCaseRequestModelConfig implements WebMvcConfigurer {

    private final SnakeCaseWebMvcConfigurerComposite configurers = new SnakeCaseWebMvcConfigurerComposite();

    @Qualifier("mvcConversionService")
    @Autowired
    private FormattingConversionService conversionService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addWebMvcConfigurers(configurers);
        }
    }

    public SnakeCaseRequestParamResolver snakeCaseRequestParamResolver() {
        return new SnakeCaseRequestParamResolver(getConfigurableWebBindingInitializer());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(snakeCaseRequestParamResolver());
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return this.configurers.getMessageCodesResolver();
    }


    public ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {

        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(conversionService);

        Validator validator = ValidatorAdapter.get(applicationContext, configurers.getValidator());

        initializer.setValidator(validator);
        MessageCodesResolver messageCodesResolver = getMessageCodesResolver();
        if (messageCodesResolver != null) {
            initializer.setMessageCodesResolver(messageCodesResolver);
        }
        return initializer;
    }


}
