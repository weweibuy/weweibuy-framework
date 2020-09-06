package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;

import java.util.Collection;
import java.util.Set;

/**
 * 补偿存储器
 *
 * @author durenhao
 * @date 2020/2/13 20:02
 **/
public interface CompensateStore {

    /**
     * 保存补偿信息, 返回对应id
     *
     * @param compensateInfo
     */
    String saveCompensateInfo(CompensateInfo compensateInfo);

    /**
     * 查询补偿信息
     *
     * @param limit 限制数量
     * @return
     */
    Collection<CompensateInfoExt> queryCompensateInfo(Integer limit);

    /**
     * 根据id 强制查询
     *
     * @param idSet
     * @return
     */
    Collection<CompensateInfoExt> queryCompensateInfoByIdForce(Set<String> idSet);


    /**
     * 更新
     *
     * @param id
     * @param compensateInfo
     * @return
     */
    int updateCompensateInfo(String id, CompensateInfoExt compensateInfo);

    /**
     * 删除
     *
     * @param id
     * @param compensateSuccess 是否补偿成功
     * @return
     */
    int deleteCompensateInfo(String id, Boolean compensateSuccess);

}
