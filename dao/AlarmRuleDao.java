package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.AlarmRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 告警规则Dao
 *
 * @author honggang.liu
 */
@Mapper
public interface AlarmRuleDao {
    /**
     * 规则保存
     *
     * @param alarmRule 告警规则
     * @return 影响行数
     */
    int save(AlarmRule alarmRule);

    /**
     * 规则更新
     *
     * @param alarmRule 告警规则
     * @return 影响行数
     */
    int update(AlarmRule alarmRule);

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
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resourceType   资源类别
     * @param alarmName      告警名称
     * @param alarmLevel     告警级别
     * @param permissionApps 授权应用
     * @return 记录数
     */
    List<AlarmRule> pageList(@Param("offset") int offset,
                             @Param("pageSize") int pageSize,
                             @Param("app") String app,
                             @Param("resourceType") Integer resourceType,
                             @Param("alarmLevel") Integer alarmLevel,
                             @Param("alarmName") String alarmName,
                             @Param("permissionApps") List<String> permissionApps);

    /**
     * 分页查找
     *
     * @param offset         偏移量
     * @param pageSize       分页大小
     * @param app            应用名称
     * @param resourceType   资源类别
     * @param alarmName      告警名称
     * @param alarmLevel     告警级别
     * @param permissionApps 授权应用
     * @return 记录数
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pageSize") int pageSize,
                      @Param("app") String app,
                      @Param("resourceType") Integer resourceType,
                      @Param("alarmLevel") Integer alarmLevel,
                      @Param("alarmName") String alarmName,
                      @Param("permissionApps") List<String> permissionApps);

    /**
     * 根据应用名称查询告警规则
     *
     * @param app 应用名称
     * @return 规则集合
     */
    List<AlarmRule> queryByApp(@Param("app") String app);

    /**
     * 判断app是否存在告警规则
     *
     * @param app 应用名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean exist(@Param("app") String app);


    /**
     * 判断app对应的resource是否存在告警规则
     *
     * @param app       应用名称
     * @param alarmName 告警名称
     * @return 存在返回true，否则返回false或者NULL
     */
    Boolean existBy(@Param("app") String app, @Param("alarmName") String alarmName);

    /**
     * 规则ID
     *
     * @param id 主键ID
     * @return 告警规则
     */
    AlarmRule load(@Param("id") Long id);

    /**
     * 创建关联
     *
     * @param ruleId       规则ID
     * @param resourceIdes 资源集合
     * @param gmtModified  更新时间
     * @return 影响行数
     */
    Integer createRelation(@Param("ruleId") Long ruleId, @Param("resourceIdes") String resourceIdes, @Param("gmtModified") Date gmtModified);
}
