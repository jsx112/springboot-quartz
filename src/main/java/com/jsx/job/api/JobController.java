package com.jsx.job.api;
import com.jsx.job.entity.JobInfo;
import com.jsx.job.service.JobInfoService;
import com.jsx.entry.ReturnT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 任务管理
 *
 * @author jiasx
 * @create 2017-09-21 15:04
 **/
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobInfoService jobInfoService;

    @RequestMapping("/list")
    public ReturnT<List<JobInfo>> list(JobInfo jobInfo) {
        return jobInfoService.list(jobInfo);
    }

    @RequestMapping("/add")
    public ReturnT<String> add(JobInfo jobInfo) {
        return jobInfoService.add(jobInfo);
    }

    @RequestMapping("/reschedule")
    public ReturnT<String> reschedule(JobInfo jobInfo) {
        return jobInfoService.reschedule(jobInfo);
    }

    @RequestMapping("/remove")
    public ReturnT<String> remove(int id) {
        return jobInfoService.remove(id);
    }

    @RequestMapping("/pause")
    public ReturnT<String> pause(int id) {
        return jobInfoService.pause(id);
    }

    @RequestMapping("/resume")
    public ReturnT<String> resume(int id) {
        return jobInfoService.resume(id);
    }

    @RequestMapping("/trigger")
    public ReturnT<String> triggerJob(int id) {
        return jobInfoService.triggerJob(id);
    }
}
