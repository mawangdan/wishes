package cn.edu.xmu.plack.news.service;

import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.news.mapper.NewsConnectMapper;
import cn.edu.xmu.plack.news.model.po.News;
import cn.edu.xmu.plack.news.model.po.NewsConnect;
import cn.edu.xmu.plack.news.model.po.NewsConnectCount;
import cn.edu.xmu.plack.news.model.vo.UserNewsSummary;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("newsConnectService")
public class NewsConnectService extends ServiceImpl<NewsConnectMapper, NewsConnect> {

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

    public ReturnObject liulan(Long userId,Long newsId){
        NewsConnect newsConnect=NewsConnect.builder()
                .connectType("浏览")
                .newsId(newsId)
                .userId(userId).build();
        baseMapper.insert(newsConnect);
        return new ReturnObject();
    }
    public ReturnObject shouCang(Long userId,Long newsId){
        NewsConnect newsConnect=NewsConnect.builder()
                .connectType("收藏")
                .newsId(newsId)
                .userId(userId).build();
        if(baseMapper.selectOne(getQueryWrapperByNewsConnect(newsConnect))!=null){
            return new ReturnObject("无法重复收藏");
        }
        baseMapper.insert(newsConnect);
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
        return new ReturnObject();
    }

    public ReturnObject queryAllByType(Long userId,String type){
        ReturnObject returnObject = listNewsConnectByExample(NewsConnect.builder()
                .connectType(type)
                .userId(userId).build());
        return returnObject;
    }

    public ReturnObject getNewsconnectByNewsId(Integer id) {
        LambdaQueryWrapper<NewsConnect> queryWrapper = getQueryWrapperByNewsConnect(NewsConnect.builder().newsId(id.longValue()).build());
        List<NewsConnect> newsConnects=baseMapper.selectList(queryWrapper);
        int liuLan=0,xiHuan=0,shouCang=0;
        for (NewsConnect newsConnect :newsConnects) {
            if (newsConnect.getConnectType().equals("喜欢")) {
                xiHuan++;
            }else if(newsConnect.getConnectType().equals("收藏")){
                shouCang++;
            } else if (newsConnect.getConnectType().equals("浏览")) {
                liuLan++;
            }
        }
        return new ReturnObject(new NewsConnectCount(id,xiHuan,shouCang,liuLan));
    }

    public ReturnObject getUserNewsSummary(Long userId) {
        NewsConnect newsConnectExample = NewsConnect.builder()
                .userId(userId)
                .build();
        ReturnObject<List<NewsConnect>> returnObject = listNewsConnectByExample(newsConnectExample);
        List<NewsConnect> newsConnectList = returnObject.getData();
        int favorCount, collectCount, browseCount;
        Map<String, Integer> summaryMap = new HashMap<>();
        summaryMap.put("喜欢", 0);
        summaryMap.put("浏览", 0);
        summaryMap.put("收藏", 0);
        newsConnectList.stream().forEach(x -> {
            summaryMap.put(x.getConnectType(), summaryMap.getOrDefault(x.getConnectType(), 0) + 1);
        });
        return new ReturnObject(new UserNewsSummary(summaryMap.get("喜欢"), summaryMap.get("收藏"), summaryMap.get("浏览")));
    }
}
