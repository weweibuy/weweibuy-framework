package com.weweibuy.framework.common.metric;

import com.izettle.metrics.influxdb.tags.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Transformer 组合
 *
 * @author durenhao
 * @date 2020/7/5 21:07
 **/
public class TransformerComposite implements Transformer {

    private List<Transformer> transformerList = new ArrayList<>();

    @Override
    public Map<String, String> getTags(String metricName) {
        Map<String, String> hashMap = new HashMap<>();
        transformerList.forEach(t -> hashMap.putAll(t.getTags(metricName)));
        return hashMap;
    }

    public void addTagTransformer(Transformer transformer) {
        transformerList.add(transformer);
    }

    public void addTagTransformer(List<Transformer> transformerList) {
        transformerList.addAll(transformerList);
    }


}
