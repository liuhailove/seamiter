package com.shopee.seamiter.dao;

import com.shopee.seamiter.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色管理
 *
 * @author honggang.liu
 */
@Mapper
public interface RoleDao {
    /**
     * 分页加载
     *
     * @param offset   偏移
     * @param pagesize 分页大小
     * @return 角色列表
     */
    List<Role> pageList(@Param("offset") int offset,
                        @Param("pagesize") int pagesize);


    /**
     * 分页count
     *
     * @param offset   偏移
     * @param pagesize 分页大小
     * @return 角色count
     */
    int pageListCount(@Param("offset") int offset,
                      @Param("pagesize") int pagesize);

    /**
     * 角色加载
     *
     * @param id 角色ID
     * @return 角色加载
     */
    Role load(@Param("id") Integer id);

    /**
     * 角色加载
     *
     * @param roleName 角色名称
     * @return 角色加载
     */
    Role loadByName(@Param("roleName") String roleName);

    /**
     * 角色加载
     *
     * @param menuName 菜单名称
     * @return 角色加载
     */
    List<Role> loadByMenu(@Param("menuName") String menuName);

    /**
     * 角色中是否包含此菜单
     *
     * @param menuName 菜单名称
     * @return 是返回true,否则返回false
     */
    boolean existByMenu(@Param("menuName") String menuName);

    /**
     * 角色保存
     *
     * @param role 角色对象
     * @return 角色保存
     */
    int save(Role role);

    /**
     * 角色更新
     *
     * @param role 角色更新
     * @return 角色更新
     */
    int update(Role role);

    /**
     * 角色删除
     *
     * @param id 角色ID
     * @return 角色删除结果
     */
    int delete(@Param("id") int id);
}
