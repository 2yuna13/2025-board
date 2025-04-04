package board.articleread.cache;

import lombok.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.*;

class OptimizedCacheTest {
    @Test
    void parseDataTest() {
        parseDataTest("data", 10);
        parseDataTest(3L, 10);
        parseDataTest(3, 10);
        parseDataTest(new TestClass("test"), 10);
    }

    void parseDataTest(Object data, long ttlSeconds) {
        // given
        OptimizedCache optimizedCache = OptimizedCache.of(data, Duration.ofSeconds(ttlSeconds));
        System.out.println("optimizedCache = " + optimizedCache);

        // when
        Object resolvedData = optimizedCache.parseData(data.getClass());

        // then
        System.out.println("resolvedData = " + resolvedData);
        assertThat(resolvedData).isEqualTo(data);
    }

    @Test
    void isExpiredTest() {
        // 만료된 캐시
        assertThat(OptimizedCache.of("data", Duration.ofSeconds(-30)).isExpired()).isTrue();
        // 유효한 캐시
        assertThat(OptimizedCache.of("data", Duration.ofSeconds(30)).isExpired()).isFalse();
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestClass {
        String testData;
    }
}