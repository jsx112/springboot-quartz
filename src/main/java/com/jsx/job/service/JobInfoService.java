package com.jsx.job.service;


import com.jsx.job.entity.JobInfo;
import com.jsx.entry.ReturnT;

import java.util.List;

/**
 * 同步job服务
 * 
 * <pre>
 * [
 * 调用关系:
 * 实现接口及父类:
 * 子类:
 * 内部类列表:
 * ]
 * </pre>
 * 
 * @author 贾世雄
 * @since 1.0
 * @version 2017年6月7日 贾世雄
 */
public interface JobInfoService {

    /**
    * 任务列表
    **/
    public ReturnT<List<JobInfo>> list(JobInfo jobInfo);

    /**
    * 新增一个任务
    **/
    public ReturnT<String> add(JobInfo jobInfo);

    /**
    * 重新编辑任务
    **/
    public ReturnT<String> reschedule(JobInfo jobInfo);

    /**
    * 删除任务
    **/
    public ReturnT<String> remove(int id);

    /**
     * 暂停任务
     **/
    public ReturnT<String> pause(int id);

    /**
     * 恢复任务
     **/
    public ReturnT<String> resume(int id);

    /**
     * 执行任务
     **/
    public ReturnT<String> triggerJob(int id);
    
}
