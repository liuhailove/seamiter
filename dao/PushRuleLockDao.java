package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.PushRuleLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 推送规则锁接口
 *
 * @author honggang.liu
 */
@Mapper
public interface PushRuleLockDao {

    /**
     * 查询推送规则锁
     *
     * @param ruleType    规则类型
     * @param currentTime 当前时间
     * @return 推送规则锁
     */
    PushRuleLock query(@Param("ruleType") String ruleType, @Param("currentTime") Long currentTime);

    /**
     * 推送规则锁更新，
     * 如果triggerNextTime<currentTime则可以更新
     *
     * @param ruleType        规则类型
     * @param triggerLastTime 上次触发时间
     * @param triggerNextTime 下次触发时间
     * @param currentTime     当前时间
     * @return 返回影响行数
     */
    int update(@Param("ruleType") String ruleType, @Param("triggerLastTime") Long triggerLastTime, @Param("triggerNextTime") Long triggerNextTime, @Param("currentTime") Long currentTime);

    /**
     * 保存
     *
     * @param pushRuleLock 推送规则锁
     * @return 返回影响行数
     */
    int save(PushRuleLock pushRuleLock);

    /**
     * 判断app是否存在
     *
     * @param ruleType 规则类型
     * @return 存在返回true，否则返回false或者null
     */
    Boolean exist(@Param("ruleType") String ruleType);
}
