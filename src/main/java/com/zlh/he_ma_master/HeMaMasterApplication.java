package com.zlh.he_ma_master;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lh
 */
@SpringBootApplication
@MapperScan("com.zlh.he_ma_master.dao")
@EnableScheduling
public class HeMaMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeMaMasterApplication.class, args);
    }

}
