package com.jsx.job.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
* 定时任务对象
* @author jiasx
* @create 2017/9/21 14:57
**/
@Entity
@Table(name = "TBL_JOB_INFO")
public class JobInfo implements Serializable{
    /** FIXME */
    private static final long serialVersionUID = -6228183011286237991L;
    /**
    * 任务主键ID，在数据库中生成
     * 作为任务/触发器key，目前这两个设计成一致，也就是一个触发器和一个任务是一一对应关系
    **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * 任务分组名称,根据项目来的，一个项目一个group，所以这个在此默认为defaultGroup
     */
    private String jobGroup = "defaultGroup";

    /**
     * 任务描述
     */
    private String description;

    /**
     * 定时任务类，全路径
     */
    private String jobClass;
    /**
     * 表达式
     */
    private String cronExpression;
    
    /**
     * 数据,用json字符串去存储，目前没有意义
     */
    private String data;

    // 从quartz中获取
    @Transient
    private String status;		// 任务状态 【base on quartz】


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
