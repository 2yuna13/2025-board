package board.comment.controller;

import board.comment.service.CommentServiceV2;
import board.comment.service.request.CommentCreateRequestV2;
import board.comment.service.response.CommentPageResponse;
import board.comment.service.response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentControllerV2 {
    private final CommentServiceV2 commentService;

    //댓글 조회
    @GetMapping("/v2/comments/{commentId}")
    public CommentResponse read(@PathVariable("commentId") Long commentId) {
        return commentService.read(commentId);
    }

    //댓글 목록 조회 - 페이지 번호 방식
    @GetMapping("/v2/comments")
    public CommentPageResponse readAll(
            @RequestParam("articleId") Long articleId,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize
    ) {
        return commentService.readAll(articleId, page, pageSize);
    }

    //댓글 목록 조회 - 무한 스크롤 방식
    @GetMapping("/v2/comments/infinite-scroll")
    public List<CommentResponse> readAllInfiniteScroll(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "lastPath", required = false) String lastPath,
            @RequestParam("pageSize") Long pageSize
    ) {
        return commentService.readAllInfiniteScroll(articleId, lastPath, pageSize);
    }

    //댓글 생성
    @PostMapping("/v2/comments")
    public CommentResponse create(@RequestBody CommentCreateRequestV2 request) {
        return commentService.create(request);
    }

    //댓글 삭제
    @DeleteMapping("/v2/comments/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.delete(commentId);
    }

    //댓글 수 조회
    @GetMapping("/v2/comments/articles/{articleId}/count")
    public Long count(@PathVariable Long articleId) {
        return commentService.count(articleId);
    }
}
