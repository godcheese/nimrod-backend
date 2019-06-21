package com.gioov.nimrod.user.service;

import com.gioov.common.web.exception.BaseResponseException;
import com.gioov.nimrod.common.easyui.ComboTree;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.easyui.TreeGrid;
import com.gioov.nimrod.common.vue.antd.AntdTree;
import com.gioov.nimrod.common.vue.antd.AntdTreeNode;
import com.gioov.nimrod.common.vue.antd.DepartmentEntityAsAntdTable;
import com.gioov.nimrod.user.entity.DepartmentEntity;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
public interface DepartmentService {

    Pagination<DepartmentEntity> pageAllParent(Integer page, Integer rows);

    /**
     * 指定父级 API 分类 id ，获取所有 API 分类
     *
     * @return List<ApiCategoryEntity>
     */
    List<DepartmentEntity> listAllParent();

    /**
     * 指定父级 API 分类 id ，获取所有 API 分类
     *
     * @param parentId API 分类父级 id
     * @return List<ApiCategoryEntity>
     */
    List<DepartmentEntity> listAllByParentId(Long parentId);

//    /**
//     * 指定用户角色 list ，获取所有角色
//     *
//     * @param userRoleEntityList 用户角色 list
//     * @return List<RoleEntity>
//     */
//    List<DepartmentEntity> listAllByUserRoleList(List<UserRoleEntity> userRoleEntityList);

    /**
     * 指定 API 分类 id ，分页获取所有 API
     *
     * @param page 页
     * @param rows 每页显示数量
     * @return Pagination<DepartmentEntity>
     */
//    Pagination<DepartmentEntity> pageAll(Integer page, Integer rows);


    /**
     * 获取所有角色
     *
     * @return List<DepartmentEntity>
     */
    List<DepartmentEntity> listAll();

//    /**
//     * 指定用户 id ，获取用户角色
//     *
//     * @param userId 用户 id
//     * @return List<RoleEntity>
//     */
//    List<DepartmentEntity> listAllByUserId(Long userId);

    /**
     * 新增角色
     *
     * @param departmentEntity DepartmentEntity
     * @return DepartmentEntity
     */
    DepartmentEntity insertOne(DepartmentEntity departmentEntity);

    /**
     * 保存角色
     *
     * @param departmentEntity DepartmentEntity
     * @return DepartmentEntity
     */
    DepartmentEntity updateOne(DepartmentEntity departmentEntity);

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
     * @return DepartmentEntity
     */
    DepartmentEntity getOne(Long id);

    /**
     * 获取所有部门，以 EasyUI ComboTree 形式展示
     * @return
     */
    List<ComboTree> listAllDepartmentComboTree();
    List<ComboTree> getDepartmentChildrenComboTree(long parentId, List<ComboTree> departmentComboTreeList);

    /**
     * 获取所有部门，以 EasyUI TreeGrid 形式展示
     * @return
     */
    List<TreeGrid> listAllDepartmentTreeGrid();
    List<TreeGrid> getDepartmentChildrenTreeGrid(long parentId, List<TreeGrid> departmentTreeGridList);

    /**
     * 获取所有部门，以 Antd TreeNode 形式展示
     * @return
     */
    List<AntdTreeNode> listAllDepartmentAntdTreeNode();
    List<AntdTreeNode> getDepartmentChildrenAntdTreeNode(long parentId, List<AntdTreeNode> departmentAntdTreeNodeList);

    /**
     * 获取所有部门，以 Antd Tree 形式展示
     * @return
     */
    List<AntdTree> listAllDepartmentAntdTree();
    List<AntdTree> getDepartmentChildrenAntdTree(long parentId, List<AntdTree> departmentAntdTreeList);

    /**
     * 获取所有部门，以 Antd Table 形式展示
     * @return
     */
    List<DepartmentEntityAsAntdTable> listAllDepartmentEntityAsAntdTable();
    List<DepartmentEntityAsAntdTable> getDepartmentChildrenDepartmentEntityAsAntdTable(long parentId, List<DepartmentEntityAsAntdTable> departmentEntityAsAntdTableList);

    /**
     * 根据子级部门 id 获取所有父级部门
     * @param id
     * @return
     */
    List<DepartmentEntity> listAllByDepartmentId(Long id);
}
