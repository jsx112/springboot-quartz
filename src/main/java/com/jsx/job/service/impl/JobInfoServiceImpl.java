package com.jsx.job.service.impl;


import com.jsx.job.util.JobDynamicScheduler;
import com.jsx.job.dao.JobInfoRepository;
import com.jsx.job.entity.JobInfo;
import com.jsx.job.service.JobInfoService;
import com.jsx.entry.ReturnT;
import org.quartz.CronExpression;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


/**
* job服务类，只能进行修改cron表达式或者暂停/恢复
* @author jiasx
* @create 2017/9/21 14:20
**/
@Service
@Transactional
public class JobInfoServiceImpl implements JobInfoService {
    private static Logger logger = LoggerFactory.getLogger(JobInfoServiceImpl.class);

    @Autowired
    @Qualifier("schedulerFactoryBean")
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobInfoRepository jobInfoRepository;


    /**
     * 任务列表
     **/
    public ReturnT<List<JobInfo>> list(JobInfo jobInfo){
        List<JobInfo> jobs = jobInfoRepository.findAll();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        for(JobInfo job:jobs){
            JobDynamicScheduler.fillJobInfo(scheduler,job);
        }
        return new ReturnT<List<JobInfo>>(jobs);
    }

    /**
     * 新增一个任务
     **/
    public ReturnT<String> add(JobInfo jobInfo){
        if (!CronExpression.isValidExpression(jobInfo.getCronExpression())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”");
        }
        if (StringUtils.isEmpty(jobInfo.getDescription())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“任务描述”");
        }
        jobInfoRepository.save(jobInfo);
        if (jobInfo.getId() < 1) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "新增任务失败");
        }

        String jobName = String.valueOf(jobInfo.getId());
        String jobGroup = jobInfo.getJobGroup();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            JobDynamicScheduler.addJob(scheduler,jobName, jobGroup, jobInfo.getCronExpression(),jobInfo.getJobClass());
            return ReturnT.SUCCESS;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            try {
                jobInfoRepository.delete(jobInfo);
                JobDynamicScheduler.removeJob(scheduler,jobName, jobGroup);
            } catch (SchedulerException e1) {
                logger.error(e.getMessage(), e1);
            }
            return new ReturnT<String>(ReturnT.FAIL_CODE, "新增任务失败:" + e.getMessage());
        }
    }

    /**
     * 重新编辑任务
     **/
    public ReturnT<String> reschedule(JobInfo jobInfo){
        if (!CronExpression.isValidExpression(jobInfo.getCronExpression())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”");
        }
        if (StringUtils.isEmpty(jobInfo.getDescription())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“任务描述”");
        }

        JobInfo existsJobInfo = jobInfoRepository.getOne(jobInfo.getId());
        if (existsJobInfo == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "参数异常");
        }

        existsJobInfo.setCronExpression(jobInfo.getCronExpression());
        existsJobInfo.setDescription(jobInfo.getDescription());
        jobInfoRepository.saveAndFlush(existsJobInfo);

        // fresh quartz
        String jobGroup = String.valueOf(existsJobInfo.getJobGroup());
        String jobName = String.valueOf(existsJobInfo.getId());
        try {
            boolean ret = JobDynamicScheduler.rescheduleJob(schedulerFactoryBean.getScheduler(),jobGroup, jobName, existsJobInfo.getCronExpression());
            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }

        return ReturnT.FAIL;
    }

    /**
     * 删除任务
     **/
    public ReturnT<String> remove(int id){
        JobInfo jobInfo = jobInfoRepository.getOne(id);
        String group = jobInfo.getJobGroup();
        String name = String.valueOf(jobInfo.getId());

        try {
            JobDynamicScheduler.removeJob(schedulerFactoryBean.getScheduler(),name, group);
            jobInfoRepository.delete(id);
            return ReturnT.SUCCESS;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
        return ReturnT.FAIL;
    }

    /**
     * 暂停任务
     **/
    public ReturnT<String> pause(int id){
        JobInfo jobInfo = jobInfoRepository.getOne(id);
        String group = jobInfo.getJobGroup();
        String name = String.valueOf(jobInfo.getId());

        try {
            boolean ret = JobDynamicScheduler.pauseJob(schedulerFactoryBean.getScheduler(),name, group);	// jobStatus do not store
            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return ReturnT.FAIL;
        }
    }

    /**
     * 恢复任务
     **/
    public ReturnT<String> resume(int id){
        JobInfo jobInfo = jobInfoRepository.getOne(id);
        String group = jobInfo.getJobGroup();
        String name = String.valueOf(jobInfo.getId());

        try {
            boolean ret = JobDynamicScheduler.resumeJob(schedulerFactoryBean.getScheduler(),name, group);
            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return ReturnT.FAIL;
        }
    }

    /**
     * 执行任务
     **/
    public ReturnT<String> triggerJob(int id){
        JobInfo jobInfo = jobInfoRepository.getOne(id);
        if (jobInfo == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "任务ID非法");
        }

        String group = jobInfo.getJobGroup();
        String name = String.valueOf(jobInfo.getId());

        try {
            JobDynamicScheduler.triggerJob(schedulerFactoryBean.getScheduler(),name, group, jobInfo.getCronExpression(),jobInfo.getJobClass());
            return ReturnT.SUCCESS;
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
            return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
        }
    }


}
