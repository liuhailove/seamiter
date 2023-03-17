package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.MockItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Mock参数规则项Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface MockItemDao {
    /**
     * 保存
     *
     * @param mockItem Mock项规则项
     * @return 影响行数
     */
    int save(MockItem mockItem);

    /**
     * 更新
     *
     * @param mockItem Mock项规则项
     * @return 影响行数
     */
    int update(MockItem mockItem);

    /**
     * 移除规则
     *
     * @param id 规则ID
     * @return 影响行数
     */
    int remove(@Param("id") Long id);

    /**
     * 移除规则
     *
     * @param mockRuleId mock规则ID
     * @return 影响行数
     */
    int removeBy(@Param("mockRuleId") Long mockRuleId);

    /**
     * 查找全部
     *
     * @param mockRuleId mock规则ID
     * @return 记录数
     */
    List<MockItem> listAll(@Param("mockRuleId") Long mockRuleId);


    /**
     * 查找全部打开开关的item
     *
     * @param mockRuleId mock规则ID
     * @param open       是否打开
     * @return 记录数
     */
    List<MockItem> listAllOpen(@Param("mockRuleId") Long mockRuleId, @Param("open") Boolean open);

    /**
     * 分页查找
     *
     * @param offset     偏移量
     * @param pageSize   分页大小
     * @param mockRuleId mock规则ID
     * @return 记录数
     */
    List<MockItem> pageList(@Param("offset") int offset,
                            @Param("pageSize") int pageSize,
                            @Param("mockRuleId") Long mockRuleId);

    /**
     * 分页查找
     *
     * @param offset     偏移量
     * @param pageSize   分页大小
     * @param mockRuleId mock规则ID
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("mockRuleId") Long mockRuleId);

    /**
     * 加载Mock项
     *
     * @param id 主键ID
     * @return Mock项
     */
    MockItem load(@Param("id") Long id);

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
     * @param mockRuleId mock规则ID
     * @param position   位置
     * @return Mock项
     */
    MockItem selectPrevious(@Param("mockRuleId") Long mockRuleId, @Param("position") Long position);

    /**
     * 下一个
     *
     * @param mockRuleId mock规则ID
     * @param position   位置
     * @return Mock项
     */
    MockItem selectNext(@Param("mockRuleId") Long mockRuleId, @Param("position") Long position);

    /**
     * 判断item是否已经存在
     *
     * @param mockRuleId 规则ID
     * @param tagName    tag名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean existBy(@Param("mockRuleId") Long mockRuleId, @Param("tagName") String tagName);
}
