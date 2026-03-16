package com.ngsliuji.ngsaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ngsliuji.ngsaicodemother.mapper")
public class NgsAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(NgsAiCodeMotherApplication.class, args);
    }

}
