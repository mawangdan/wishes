package cn.edu.xmu.plack.news.service;

import cn.edu.xmu.plack.core.util.ReturnNo;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.news.mapper.NewsConnectMapper;
import cn.edu.xmu.plack.news.model.po.News;
import cn.edu.xmu.plack.news.model.po.NewsConnect;
import cn.edu.xmu.plack.news.model.po.NewsConnectCount;
import cn.edu.xmu.plack.news.model.vo.UserNewsSummary;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service("newsConnectService")
public class NewsConnectService extends ServiceImpl<NewsConnectMapper, NewsConnect> {
    @Autowired
    private NewsService newsService;

    private LambdaQueryWrapper<NewsConnect> getQueryWrapperByNewsConnect(NewsConnect exampleTask) {
        LambdaQueryWrapper<NewsConnect> queryWrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(exampleTask.getNewsId()).ifPresent(x -> queryWrapper.eq(NewsConnect::getNewsId, x));
        Optional.ofNullable(exampleTask.getUserId()).ifPresent(x -> queryWrapper.eq(NewsConnect::getUserId, x));
        Optional.ofNullable(exampleTask.getConnectType()).ifPresent(x -> queryWrapper.eq(NewsConnect::getConnectType, x));
        return queryWrapper;
    }
    private ReturnObject<Page<NewsConnect> > listNewsConnectByExampleAndPage(NewsConnect exampleNewsConnect, Integer page, Integer pageSize) {
        // get data
        Page<NewsConnect> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<NewsConnect> queryWrapper = getQueryWrapperByNewsConnect(exampleNewsConnect);
        Page<NewsConnect> newsPage = this.baseMapper.selectPage(taskPage, queryWrapper);
        return new ReturnObject<>(newsPage);
    }

    private ReturnObject<List<NewsConnect>> listNewsConnectByExample(NewsConnect exampleNewsConnect){
        LambdaQueryWrapper<NewsConnect> queryWrapper = getQueryWrapperByNewsConnect(exampleNewsConnect);
        List<NewsConnect> news = baseMapper.selectList(queryWrapper);
        return new ReturnObject<>(news);
    }

    @Transactional
    public ReturnObject liulan(Long userId,Long newsId){
        NewsConnect newsConnect=NewsConnect.builder()
                .connectType("浏览")
                .newsId(newsId)
                .userId(userId).build();
        baseMapper.insert(newsConnect);

        LambdaUpdateWrapper<News> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(News::getId, newsId)
                .setSql("browse_count=browse_count+1");
        newsService.update(lambdaUpdateWrapper);
        return new ReturnObject();
    }
    public ReturnObject shouCang(Long userId,Long newsId){
        NewsConnect newsConnect=NewsConnect.builder()
                .connectType("收藏")
                .newsId(newsId)
                .userId(userId).build();
        if(baseMapper.selectOne(getQueryWrapperByNewsConnect(newsConnect))!=null){
            return new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST, "无法重复收藏");
        }
        baseMapper.insert(newsConnect);

        LambdaUpdateWrapper<News> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(News::getId, newsId)
                .setSql("collect_count=collect_count+1");
        newsService.update(lambdaUpdateWrapper);
        return new ReturnObject();
    }
    public ReturnObject xiHuan(Long userId,Long newsId){
        NewsConnect newsConnect=NewsConnect.builder()
                .connectType("喜欢")
                .newsId(newsId)
                .userId(userId).build();
        if(baseMapper.selectOne(getQueryWrapperByNewsConnect(newsConnect))!=null){
            return new ReturnObject("无法重复喜欢");
        }
        baseMapper.insert(newsConnect);

        LambdaUpdateWrapper<News> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper
                .eq(News::getId, newsId)
                .setSql("favor_count=favor_count+1");
        newsService.update(lambdaUpdateWrapper);
        return new ReturnObject();
    }

    public ReturnObject queryAllByType(Long userId,String type){
        ReturnObject returnObject = listNewsConnectByExample(NewsConnect.builder()
                .connectType(type)
                .userId(userId).build());
        return returnObject;
    }

    public ReturnObject getNewsConnectByNewsId(Long id) {
        News news = newsService.getById(id);
        if (news == null) {
            return ReturnObject.RESOURCE_ID_NOTEXIST_RET;
        }
        return new ReturnObject(new NewsConnectCount(id, news.getFavorCount(), news.getCollectCount(), news.getBrowseCount()));
    }

    public ReturnObject getUserNewsSummary(Long userId) {
        NewsConnect newsConnectExample = NewsConnect.builder()
                .userId(userId)
                .build();
        ReturnObject<List<NewsConnect>> returnObject = listNewsConnectByExample(newsConnectExample);
        List<NewsConnect> newsConnectList = returnObject.getData();
        Map<String, Integer> summaryMap = new HashMap<>();
        summaryMap.put("喜欢", 0);
        summaryMap.put("浏览", 0);
        summaryMap.put("收藏", 0);
        newsConnectList.stream().forEach(x -> {
            summaryMap.put(x.getConnectType(), summaryMap.getOrDefault(x.getConnectType(), 0) + 1);
        });
        return new ReturnObject(new UserNewsSummary(summaryMap.get("喜欢"), summaryMap.get("收藏"), summaryMap.get("浏览")));
    }


    public ReturnObject getUserSummary(Integer n) {
        QueryWrapper<NewsConnect> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("CONVERT(gmt_create,date) as date, connect_type as connectType, COUNT(1) as count");
        queryWrapper.ge("gmt_create", LocalDate.now().minusDays(n - 1));
        queryWrapper.groupBy("date, connect_type");
        List<Map<String, Object>> maps = this.listMaps(queryWrapper);
        return new ReturnObject(maps);
    }

    public ReturnObject getUserVisitProportion(String type, Integer n) {
        QueryWrapper<NewsConnect> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("news_id as newsId, COUNT(1) as count");
        queryWrapper.ge("gmt_create", LocalDate.now().minusDays(n - 1));
        queryWrapper.eq("connect_type", type);
        queryWrapper.groupBy("news_id");
        List<Map<String, Object>> maps = this.listMaps(queryWrapper);
        Map<String, Long> visitMap = new HashMap<>();
        maps.stream().forEach(x -> {
            Long newsId = (Long) x.get("newsId");
            News news = newsService.getNewsById(newsId);
            String newsType = news.getNewsType();
            visitMap.put(newsType, visitMap.getOrDefault(newsType, 0L) + 1);
        });
        List<Map<String, ? extends Serializable>> visitList = visitMap.entrySet().stream().map(entry -> Map.of("name", entry.getKey(), "count", entry.getValue())).collect(Collectors.toList());
        return new ReturnObject(visitList);
    }

    public ReturnObject getUserVisitNum(Integer n) {
        QueryWrapper<NewsConnect> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("connect_type as connectType, COUNT(1) as count");
        queryWrapper.ge("gmt_create", LocalDate.now().minusDays(n - 1));
        queryWrapper.groupBy("connect_type");
        List<Map<String, Object>> maps = this.listMaps(queryWrapper);
        return new ReturnObject(maps);
    }
}
