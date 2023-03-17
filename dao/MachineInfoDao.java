package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.MachineInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机器信息DAO
 *
 * @author honggang.liu
 */
@Mapper
public interface MachineInfoDao {

    /**
     * 注册更新
     *
     * @param machineInfo 机器信息
     * @return 返回影响行数
     */
    int registryUpdate(MachineInfo machineInfo);

    /**
     * 注册保存
     *
     * @param machineInfo 机器信息
     * @return 返回影响行数
     */
    int registrySave(MachineInfo machineInfo);

    /**
     * 注册删除
     *
     * @param app  应用名称
     * @param ip   ip
     * @param port 端口号
     * @return 返回影响行数
     */
    int registryDelete(@Param("app") String app, @Param("ip") String ip, @Param("port") int port);

    /**
     * 根据应用名称获取应用机器信息
     *
     * @param app      应用名称
     * @param ip       ip地址
     * @param offset   偏移量
     * @param pagesize 分页
     * @return 机器列表
     */
    List<MachineInfo> getMachineDetailByApp(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("app") String app, @Param("ip") String ip);

    /**
     * 获取机器信息
     *
     * @param app  应用名称
     * @param ip   ip
     * @param port 端口号
     * @return 存在返回机器信息
     */
    MachineInfo getMachineDetail(@Param("app") String app, @Param("ip") String ip, @Param("port") int port);

    /**
     * 获取机器信息列表
     *
     * @param app 应用名称
     * @return 存在返回机器信息
     */
    List<MachineInfo> queryBy(@Param("app") String app);

    /**
     * 统计节点个数
     *
     * @param app 应用名称
     * @return 节点个数
     */
    int countBy(@Param("app") String app);

    /**
     * 查找健康机器列表
     *
     * @param app         应用名称
     * @param deadTimeout 断联时间
     * @return 健康机器列表
     */
    List<MachineInfo> findHealthy(@Param("app") String app, @Param("deadTimeout") Long deadTimeout);

    /**
     * 查看已经不心跳的machine
     *
     * @param deadTimeout 断联时间
     * @return 不心跳的machine
     */
    List<Integer> findDead(@Param("deadTimeout") Long deadTimeout);

    /**
     * 移除已经不心跳的machine
     *
     * @param ids 断联id
     * @return 影响行数
     */
    int removeDead(@Param("ids") List<Integer> ids);

    List<MachineInfo> findAll(@Param("deadTimeout") Long deadTimeout);

}
