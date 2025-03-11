package board.hotarticle.controller;

import board.hotarticle.service.HotArticleService;
import board.hotarticle.service.response.HotArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotArticleController {
    private final HotArticleService hotArticleService;

    @GetMapping("/v1/hot-article/articles/data/{dataStr}")
    public List<HotArticleResponse> readAll(
            @PathVariable("dataStr") String dataStr
    ) {
        return hotArticleService.readAll(dataStr);
    }
}
