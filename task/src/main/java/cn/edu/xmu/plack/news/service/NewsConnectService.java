package cn.edu.xmu.plack.news.service;

import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.news.mapper.NewsConnectMapper;
import cn.edu.xmu.plack.news.model.po.NewsConnect;
import cn.edu.xmu.plack.news.model.po.NewsConnectCount;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("newsConnectService")
public class NewsConnectService extends ServiceImpl<NewsConnectMapper, NewsConnect> {

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
        ReturnObject returnObject = listAllNewsConnectByExampleAndPage(NewsConnect.builder()
                .connectType(type)
                .userId(userId).build());
        return returnObject;
    }

    private LambdaQueryWrapper<NewsConnect> getQueryWrapperByNewsConnect(NewsConnect exampleTask) {
        LambdaQueryWrapper<NewsConnect> queryWrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(exampleTask.getNewsId()).ifPresent(x -> queryWrapper.eq(NewsConnect::getNewsId, x));
        Optional.ofNullable(exampleTask.getUserId()).ifPresent(x -> queryWrapper.eq(NewsConnect::getUserId, x));
        Optional.ofNullable(exampleTask.getConnectType()).ifPresent(x -> queryWrapper.eq(NewsConnect::getConnectType, x));
        return queryWrapper;
    }
    public ReturnObject listNewsConnectByExampleAndPage(NewsConnect exampleNewsConnect, Integer page, Integer pageSize) {
        // get data
        Page<NewsConnect> taskPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<NewsConnect> queryWrapper = getQueryWrapperByNewsConnect(exampleNewsConnect);
        Page<NewsConnect> newsPage = this.baseMapper.selectPage(taskPage, queryWrapper);
        return new ReturnObject(newsPage);
    }
    public ReturnObject listAllNewsConnectByExampleAndPage(NewsConnect exampleNewsConnect){
        LambdaQueryWrapper<NewsConnect> queryWrapper = getQueryWrapperByNewsConnect(exampleNewsConnect);
        List<NewsConnect> news = baseMapper.selectList(queryWrapper);
        return new ReturnObject(news);
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
}
