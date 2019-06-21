package com.gioov.nimrod.user.service;

import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.vue.antd.AntdVueMenu;
import com.gioov.nimrod.user.entity.ViewMenuEntity;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
public interface ViewMenuService {

    /**
     * 指定用户 id 、 视图菜单分类 id ，获取所有视图菜单
     *
     * @param userId         用户 id
     * @param menuCategoryId 视图菜单分类 id
     * @return List<ViewMenuEntity>
     */
    List<ViewMenuEntity> listAllByUserIdAndMenuCategoryId(Long userId, Long menuCategoryId);

    Pagination<ViewMenuEntity> pageAllByMenuCategoryIdAndRoleId(Long menuCategoryId, Long roleId, Integer page, Integer rows);

    List<ViewMenuEntity> searchAllByName(String name);

    /**
     * 新增角色
     *
     * @param viewMenuEntity ViewMenuEntity
     * @return ViewMenuEntity
     */
    ViewMenuEntity insertOne(ViewMenuEntity viewMenuEntity);

    /**
     * 保存角色
     *
     * @param viewMenuEntity ViewMenuEntity
     * @return ViewMenuEntity
     */
    ViewMenuEntity updateOne(ViewMenuEntity viewMenuEntity);

    /**
     * 指定角色 id list ，批量删除角色
     *
     * @param idList 角色 id list
     * @return 已删除角色个数
     */
    int deleteAll(List<Long> idList);

    /**
     * 指定角色 id ，获取角色信息
     *
     * @param id 角色 id
     * @return ViewMenuEntity
     */
    ViewMenuEntity getOne(Long id);

    List<AntdVueMenu> forEachVueMenuByVueMenuParentId(long parentId, List<AntdVueMenu> vueMenuList);

    public void forEachViewMenuAndViewMenuCategoryByViewMenuCategoryId(long viewMenuCategoryId, List<Long> roleIdList, List<AntdVueMenu> vueMenuList);

    /**
     * 指定角色 id、API 权限（authority），批量授权
     *
     * @param roleId        角色 id
     * @param viewMenuIdList 权限（authority） list
     * @return List<String>
     */
    List<Long> grantAllByRoleIdAndViewMenuIdList(Long roleId, List<Long> viewMenuIdList);

    /**
     * 指定角色 id、API 权限（authority），批量撤销授权
     *
     * @param roleId        角色 id
     * @param viewMenuIdList 权限（authority） list
     * @return List<String>
     */
    List<Long> revokeAllByRoleIdAndViewMenuIdList(Long roleId, List<Long> viewMenuIdList);

}
