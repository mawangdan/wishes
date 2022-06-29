package cn.edu.xmu.wishes.news.service;

import cn.edu.xmu.wishes.core.util.Common;
import cn.edu.xmu.wishes.core.util.ReturnObject;
import cn.edu.xmu.wishes.news.mapper.NewsMapper;
import cn.edu.xmu.wishes.news.model.po.News;
import cn.edu.xmu.wishes.task.mapper.TaskMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.vo.TaskRetVo;
import cn.edu.xmu.wishes.user.mapper.UserMapper;
import cn.edu.xmu.wishes.user.model.po.User;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Optional.ofNullable(exampleTask.getAuthor()).ifPresent(x -> queryWrapper.eq(News::getAuthor, x));
        Optional.ofNullable(exampleTask.getContent()).ifPresent(x -> queryWrapper.eq(News::getContent, x));
        Optional.ofNullable(exampleTask.getNewsTitle()).ifPresent(x -> queryWrapper.eq(News::getNewsTitle, x));
        Optional.ofNullable(exampleTask.getNewsType()).ifPresent(x -> queryWrapper.eq(News::getNewsType, x));

        return queryWrapper;
    }
    public ReturnObject listNewsByExampleAndPage(News exampleNews, Integer page, Integer pageSize) {
        // get data
        Page<News> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<News> queryWrapper = getQueryWrapperByNews(exampleNews);
        Page<News> newsPage = this.baseMapper.selectPage(taskPage, queryWrapper);
        return new ReturnObject(newsPage);
    }
    public ReturnObject listAllNewsByExampleAndPage(News exampleNews){
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


}
