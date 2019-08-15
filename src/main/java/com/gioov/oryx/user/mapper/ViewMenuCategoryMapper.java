package com.gioov.oryx.user.mapper;

import com.gioov.tile.mybatis.CrudMapper;
import com.gioov.oryx.user.entity.ViewMenuCategoryEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@Component("viewMenuCategoryMapper")
@Mapper
public interface ViewMenuCategoryMapper extends CrudMapper<ViewMenuCategoryEntity, Long> {

    /**
     * 指定角色 id ，获取所有视图菜单分类
     *
     * @param roleId 角色 id
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> listAllByParentIdIsNullAndRoleId(@Param("roleId") Long roleId);

    /**
     * 指定角色 id ，获取所有视图菜单分类
     * @param roleId   角色 id
     * @return List<ViewMenuCategoryEntity>
     */
    Page<ViewMenuCategoryEntity> pageAllByParentIdIsNullAndRoleId(@Param("roleId") Long roleId);

    /**
     * 指定角色 id ，统计父级 id 为 null 的视图菜单分类个数
     *
     * @param roleId 角色 id
     * @return int
     */
    int countAllByParentIdIsNullAndRoleId(@Param("roleId") Long roleId);

    /**
     * 指定角色 id 、视图菜单分类父级 id ，获取所有视图菜单分类
     *
     * @param roleId   角色 id
     * @param parentId 视图菜单分类父级 id
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> listAllByParentIdAndRoleId(@Param("parentId") Long parentId, @Param("roleId") Long roleId);

    /**
     * 指定角色 id 、视图菜单分类父级 id ，获取视图菜单分类
     *
     * @param roleId   角色 id
     * @param parentId 视图菜单分类父级 id
     * @return ViewMenuCategoryEntity
     */
    ViewMenuCategoryEntity getOneByParentId(@Param("parentId") Long parentId);

    /**
     * 指定视图菜单分类名，搜索获取所有视图菜单分类
     *
     * @param name 视图菜单分类名
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> searchAllByName(@Param("name") String name);

    /**
     * 指定角色 id ，获取所有视图菜单分类
     *
     * @param roleIdList 角色 id list
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> listAllByParentIdIsNullAndRoleIdList(@Param("roleIdList") List<Long> roleIdList);

    /**
     * 指定角色 id 、视图菜单分类父级 id ，获取所有视图菜单分类
     *
     * @param roleIdList   角色 id list
     * @param parentId 视图菜单分类父级 id
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> listAllByParentIdAndRoleIdList(@Param("parentId") Long parentId, @Param("roleIdList") List<Long> roleIdList);

}