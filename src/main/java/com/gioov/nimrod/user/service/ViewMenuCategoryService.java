package com.gioov.nimrod.user.service;

import com.gioov.common.web.exception.BaseResponseException;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.vue.antd.AntdTreeNode;
import com.gioov.nimrod.common.vue.antd.ViewMenuCategoryEntityAsAntdTable;
import com.gioov.nimrod.user.entity.ViewMenuCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
public interface ViewMenuCategoryService {

    /**
     * 指定用户 id ，获取视图菜单分类
     *
     * @param userId 用户 id
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> listAllParentByUserId(Long userId);

    /**
     * 指定视图菜单分类父级 id 、用户 id ，获取视图菜单分类
     *
     * @param parentId 视图页面分类父级 id
     * @param userId   用户 id
     * @return List<ViewMenuCategoryEntity>
     */
    List<ViewMenuCategoryEntity> listAllChildByParentIdAndUserId(Long parentId, Long userId);

    List<ViewMenuCategoryEntity> listAllByParentIdAndRoleId(Long parentId, Long roleId);

    List<ViewMenuCategoryEntity> listAllParentByRoleId(Long roleId);

    List<Map<String, Object>> listAllChildViewMenuCategoryAndViewMenuByParentIdAndUserId(Long parentId, Long userId);

    List<ViewMenuCategoryEntity> listAll();

    List<ViewMenuCategoryEntity> searchAllByName(String name);

    /**
     * 指定 API 分类 id ，分页获取所有 API
     *
     * @param page 页
     * @param rows 每页显示数量
     * @return Pagination<ViewMenuCategoryEntity>
     */
    Pagination<ViewMenuCategoryEntity> pageAllParent(Long roleId, Integer page, Integer rows);

    /**
     * 新增角色
     *
     * @param viewMenuCategoryEntity ViewMenuCategoryEntity
     * @return ViewMenuCategoryEntity
     */
    ViewMenuCategoryEntity insertOne(ViewMenuCategoryEntity viewMenuCategoryEntity);

    /**
     * 保存角色
     *
     * @param viewMenuCategoryEntity ViewMenuCategoryEntity
     * @return ViewMenuCategoryEntity
     */
    ViewMenuCategoryEntity updateOne(ViewMenuCategoryEntity viewMenuCategoryEntity);

    /**
     * 指定角色 id list ，批量删除角色
     *
     * @param idList 角色 id list
     * @return 已删除角色个数
     */
    int deleteAll(List<Long> idList) throws BaseResponseException;

    /**
     * 指定角色 id ，获取角色信息
     *
     * @param id 角色 id
     * @return ViewMenuCategoryEntity
     */
    ViewMenuCategoryEntity getOne(Long id);

    /**
     * 获取视图菜单分类，以 Antd Table 形式展示
     * @return
     */
    List<ViewMenuCategoryEntityAsAntdTable> listAllViewMenuCategoryEntityAsAntdTable();
    List<ViewMenuCategoryEntityAsAntdTable> getViewMenuCategoryChildrenViewMenuCategoryEntityAsAntdTable(long parentId, List<ViewMenuCategoryEntityAsAntdTable> viewMenuCategoryAsAntdTableList);

    /**
     * 获取视图菜单分类，以 Antd Table 形式展示
     * 判断当前角色是否被授权此视图菜单分类
     * @return
     */
    List<ViewMenuCategoryEntityAsAntdTable> listAllViewMenuCategoryEntityAsAntdTableByRoleId(long roleId);

    /**
     * 获取所有部门，以 Antd TreeNode 形式展示
     * @return
     */
    List<AntdTreeNode> listAllViewMenuCategoryAntdTreeNode();
    List<AntdTreeNode> getViewMenuCategoryChildrenAntdTreeNode(long parentId, List<AntdTreeNode> viewMenuCategoryAntdTreeNodeList);

    /**
     * 指定角色 id、API 权限（authority），批量授权
     *
     * @param roleId        角色 id
     * @param viewMenuCategoryIdList 权限（authority） list
     * @return List<String>
     */
    List<Long> grantAllByRoleIdAndViewMenuCategoryIdList(Long roleId, List<Long> viewMenuCategoryIdList);

    /**
     * 指定角色 id、API 权限（authority），批量撤销授权
     *
     * @param roleId        角色 id
     * @param viewMenuCategoryIdList 权限（authority） list
     * @return List<String>
     */
    List<Long> revokeAllByRoleIdAndViewMenuCategoryIdList(Long roleId, List<Long> viewMenuCategoryIdList);

}
