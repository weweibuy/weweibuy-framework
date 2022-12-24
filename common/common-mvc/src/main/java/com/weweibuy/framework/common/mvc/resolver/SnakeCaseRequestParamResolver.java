package com.weweibuy.framework.common.mvc.resolver;

import com.weweibuy.framework.common.mvc.resolver.annotation.SnakeCaseRequestParamBody;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.ServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * {@link ModelAttributeMethodProcessor}
 *
 * @author durenhao
 * @date 2020/2/17 21:59
 **/
public class SnakeCaseRequestParamResolver implements HandlerMethodArgumentResolver {

    private WebBindingInitializer webBindingInitializer;

    public SnakeCaseRequestParamResolver() {
    }

    public SnakeCaseRequestParamResolver(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SnakeCaseRequestParamBody.class);
    }


    @Override
    @Nullable
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

        Assert.state(mavContainer != null, "ModelAttributeMethodProcessor requires ModelAndViewContainer");
        Assert.state(binderFactory != null, "ModelAttributeMethodProcessor requires WebDataBinderFactory");

        String name = ModelFactory.getNameForParameter(parameter);
        ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
        if (ann != null) {
            mavContainer.setBinding(name, ann.binding());
        }

        Object attribute = null;
        BindingResult bindingResult = null;

        if (mavContainer.containsAttribute(name)) {
            attribute = mavContainer.getModel().get(name);
        } else {
            // Create attribute instance
            try {
                attribute = createAttribute(name, parameter, binderFactory, webRequest);
            } catch (BindException ex) {
                if (isBindExceptionRequired(parameter)) {
                    // No BindingResult parameter -> fail with BindException
                    throw ex;
                }
                // Otherwise, expose null/empty value and associated BindingResult
                if (parameter.getParameterType() == Optional.class) {
                    attribute = Optional.empty();
                }
                bindingResult = ex.getBindingResult();
            }
        }

        if (bindingResult == null) {
            // Bean property binding and validation;
            // skipped in case of binding failure on construction.
//            WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);

            SnakeCaseServletRequestDataBinder binder = new SnakeCaseServletRequestDataBinder(attribute, name);
            if (webBindingInitializer != null) {
                webBindingInitializer.initBinder(binder);
            }

            if (binder.getTarget() != null) {
                if (!mavContainer.isBindingDisabled(name)) {
                    bindRequestParameters(binder, webRequest);
                }
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
            // Value type adaptation, also covering java.util.Optional
            if (!parameter.getParameterType().isInstance(attribute)) {
                attribute = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);
            }
            bindingResult = binder.getBindingResult();
        }

        // Add resolved attribute and BindingResult at the end of the model
        Map<String, Object> bindingResultModel = bindingResult.getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return attribute;
    }


    protected boolean isBinderMethodApplicable(HandlerMethod initBinderMethod, WebDataBinder dataBinder) {
        InitBinder ann = initBinderMethod.getMethodAnnotation(InitBinder.class);
        Assert.state(ann != null, "No InitBinder annotation");
        String[] names = ann.value();
        return (ObjectUtils.isEmpty(names) || ObjectUtils.containsElement(names, dataBinder.getObjectName()));
    }


    protected Object createAttribute2(String attributeName, MethodParameter parameter,
                                      WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {

        MethodParameter nestedParameter = parameter.nestedIfOptional();
        Class<?> clazz = nestedParameter.getNestedParameterType();

        Constructor<?> ctor = BeanUtils.findPrimaryConstructor(clazz);
        if (ctor == null) {
            Constructor<?>[] ctors = clazz.getConstructors();
            if (ctors.length == 1) {
                ctor = ctors[0];
            } else {
                try {
                    ctor = clazz.getDeclaredConstructor();
                } catch (NoSuchMethodException ex) {
                    throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
                }
            }
        }

        Object attribute = constructAttribute(ctor, attributeName, parameter, binderFactory, webRequest);
        if (parameter != nestedParameter) {
            attribute = Optional.of(attribute);
        }
        return attribute;
    }

    @SuppressWarnings("deprecation")
    protected Object constructAttribute(Constructor<?> ctor, String attributeName, MethodParameter parameter,
                                        WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {

        if (ctor.getParameterCount() == 0) {
            // A single default constructor -> clearly a standard JavaBeans arrangement.
            return BeanUtils.instantiateClass(ctor);
        }
        throw new IllegalArgumentException(ctor.toString() + " 必须有空参构造");
    }


    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
        Assert.state(servletRequest != null, "No ServletRequest");
        ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
        servletBinder.bind(servletRequest);
    }


    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        for (Annotation ann : parameter.getParameterAnnotations()) {
            Object[] validationHints = determineValidationHints(ann);
            if (validationHints != null) {
                binder.validate(validationHints);
                break;
            }
        }
    }

    protected void validateValueIfApplicable(WebDataBinder binder, MethodParameter parameter,
                                             Class<?> targetType, String fieldName, @Nullable Object value) {

        for (Annotation ann : parameter.getParameterAnnotations()) {
            Object[] validationHints = determineValidationHints(ann);
            if (validationHints != null) {
                for (Validator validator : binder.getValidators()) {
                    if (validator instanceof SmartValidator) {
                        try {
                            ((SmartValidator) validator).validateValue(targetType, fieldName, value,
                                    binder.getBindingResult(), validationHints);
                        } catch (IllegalArgumentException ex) {
                            // No corresponding field on the target class...
                        }
                    }
                }
                break;
            }
        }
    }

    @Nullable
    private Object[] determineValidationHints(Annotation ann) {
        Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
        if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
            Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
            if (hints == null) {
                return new Object[0];
            }
            return (hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
        }
        return null;
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        return isBindExceptionRequired(parameter);
    }

    protected boolean isBindExceptionRequired(MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getExecutable().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }


    protected final Object createAttribute(String attributeName, MethodParameter parameter,
                                           WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {

        String value = getRequestValueForAttribute(attributeName, request);
        if (value != null) {
            Object attribute = createAttributeFromRequestValue(
                    value, attributeName, parameter, binderFactory, request);
            if (attribute != null) {
                return attribute;
            }
        }

        return createAttribute2(attributeName, parameter, binderFactory, request);
    }

    @Nullable
    protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
        Map<String, String> variables = getUriTemplateVariables(request);
        String variableValue = variables.get(attributeName);
        if (StringUtils.hasText(variableValue)) {
            return variableValue;
        }
        String parameterValue = request.getParameter(attributeName);
        if (StringUtils.hasText(parameterValue)) {
            return parameterValue;
        }
        return null;
    }

    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables = (Map<String, String>) request.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return (variables != null ? variables : Collections.emptyMap());
    }

    protected Object createAttributeFromRequestValue(String sourceValue, String attributeName,
                                                     MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request)
            throws Exception {

        DataBinder binder = binderFactory.createBinder(request, null, attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(parameter);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
            }
        }
        return null;
    }


}
