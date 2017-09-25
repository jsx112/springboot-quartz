package com.jsx;/**
 * Created by dell on 2017/9/15.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务启动类
 *
 * @author jiasx
 * @create 2017-09-15 11:03
 **/
@SpringBootApplication
@EnableScheduling
public class SpringbootQuartzApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootQuartzApp.class,args);
    }
}
