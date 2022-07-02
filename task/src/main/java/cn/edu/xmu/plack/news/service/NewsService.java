package cn.edu.xmu.plack.news.service;

import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.news.mapper.NewsMapper;
import cn.edu.xmu.plack.news.model.dto.NewsDTO;
import cn.edu.xmu.plack.news.model.po.News;
import cn.edu.xmu.plack.news.model.vo.NewsVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service("newsService")
public class NewsService extends ServiceImpl<NewsMapper, News>{
    @Autowired
    public NewsMapper newsMapper;
    public News getNewsById(Integer id){
        return getById(id);
    }

    public int addNews(News news){
       return newsMapper.insert(news);
    }

    private LambdaQueryWrapper<News> getQueryWrapperByNews(News exampleTask) {
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(exampleTask.getAuthor())
                .filter(StringUtils::hasLength).ifPresent(x -> queryWrapper.eq(News::getAuthor, x));
        Optional.ofNullable(exampleTask.getContent())
                .filter(StringUtils::hasLength).ifPresent(x -> queryWrapper.like(News::getContent, String.format("%%%s%%", x)));
        Optional.ofNullable(exampleTask.getNewsTitle())
                .filter(StringUtils::hasLength).ifPresent(x -> queryWrapper.like(News::getNewsTitle, String.format("%%%s%%", x)));
        Optional.ofNullable(exampleTask.getNewsType())
                .filter(StringUtils::hasLength).ifPresent(x -> queryWrapper.eq(News::getNewsType, x));

        return queryWrapper;
    }
    public ReturnObject listNewsByExampleAndPage(News exampleNews, Integer page, Integer pageSize) {
        // get data
        Page<News> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<News> queryWrapper = getQueryWrapperByNews(exampleNews);
        Page<News> newsPage = this.baseMapper.selectPage(taskPage, queryWrapper);
        return new ReturnObject(newsPage);
    }
    public ReturnObject listNewsByExample(News exampleNews){
        LambdaQueryWrapper<News> queryWrapper = getQueryWrapperByNews(exampleNews);
        List<News> news = baseMapper.selectList(queryWrapper);
        return new ReturnObject(news);
    }

    public ReturnObject getNewsallpageById(Integer page, Integer pageSize) {
        Page<News> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        Page<News> newsPage = this.baseMapper.selectPage(taskPage, queryWrapper);
        return new ReturnObject(newsPage);
    }

    public ReturnObject listNewsPage(NewsDTO newsDTO, Integer page, Integer pageSize) {
        News newsExample = News.builder()
                .newsTitle(newsDTO.getTitle())
                .author(newsDTO.getAuthor())
                .newsType(newsDTO.getType())
                .content(newsDTO.getContent())
                .build();
        return listNewsByExampleAndPage(newsExample, page, pageSize);
    }

    public ReturnObject addNews(NewsVo newsVo) {
        News news = Common.cloneVo(newsVo, News.class);
        boolean isSave = this.save(news);
        if (!isSave) {
            return ReturnObject.INTERNAL_SERVER_ERR_RET;
        }
        return ReturnObject.OK_RET;
    }
}
