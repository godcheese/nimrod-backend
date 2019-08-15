package com.gioov.oryx.quartz.service.impl;

import com.gioov.tile.web.exception.BaseResponseException;
import com.gioov.oryx.common.easyui.Pagination;
import com.gioov.oryx.quartz.entity.JobEntity;
import com.gioov.oryx.quartz.job.BaseJob;
import com.gioov.oryx.quartz.mapper.JobMapper;
import com.gioov.oryx.quartz.service.JobService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author godcheese [godcheese@outlook.com]
 * @date 2019-02-01
 */
@Service
public class JobServiceImpl implements JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Date addOne(String jobClassName, String jobGroup, String cronExpression, String description) throws BaseResponseException {
        try {
            scheduler.start();
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroup).withDescription(description).build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                    // 不触发立即执行，等待下次 Cron 触发频率到达时刻开始按照 Cron 频率依次执行
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroup).withSchedule(cronScheduleBuilder).withDescription(description).build();
            return scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (IllegalAccessException | InstantiationException | SchedulerException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new BaseResponseException("任务新增失败");
        }
    }

    @Override
    public JobEntity getOne(String jobClassName, String jobGroup) {
        return jobMapper.getOneByJobClassNameAndJobGroup(jobClassName, jobGroup);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public int deleteAll(List<String> jobClassNameList, List<String> jobGroupList) throws BaseResponseException {
        int index = 0;
        try {
            JobKey jobKey;
            for (String jobClassName : jobClassNameList) {
                jobKey = JobKey.jobKey(jobClassName, jobGroupList.get(index));
                scheduler.pauseJob(jobKey);
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupList.get(index)));
                scheduler.deleteJob(jobKey);
                index++;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BaseResponseException("任务删除失败");
        }
        return index;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Date updateCronExpressionByJobClassNameAndJobGroup(String jobClassName, String jobGroup, String cronExpression, String description) throws BaseResponseException {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroup);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression)
                    // 不触发立即执行，等待下次 Cron 触发频率到达时刻开始按照 Cron 频率依次执行
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).withDescription(description).build();
            jobMapper.updateJobDetailsDescriptionByJobClassNameAndJobGroup(jobClassName, jobGroup, description);
            scheduler.rescheduleJob(triggerKey, cronTrigger);
            return new Date();
        } catch (SchedulerException e) {
            throw new BaseResponseException("任务更新失败");
        }
    }

    @Override
    public int pauseAll(List<String> jobClassNameList, List<String> jobGroupList) throws BaseResponseException {
        int index = 0;
        try {
            if(jobClassNameList != null && jobGroupList != null) {
                for (String jobClassName : jobClassNameList) {
                    scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupList.get(index)));
                    index++;
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BaseResponseException("任务暂停失败");
        }
        return index;
    }

    @Override
    public int resumeAll(List<String> jobClassNameList, List<String> jobGroupList) throws BaseResponseException {
        int index = 0;
        try {
            if(jobClassNameList != null && jobGroupList != null) {
                for (String jobClassName : jobClassNameList) {
                    LOGGER.info("jobClassName={}={}", jobClassName, jobGroupList.get(index));
                    scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupList.get(index)));
                    index++;
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BaseResponseException("任务恢复失败");
        }
        return index;
    }

    @Override
    public Pagination<JobEntity> pageAll(Integer page, Integer rows) {
        Pagination<JobEntity> pagination = new Pagination<>();
        PageHelper.startPage(page, rows);
        Page<JobEntity> jobEntityPage = jobMapper.pageAll();
        pagination.setRows(jobEntityPage.getResult());
        pagination.setTotal(jobEntityPage.getTotal());
        return pagination;
    }

    private static BaseJob getClass(String classname) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob) class1.newInstance();
    }

}
