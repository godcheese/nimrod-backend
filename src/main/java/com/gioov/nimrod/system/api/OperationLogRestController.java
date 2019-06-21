package com.gioov.nimrod.system.api;

import com.gioov.common.mybatis.Sort;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.operationlog.OperationLog;
import com.gioov.nimrod.common.operationlog.OperationLogType;
import com.gioov.nimrod.system.System;
import com.gioov.nimrod.system.entity.DictionaryEntity;
import com.gioov.nimrod.system.entity.OperationLogEntity;
import com.gioov.nimrod.system.entity.OperationLogEntity2;
import com.gioov.nimrod.system.service.OperationLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gioov.nimrod.user.service.UserService.SYSTEM_ADMIN;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@RestController
@RequestMapping(value = System.Api.OPERATION_LOG, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OperationLogRestController {

    private static final String OPERATION_LOG = "/API/SYSTEM/OPERATION_LOG";

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 获取当前用户的菜单

     * @return ResponseEntity<Pagination<VueMenu>>
     */
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + OPERATION_LOG + "/PAGE_ALL')")
    @GetMapping(value = "/page_all")
    public ResponseEntity<Pagination<OperationLogEntity2>> pageAll(@RequestParam Integer page, @RequestParam Integer rows) {
        Pagination<OperationLogEntity2> pagination = new Pagination<>();

//        if(sorterField != null && !"".equals(sorterField) && sorterOrder != null && !"".equals(sorterOrder)) {
//            sorterField = StringUtil.camelToUnderline(sorterField);
//            String orderBy = sorterField + " " + sorterOrder;
//            PageHelper.startPage(page, rows, orderBy);
//        } else {
        PageHelper.startPage(page, rows);
//        }
        Page<OperationLogEntity2> dictionaryEntity2Page = operationLogService.pageAll();
        pagination.setRows(dictionaryEntity2Page.getResult());
        pagination.setTotal(dictionaryEntity2Page.getTotal());
        return new ResponseEntity<>(pagination, HttpStatus.OK);
    }

    /**
     * 指定操作日志 id ，批量删除操作日志
     *
     * @param idList API 分类 id list
     * @return ResponseEntity<Integer>
     */
    @OperationLog(value = "批量删除操作日志", type = OperationLogType.API)
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + OPERATION_LOG + "/DELETE_ALL')")
    @PostMapping(value = "/delete_all")
    public ResponseEntity<Integer> deleteAll(@RequestParam("id[]") List<Long> idList) {
        return new ResponseEntity<>(operationLogService.deleteAll(idList), HttpStatus.OK);
    }

    /**
     * 指定操作日志 id ，获取操作日志信息
     *
     * @param id 操作日志 idx
     * @return ResponseEntity<OperationLogEntity2>
     */
    @OperationLog(value = "获取操作日志信息", type = OperationLogType.API)
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + OPERATION_LOG + "/ONE')")
    @GetMapping(value = "/one/{id}")
    public ResponseEntity<OperationLogEntity2> getOne(@PathVariable Long id) {
        return new ResponseEntity<>(operationLogService.getOne(id), HttpStatus.OK);
    }

    /**
     * 清空操作日志
     *
     * @return ResponseEntity<Integer>
     */
    @OperationLog(value = "清空操作日志", type = OperationLogType.API)
    @PreAuthorize("hasRole('" + SYSTEM_ADMIN + "') OR hasAuthority('" + OPERATION_LOG + "/CLEAR_ALL')")
    @PostMapping(value = "/clear_all")
    public ResponseEntity<HttpStatus> clearAll() {
        operationLogService.truncate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
