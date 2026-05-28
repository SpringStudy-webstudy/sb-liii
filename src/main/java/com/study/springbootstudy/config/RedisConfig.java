package com.study.springbootstudy.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching // 👈 애플리케이션의 캐시 기능을 활성화합니다.
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Redis 캐시의 기본 설정을 구성합니다.
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 7주차 사전 학습 내용: TTL 설정 (예시로 1분 설정)
                .entryTtl(Duration.ofMinutes(1))
                // 데이터 저장 시 JSON 포맷으로 변환되도록 직렬화 설정
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}