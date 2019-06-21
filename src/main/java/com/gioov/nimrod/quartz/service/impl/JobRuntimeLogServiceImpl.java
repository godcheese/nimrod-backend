package com.gioov.nimrod.quartz.service.impl;

import com.gioov.common.mybatis.Pageable;
import com.gioov.common.mybatis.Sort;
import com.gioov.nimrod.common.easyui.Pagination;
import com.gioov.nimrod.common.util.SpringContextUtil;
import com.gioov.nimrod.quartz.entity.JobRuntimeLogEntity;
import com.gioov.nimrod.quartz.mapper.JobRuntimeLogMapper;
import com.gioov.nimrod.quartz.service.JobRuntimeLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2019-02-13
 */
@Service
public class JobRuntimeLogServiceImpl implements JobRuntimeLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobRuntimeLogServiceImpl.class);

    private JobRuntimeLogMapper jobRuntimeLogMapper;

    public JobRuntimeLogServiceImpl() {
        jobRuntimeLogMapper = (JobRuntimeLogMapper) SpringContextUtil.getBean("jobRuntimeLogMapper", JobRuntimeLogMapper.class);
    }

    @Override
    public JobRuntimeLogEntity log(JobExecutionContext jobExecutionContext, JobExecutionException jobExecutionException, String log) {
        JobRuntimeLogEntity jobRuntimeLogEntity = new JobRuntimeLogEntity();
        jobRuntimeLogEntity.setJobClassName(jobExecutionContext.getJobDetail().getKey().getName());
        jobRuntimeLogEntity.setJobGroup(jobExecutionContext.getJobDetail().getKey().getGroup());
        jobRuntimeLogEntity.setDescription(jobExecutionContext.getJobDetail().getDescription());
        jobRuntimeLogEntity.setFireTime(jobExecutionContext.getFireTime());
        jobRuntimeLogEntity.setNextFireTime(jobExecutionContext.getNextFireTime());
        jobRuntimeLogEntity.setJobRunTime(jobExecutionContext.getJobRunTime());
        jobRuntimeLogEntity.setLog(log);
        if(jobExecutionException != null) {
            jobRuntimeLogEntity.setJobException(jobExecutionException.getMessage());
        }
        jobRuntimeLogEntity.setGmtCreated(new Date());

        jobRuntimeLogMapper.insertOne(jobRuntimeLogEntity);
        return jobRuntimeLogEntity;
    }

    @Override
    public Pagination<JobRuntimeLogEntity> pageAll(Integer page, Integer rows) {
        Pagination<JobRuntimeLogEntity> pagination = new Pagination<>();
        PageHelper.startPage(page, rows);
        Page<JobRuntimeLogEntity> jobRuntimeLogEntityPage = jobRuntimeLogMapper.pageAll();
        pagination.setRows(jobRuntimeLogEntityPage.getResult());
        pagination.setTotal(jobRuntimeLogEntityPage.getTotal());
        return pagination;
    }

    @Override
    public void truncate() {
        jobRuntimeLogMapper.truncate();
    }
}
