package com.example.base_module.cache;

import java.time.Duration;
import java.util.Random;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

/**
 * Redis-backed {@link org.springframework.cache.CacheManager}: JSON values + TTL (entities are not
 * {@link java.io.Serializable} for JDK serialization).
 */
@Configuration
public class RedisCacheConfig {

	@Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer json = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Random TTL
        Duration ttl = Duration.ofMinutes(new Random().nextInt(10) + 10);

        RedisCacheConfiguration defaults = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(json))
                // Use lock to avoid stampede
                .enableTimeToIdle();

        return builder -> builder
                .cacheDefaults(defaults)
                .enableStatistics(); // Hit/miss rate
    }
}
