package board.view.service;

import board.view.repository.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleViewService {
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackUpProcessor articleViewCountBackUpProcessor;
    private static final int BACK_UP_BACH_SIZE = 100;

    // 조회수 증가
    public Long increase(Long articleId, Long userId) {
        Long count = articleViewCountRepository.increase(articleId);
        if (count % BACK_UP_BACH_SIZE == 0) {
            articleViewCountBackUpProcessor.backup(articleId, count);
        }
        return count;
    }

    // 조회수 조회
    public Long count(Long articleId) {
        return articleViewCountRepository.read(articleId);
    }
}
