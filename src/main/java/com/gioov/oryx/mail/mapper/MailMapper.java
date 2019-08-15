package com.gioov.oryx.mail.mapper;

import com.gioov.tile.mybatis.CrudMapper;
import com.gioov.oryx.mail.entity.MailEntity;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2018-02-22
 */
@Component("mailMapper")
@Mapper
public interface MailMapper extends CrudMapper<MailEntity, Long> {

    /**
     * 指定状态 list，获取所有电子邮件
     * @param statusList 状态 list
     * @return List<MailEntity>
     */
    List<MailEntity> listAllByStatus(@Param("statusList") List<Integer> statusList);

    /**
     * 分页获取所有电子邮件
     * @return Page<MailEntity>
     */
    Page<MailEntity> pageAll();

}
