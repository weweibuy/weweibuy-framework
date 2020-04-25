package com.weweibuy.framework.compensate.mybatis.store;

import com.weweibuy.framework.compensate.interfaces.CompensateConfigStore;
import com.weweibuy.framework.compensate.interfaces.CompensateStore;
import com.weweibuy.framework.compensate.interfaces.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfo;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.mybatis.mapper.CompensateMapper;
import com.weweibuy.framework.compensate.mybatis.po.Compensate;
import com.weweibuy.framework.compensate.mybatis.po.CompensateExample;
import com.weweibuy.framework.compensate.mybatis.po.CompensateMethodArgsExt;
import com.weweibuy.framework.compensate.mybatis.repository.CompensateRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author durenhao
 * @date 2020/2/16 12:22
 **/
public class JdbcCompensateStore implements CompensateStore, InitializingBean {

    @Autowired
    private CompensateMapper compensateMapper;

    @Autowired
    private CompensateRepository compensateRepository;

    @Autowired
    private CompensateConfigStore compensateConfigStore;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Integer compensateFieldLength = 8000;

    private Integer compensateExtFieldLength = 8000;


    @Override
    public int saveCompensateInfo(CompensateInfo compensateInfo) {
        String methodArgs = compensateInfo.getMethodArgs();
        if (methodArgs.length() > compensateFieldLength) {
            String substring = methodArgs.substring(0, compensateFieldLength);
            compensateInfo.setMethodArgs(substring);
            Compensate compensate = toCompensate(compensateInfo);
            compensate.setHasArgsExt(true);

            String substring1 = methodArgs.substring(compensateFieldLength, methodArgs.length());

            List<CompensateMethodArgsExt> collect = stringSpilt(substring1, compensateExtFieldLength).stream()
                    .map(m -> {
                        CompensateMethodArgsExt compensateMethodArgs = new CompensateMethodArgsExt();
                        compensateMethodArgs.setMethodArgs(m);
                        return compensateMethodArgs;
                    })
                    .collect(Collectors.toList());
            return compensateRepository.insertCompensate(compensate, collect);

        }
        Compensate compensate = toCompensate(compensateInfo);
        return compensateMapper.insertSelective(compensate);

    }

    @Override
    public Collection<CompensateInfoExt> queryCompensateInfo() {
        CompensateExample compensateExample = new CompensateExample();
        compensateExample.createCriteria().andIsDeleteEqualTo(false)
                .andNextTriggerTimeLessThan(LocalDateTime.now());
        List<Compensate> compensates = compensateMapper.selectByExample(compensateExample);

        Map<Boolean, List<Compensate>> listMap = compensates.stream()
                .collect(Collectors.groupingBy(c -> c.getHasArgsExt()));

        List<Compensate> hasExtCompensateList = listMap.get(true);
        if (CollectionUtils.isEmpty(hasExtCompensateList)) {
            return compensates.stream()
                    .map(this::toCompensateInfoExt)
                    .collect(Collectors.toList());
        }

        List<Long> idList = hasExtCompensateList.stream().map(Compensate::getId).collect(Collectors.toList());
        Map<Long, List<CompensateMethodArgsExt>> compensateMethodArgsExtMap = compensateRepository.selectExtByCompensateId(idList).stream()
                .collect(Collectors.groupingBy(CompensateMethodArgsExt::getCompensateId));

        Stream<Compensate> compensateStream = hasExtCompensateList.stream()
                .map(c -> {
                    List<CompensateMethodArgsExt> compensateMethodArgsExtList = compensateMethodArgsExtMap.get(c.getId());
                    if (CollectionUtils.isEmpty(compensateMethodArgsExtList)) {
                        return null;
                    }
                    String collect = compensateMethodArgsExtList.stream()
                            .sorted(Comparator.comparing(CompensateMethodArgsExt::getArgsOrder))
                            .map(CompensateMethodArgsExt::getMethodArgs)
                            .collect(Collectors.joining());
                    c.setMethodArgs(c.getMethodArgs() + collect);
                    return c;
                })
                .filter(Objects::nonNull);
        List<Compensate> compensateList = listMap.get(false);
        if (CollectionUtils.isEmpty(compensateList)) {
            compensateList = Collections.emptyList();
        }

        return Stream.concat(compensateList.stream(), compensateStream)
                .map(this::toCompensateInfoExt)
                .collect(Collectors.toList());
    }

    @Override
    public int updateCompensateInfo(String id, CompensateInfoExt compensateInfo) {
        Compensate compensate = toCompensate2(compensateInfo);
        compensate.setId(Long.valueOf(id));
        return compensateMapper.updateByPrimaryKeySelective(compensate);
    }

    @Override
    public int deleteCompensateInfo(String id) {
        Compensate compensate = new Compensate();
        compensate.setIsDelete(true);
        compensate.setId(Long.valueOf(id));
        return compensateMapper.updateByPrimaryKeySelective(compensate);
    }

    private CompensateInfoExt toCompensateInfoExt(Compensate compensate) {
        CompensateInfoExt infoExt = new CompensateInfoExt();
        infoExt.setId(compensate.getId() + "");
        infoExt.setAlarmCount(compensate.getAlarmCount());
        infoExt.setRetryCount(compensate.getRetryCount());
        infoExt.setBizId(compensate.getBizId());
        infoExt.setCompensateKey(compensate.getCompensateKey());
        infoExt.setUpdateTime(compensate.getUpdateTime());
        infoExt.setMethodArgs(compensate.getMethodArgs());
        CompensateConfigProperties properties = compensateConfigStore.compensateConfig(compensate.getCompensateKey());
        infoExt.setType(properties.getCompensateType());
        infoExt.setAlarmRule(properties.getAlarmRule());
        infoExt.setRetryRule(properties.getRetryRule());
        infoExt.setNextTriggerTime(compensate.getNextTriggerTime());
        return infoExt;
    }

    private Compensate toCompensate(CompensateInfo compensateInfo) {
        Compensate compensate = new Compensate();
        compensate.setCompensateKey(compensateInfo.getCompensateKey());
        compensate.setCompensateType(compensateInfo.getCompensateType());
        compensate.setBizId(compensateInfo.getBizId());
        compensate.setMethodArgs(compensateInfo.getMethodArgs());
        compensate.setNextTriggerTime(compensateInfo.getNextTriggerTime());
        return compensate;
    }

    private Compensate toCompensate2(CompensateInfoExt compensateInfo) {
        Compensate compensate = new Compensate();
        compensate.setRetryCount(compensateInfo.getRetryCount());
        compensate.setAlarmCount(compensateInfo.getAlarmCount());
        return compensate;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
        List<Map<String, Object>> compensateMapsList = jdbcTemplate.queryForList("DESC compensate ");
        compensateFieldLength = getLength(compensateMapsList, pattern);

        List<Map<String, Object>> compensateExtMapsList = jdbcTemplate.queryForList("DESC compensate_method_args_ext ");
        compensateExtFieldLength = getLength(compensateExtMapsList, pattern);
    }

    private Integer getLength(List<Map<String, Object>> mapsList, Pattern pattern) {
        return mapsList.stream()
                .filter(m -> "method_args".equals(m.get("Field")))
                .map(m -> m.get("Type"))
                .filter(Objects::nonNull)
                .map(s -> {
                    String s1 = (String) s;
                    Matcher matcher = pattern.matcher(s1);
                    if (matcher.find()) {
                        return Integer.valueOf(matcher.group());
                    }
                    return 8000;
                })
                .findFirst()
                .orElse(8000);
    }


    private static List<String> stringSpilt(String s, int spiltNum) {
        if (spiltNum <= 0 || s.length() <= spiltNum) {
            return Collections.singletonList(s);
        }
        int startIndex = 0;
        int endIndex = spiltNum;
        int length = s.length();
        List<String> subs = new ArrayList<>();
        boolean isEnd = true;
        while (isEnd) {
            if (endIndex >= length) {
                endIndex = length;
                isEnd = false;
            }
            subs.add(s.substring(startIndex, endIndex));
            startIndex = endIndex;
            endIndex = endIndex + spiltNum;
        }
        return subs;
    }


    public static void main(String[] args) {
        String[] arr = {" hello ", " world "};
        String collect = Arrays.stream(arr).collect(Collectors.joining());
        System.err.println(collect);

    }

}
