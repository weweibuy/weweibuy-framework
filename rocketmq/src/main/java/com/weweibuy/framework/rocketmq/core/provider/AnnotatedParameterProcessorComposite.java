package com.weweibuy.framework.rocketmq.core.provider;

import java.util.LinkedList;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/20 21:51
 **/
public class AnnotatedParameterProcessorComposite {

    private final List<AnnotatedParameterProcessor> resolvers = new LinkedList<>();


    public void addProcessors(List<AnnotatedParameterProcessor> annotatedParameterProcessors) {
        resolvers.addAll(annotatedParameterProcessors);
    }

    public void addProcessor(AnnotatedParameterProcessor annotatedParameterProcessor) {
        resolvers.add(annotatedParameterProcessor);
    }

    public List<AnnotatedParameterProcessor> getParameterProcessors() {
        return resolvers;
    }

}
