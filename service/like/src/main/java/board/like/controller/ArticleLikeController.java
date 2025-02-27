package board.like.controller;

import board.like.service.ArticleLikeService;
import board.like.service.response.ArticleLikeResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    // 좋아요 조회
    @GetMapping("v1/article-likes/articles/{articleId}/users/{userId}")
    public ArticleLikeResponse read(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        return articleLikeService.read(articleId, userId);
    }

    // 좋아요 수 조회
    @GetMapping("v1/article-likes/articles/{articleId}/count")
    public Long count(
            @PathVariable("articleId") Long articleId
    ) {
        return articleLikeService.count(articleId);
    }

    // 좋아요 추가 - 비관적 락(update 구문)
    @PostMapping("v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void likePessimisticLock1(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.likePessimisticLock1(articleId, userId);
    }

    // 좋아요 취소 - 비관적 락(update 구문)
    @DeleteMapping("v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1")
    public void unlikePessimisticLock1(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.unlikePessimisticLock1(articleId, userId);
    }

    // 좋아요 추가 - 비관적 락(for update + update)
    @PostMapping("v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void likePessimisticLock2(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.likePessimisticLock2(articleId, userId);
    }

    // 좋아요 취소 - 비관적 락(for update + update)
    @DeleteMapping("v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-2")
    public void unlikePessimisticLock2(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.unlikePessimisticLock2(articleId, userId);
    }

    // 좋아요 추가 - 낙관적 락(version 체크)
    @PostMapping("v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
    public void likeOptimisticLock(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.likeOptimisticLock(articleId, userId);
    }

    // 좋아요 취소 - 낙관적 락(version 체크)
    @DeleteMapping("v1/article-likes/articles/{articleId}/users/{userId}/optimistic-lock")
    public void unlikeOptimisticLock(
            @PathVariable("articleId") Long articleId,
            @PathVariable("userId") Long userId
    ) {
        articleLikeService.unlikeOptimisticLock(articleId, userId);
    }
}
