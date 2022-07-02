package cn.edu.xmu.plack.news.controller;

import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ResponseUtil;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.news.model.vo.CategoryVo;
import cn.edu.xmu.plack.news.service.NewsConnectService;
import cn.edu.xmu.plack.news.model.po.News;
import cn.edu.xmu.plack.news.service.NewsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsConnectService newsConnectService;


    @ApiOperation(value = "添加新闻")
    @PostMapping("/news/add")
    public Object addNews(@RequestBody News news
    ){
        newsService.addNews(news);
        return Common.decorateReturnObject(new ReturnObject());
    }

    @ApiOperation(value = "查找新闻")
    @GetMapping("/news/{news_id}")
    public Object getNewsById(@PathVariable("news_id") Integer id
    ){
        return Common.decorateReturnObject(new ReturnObject(newsService.getNewsById(id)));
    }

    @ApiOperation(value = "查找新闻的浏览，喜欢，收藏次数")
    @GetMapping("/newsconnect/{news_id}")
    public Object getNewsconnectByNewsId(@PathVariable("news_id") Integer id
    ){
        return Common.decorateReturnObject(newsConnectService.getNewsconnectByNewsId(id));
    }

    @ApiOperation(value = "查找所有新闻分页")
    @GetMapping("/newsallpage")
    public Object getNewsallpageById(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer pageSize
    ){
        return Common.decorateReturnObject(newsService.getNewsallpageById(page,pageSize));
    }

    @ApiOperation(value = "查找指定类型新闻")
    @GetMapping("/category/{newstype}")
    public Object getNewsByType(@PathVariable("newstype") String newstype
    ){
        News exampleNews = News.builder()
                .newsType(newstype)
                .build();
        return Common.decorateReturnObject(newsService.listAllNewsByExampleAndPage(exampleNews));
    }

    @ApiOperation(value = "查找指定类型新闻分页")
    @GetMapping("/categorypage/{newstype}")
    public Object getNewsByTypePage(@PathVariable("newstype") String newstype,@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize
    ){
        News exampleNews = News.builder()
                .newsType(newstype)
                .build();
        return Common.decorateReturnObject(newsService.listNewsByExampleAndPage(exampleNews,page,pageSize));
    }

    @ApiOperation(value = "查找用户浏览的新闻")
    @GetMapping("/liulan")
    public Object getNewsByLiuLan(@RequestParam(required = false) Long userId
    ){
        return Common.decorateReturnObject(newsConnectService.queryAllByType(userId,"浏览"));
    }

    @ApiOperation(value = "查找用户喜欢的新闻")
    @GetMapping("/xihuan")
    public Object getNewsByXiHuan(@RequestParam(required = false) Long userId
    ){
        return Common.decorateReturnObject(newsConnectService.queryAllByType(userId,"喜欢"));
    }

    @ApiOperation(value = "查找用户收藏的新闻")
    @GetMapping("/shoucang")
    public Object getNewsByShouCang(@RequestParam(required = false) Long userId
    ){
        return Common.decorateReturnObject(newsConnectService.queryAllByType(userId,"收藏"));
    }

    @ApiOperation(value = "用户浏览新闻")
    @PostMapping("/liulan")
    public Object addNewsByLiuLan(@RequestParam(required = true) Long userId,@RequestParam(required = true) Long newsId
    ){
        return Common.decorateReturnObject(newsConnectService.liulan(userId,newsId));
    }

    @ApiOperation(value = "用户喜欢新闻")
    @PostMapping("/xihuan")
    public Object addNewsByXiHuan(@RequestParam(required = true) Long userId,@RequestParam(required = true) Long newsId
    ){
        return Common.decorateReturnObject(newsConnectService.xiHuan(userId,newsId));
    }

    @ApiOperation(value = "用户收藏新闻")
    @PostMapping("/shoucang")
    public Object addNewsByShouCang(@RequestParam(required = true) Long userId,@RequestParam(required = true) Long newsId
    ){
        return Common.decorateReturnObject(newsConnectService.shouCang(userId,newsId));
    }

    /**
     * 获取用户喜欢，浏览，收藏的新闻数量
     */
    @ApiOperation("获取用户喜欢，浏览，收藏的新闻数量")
    @GetMapping("/news/user/summary")
    public Object getUserNewsSummary(@RequestParam Long userId) {
        return Common.decorateReturnObject(newsConnectService.getUserNewsSummary(userId));
    }

    /**
     * 获取所有新闻类型
     */
    @ApiOperation("获取所有新闻类型")
    @GetMapping("/news/category")
    public Object getAllNewsCategory() {
        EnumSet<News.NewsType> enumSet = EnumSet.allOf(News.NewsType.class);
        List<CategoryVo> newsTypeList = enumSet.stream().map(x -> new CategoryVo((long) x.getCode(), x.getDesc())).collect(Collectors.toUnmodifiableList());
        return ResponseUtil.ok(newsTypeList);
    }
}
