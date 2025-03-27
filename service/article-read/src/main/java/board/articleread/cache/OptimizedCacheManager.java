package board.articleread.cache;

import board.common.dataserializer.DataSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;

@Component
@RequiredArgsConstructor
public class OptimizedCacheManager {
    private final StringRedisTemplate redisTemplate;
    private final OptimizedCacheLockProvider optimizedCacheLockProvider;

    private static final String DELIMITER = "::";

    public Object process(String type, long ttlSeconds, Object[] args, Class<?> returnType,
                          OptimizedCacheOriginDataSupplier<?> originDataSupplier) throws Throwable {
        String key = generateKey(type, args);

        String cachedData = redisTemplate.opsForValue().get(key);
        // 캐시 데이터 x -> 원본 데이터 조회 후 캐시 저장
        if (cachedData == null) {
            return refresh(originDataSupplier, key, ttlSeconds);
        }

        OptimizedCache optimizedCache = DataSerializer.deserialize(cachedData, OptimizedCache.class);
        // 역질렬화 실패 (데이터가 잘못된 경우?) -> 원본 데이터 조회 후 캐시 저장
        if (optimizedCache == null) {
            return refresh(originDataSupplier, key, ttlSeconds);
        }


        // 캐시 만료 x -> 캐시 데이터 반환 (정상 처리)
        if (!optimizedCache.isExpired()) {
            return optimizedCache.parseData(returnType);
        }

        // 캐시 만료 & 다른 요청 캐시 갱신 -> 기존 캐시 데이터 반환
        if (!optimizedCacheLockProvider.lock(key)) {
            return optimizedCache.parseData(returnType);
        }

        // 캐시 갱신 (락 획득 성공 시)
        try {
            return refresh(originDataSupplier, key, ttlSeconds);
        } finally {
            optimizedCacheLockProvider.unlock(key);
        }
    }

    private Object refresh(OptimizedCacheOriginDataSupplier<?> originDataSupplier, String key, long ttlSeconds) throws Throwable {
        Object result = originDataSupplier.get();

        OptimizedCacheTTL optimizedCacheTTL = OptimizedCacheTTL.of(ttlSeconds);
        OptimizedCache optimizedCache = OptimizedCache.of(result, optimizedCacheTTL.getLogicalTTL());

        redisTemplate.opsForValue()
                .set(
                        key,
                        DataSerializer.serialize(optimizedCache),
                        optimizedCacheTTL.getPhysicalTTL()
                );

        return result;
    }

    private String generateKey(String prefix, Object[] args) {
        return prefix + DELIMITER +
                Arrays.stream(args)
                        .map(String::valueOf)
                        .collect(joining(DELIMITER));
    }
}
