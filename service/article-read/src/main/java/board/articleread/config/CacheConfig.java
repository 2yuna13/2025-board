package board.articleread.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(
                        Map.of(
                                // "articleViewCount" 캐시에 저장된 데이터는 1초 후 자동으로 삭제
                                "articleViewCount", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(1))
                        )
                )
                .build();
    }
}
