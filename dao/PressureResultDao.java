package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.PressureResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 压测结果Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface PressureResultDao {
    /**
     * 保存
     *
     * @param pressureResult 压测结果
     * @return 影响行数
     */
    int save(PressureResult pressureResult);

    /**
     * 更新
     *
     * @param pressureResult 压测结果
     * @return 影响行数
     */
    int update(PressureResult pressureResult);

    /**
     * 移除
     *
     * @param id 压测结果ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param pressureRuleId 压测规则ID
     * @return 记录数
     */
    List<PressureResult> pageList(@Param("offset") int offset,
                                  @Param("pageSize") int pageSize,
                                  @Param("pressureRuleId") Long pressureRuleId);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param pressureRuleId 压测规则ID
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("pressureRuleId") Long pressureRuleId);

    /**
     * 压测结果
     *
     * @param id 压测结果ID
     * @return 压测结果
     */
    PressureResult load(@Param("id") Long id);

}
