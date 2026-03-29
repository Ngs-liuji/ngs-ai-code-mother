package com.ngsliuji.ngsaicodemother;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//在启动类中排除 em؜b؜e؜dding؜ 的؜自动؜装配，因为本项目用不到，禁用的是向量数据库RAG会用到，但是这个项目没有
@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})

@MapperScan("com.ngsliuji.ngsaicodemother.mapper")
public class NgsAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(NgsAiCodeMotherApplication.class, args);
    }

}
