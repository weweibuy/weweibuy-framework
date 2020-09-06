package com.weweibuy.framework.compensate.mybatis.store;

import com.weweibuy.framework.compensate.core.CompensateConfigStore;
import com.weweibuy.framework.compensate.core.CompensateStore;
import com.weweibuy.framework.compensate.model.BuiltInCompensateType;
import com.weweibuy.framework.compensate.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.mybatis.constant.CompensateStatusConstant;
import com.weweibuy.framework.compensate.mybatis.mapper.CompensateMapper;
import com.weweibuy.framework.compensate.mybatis.po.Compensate;
import com.weweibuy.framework.compensate.mybatis.po.CompensateArgsExt;
import com.weweibuy.framework.compensate.mybatis.po.CompensateExample;
import com.weweibuy.framework.compensate.mybatis.repository.CompensateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author durenhao
 * @date 2020/2/16 12:22
 **/
public class JdbcCompensateStore implements CompensateStore {

    @Autowired
    private CompensateMapper compensateMapper;

    @Autowired
    private CompensateRepository compensateRepository;

    @Autowired
    private CompensateConfigStore compensateConfigStore;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Integer compensateFieldLength = 5000;

    private Integer compensateExtFieldLength = 5000;


    @Override
    public String saveCompensateInfo(CompensateInfo compensateInfo) {
        if (BuiltInCompensateType.BIZ_ID.toString().equals(compensateInfo.getCompensateType()) ||
                compensateInfo.getMethodArgs() == null) {
            Compensate compensate = toCompensate(compensateInfo);
            compensateMapper.insertSelective(compensate);
            return compensate.getId() + "";
        }

        String methodArgs = compensateInfo.getMethodArgs();
        if (methodArgs.length() > compensateFieldLength) {
            String substring = methodArgs.substring(0, compensateFieldLength);
            compensateInfo.setMethodArgs(substring);
            Compensate compensate = toCompensate(compensateInfo);
            compensate.setHasArgsExt(true);

            String substring1 = methodArgs.substring(compensateFieldLength, methodArgs.length());

            List<CompensateArgsExt> collect = stringSpilt(substring1, compensateExtFieldLength).stream()
                    .map(m -> {
                        CompensateArgsExt compensateMethodArgs = new CompensateArgsExt();
                        compensateMethodArgs.setMethodArgs(m);
                        return compensateMethodArgs;
                    })
                    .collect(Collectors.toList());
            compensateRepository.insertCompensate(compensate, collect);
            return compensate.getId() + "";
        }
        Compensate compensate = toCompensate(compensateInfo);
        compensateMapper.insertSelective(compensate);
        return compensate.getId() + "";

    }

    @Override
    public Collection<CompensateInfoExt> queryCompensateInfo(Integer limit) {
        CompensateExample compensateExample = CompensateExample.newAndCreateCriteria()
                .andIsDeleteEqualTo(false)
                .andNextTriggerTimeLessThan(LocalDateTime.now())
                .andCompensateStatusEqualTo(CompensateStatusConstant.COMPENSATING)
                .example();
        List<Compensate> compensates = compensateMapper.selectByExampleWithLimit(compensateExample, limit);

        Map<Boolean, List<Compensate>> listMap = compensates.stream()
                .collect(Collectors.groupingBy(Compensate::getHasArgsExt));

        List<Compensate> hasExtCompensateList = listMap.get(true);
        if (CollectionUtils.isEmpty(hasExtCompensateList)) {
            return compensates.stream()
                    .map(this::toCompensateInfoExt)
                    .collect(Collectors.toList());
        }

        List<Long> idList = hasExtCompensateList.stream().map(Compensate::getId).collect(Collectors.toList());
        Map<Long, List<CompensateArgsExt>> compensateMethodArgsExtMap = compensateRepository.selectExtByCompensateId(idList).stream()
                .collect(Collectors.groupingBy(CompensateArgsExt::getCompensateId));

        Stream<Compensate> compensateStream = hasExtCompensateList.stream()
                .map(c -> {
                    List<CompensateArgsExt> compensateMethodArgsExtList = compensateMethodArgsExtMap.get(c.getId());
                    if (CollectionUtils.isEmpty(compensateMethodArgsExtList)) {
                        return null;
                    }
                    String collect = compensateMethodArgsExtList.stream()
                            .sorted(Comparator.comparing(CompensateArgsExt::getArgsOrder))
                            .map(CompensateArgsExt::getMethodArgs)
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
    public Collection<CompensateInfoExt> queryCompensateInfoByIdForce(Set<String> idSet) {
        return compensateMapper.selectByExample(
                CompensateExample.newAndCreateCriteria()
                        .andIdIn(idSet.stream().map(Long::valueOf).collect(Collectors.toList()))
                        .andCompensateStatusNotEqualTo(CompensateStatusConstant.COMPENSATE_SUCCESS)
                        .example())
                .stream().map(this::toCompensateInfoExt).collect(Collectors.toList());
    }

    @Override
    public int updateCompensateInfo(String id, CompensateInfoExt compensateInfo) {
        Compensate compensate = toCompensate2(compensateInfo);
        compensate.setId(Long.valueOf(id));
        return compensateMapper.updateByPrimaryKeySelective(compensate);
    }

    @Override
    public int deleteCompensateInfo(String id, Boolean success) {
        Compensate compensate = new Compensate();
        if (success) {
            compensate.setCompensateStatus(CompensateStatusConstant.COMPENSATE_SUCCESS);
        } else {
            compensate.setCompensateStatus(CompensateStatusConstant.COMPENSATE_FAIL);
        }
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
        infoExt.setCompensateType(compensate.getCompensateType());
        CompensateConfigProperties properties = compensateConfigStore.compensateConfig(compensate.getCompensateKey());
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
        compensate.setNextTriggerTime(compensateInfo.getNextTriggerTime());
        return compensate;
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


}
