package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.GrayCondition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 灰度条件Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface GrayConditionDao {
    /**
     * 灰度条件保存
     *
     * @param grayCondition 灰度条件
     * @return 影响行数
     */
    int save(GrayCondition grayCondition);

    /**
     * 灰度条件更新
     *
     * @param grayCondition 灰度条件
     * @return 影响行数
     */
    int update(GrayCondition grayCondition);

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 分页查找
     *
     * @param offset     偏移量
     * @param pageSize   分页大小
     * @param grayRuleId 灰度规则ID
     * @return 记录数
     */
    List<GrayCondition> pageList(@Param("offset") int offset,
                                 @Param("pageSize") int pageSize,
                                 @Param("grayRuleId") Long grayRuleId);

    /**
     * 分页查找
     *
     * @param offset     偏移量
     * @param pageSize   分页大小
     * @param grayRuleId 灰度规则ID
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("grayRuleId") Long grayRuleId);

    /**
     * 根据应用名称查询灰度规则
     *
     * @param grayRuleId 灰度规则ID
     * @param open       是否开启
     * @return 规则集合
     */
    List<GrayCondition> queryBy(@Param("grayRuleId") Long grayRuleId, @Param("open") Boolean open);

    /**
     * 判断灰度标签是否存在
     *
     * @param grayRuleId     应用名称
     * @param targetResource 目标资源
     * @param targetVersion  目标版本
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean existBy(@Param("grayRuleId") Long grayRuleId, @Param("targetResource") String targetResource, @Param("targetVersion") String targetVersion);

    /**
     * 规则ID
     *
     * @param id 主键ID
     * @return 流控规则
     */
    GrayCondition load(@Param("id") Long id);

    /**
     * 更新位置
     *
     * @param id       主键ID
     * @param position 位置
     * @return 影响行数
     */
    int updatePosition(@Param("id") Long id, @Param("position") Long position);

    /**
     * 上一个
     *
     * @param grayRuleId 灰度规则ID
     * @param position   位置
     * @return Mock项
     */
    GrayCondition selectPrevious(@Param("grayRuleId") Long grayRuleId, @Param("position") Long position);

    /**
     * 下一个
     *
     * @param grayRuleId 灰度规则ID
     * @param position   位置
     * @return Mock项
     */
    GrayCondition selectNext(@Param("grayRuleId") Long grayRuleId, @Param("position") Long position);

    /**
     * 关闭条件路由
     *
     * @param grayRuleId 灰度规则ID
     * @return 影响行数
     */
    int closeBy(@Param("grayRuleId") Long grayRuleId);

    /**
     * 删除条件路由
     *
     * @param grayRuleId 灰度规则ID
     * @return 影响行数
     */
    int removeBy(@Param("grayRuleId") Long grayRuleId);
}
