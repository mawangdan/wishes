package cn.edu.xmu.plack.news.controller;

import cn.edu.xmu.plack.core.constant.Constants;
import cn.edu.xmu.plack.core.util.Common;
import cn.edu.xmu.plack.core.util.ReturnObject;
import cn.edu.xmu.plack.news.service.NewsConnectService;
import cn.edu.xmu.plack.news.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/news/statistics", produces = "application/json;charset=UTF-8")
@Slf4j
public class NewsStatisticsController {
    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsConnectService newsConnectService;

    /**
     * 统计beginDate到endDate内的发布的总新闻数
     * @param beginDate
     * @param endDate
     * @return
     */
    @GetMapping("/count")
    public Object getNewsCount(@RequestParam(required = false) @DateTimeFormat(pattern = Constants.INPUT_DATE_TIME_FORMAT) LocalDateTime beginDate,
                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.INPUT_DATE_TIME_FORMAT) LocalDateTime endDate
    ) {
        if (beginDate != null && endDate != null && beginDate.isAfter(endDate)) {
            return Common.decorateReturnObject(ReturnObject.FIELD_NOTVALID_RET);
        }
        return Common.decorateReturnObject(newsService.getNewsCount(beginDate, endDate));
    }

    /**
     * 统计beginDate到endDate内的发布的各种类型新闻的数量
     * @param beginDate
     * @param endDate
     * @return
     */
    @GetMapping("/type/count")
    public Object getNewsTypeCount(@RequestParam(required = false) @DateTimeFormat(pattern = Constants.INPUT_DATE_TIME_FORMAT) LocalDateTime beginDate,
                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.INPUT_DATE_TIME_FORMAT) LocalDateTime endDate
    ) {
        if (beginDate != null && endDate != null && beginDate.isAfter(endDate)) {
            return Common.decorateReturnObject(ReturnObject.FIELD_NOTVALID_RET);
        }
        return Common.decorateReturnObject(newsService.getNewsTypeCount(beginDate, endDate));
    }

    /**
     * 返回过去n天内每天发表的新闻数,包括今天
     * @param n
     * @return
     */
    @GetMapping("/count/day")
    public Object getNewsAddition(@RequestParam(defaultValue = "7") Integer n
    ) {
        return Common.decorateReturnObject(newsService.getNewsAddition(n));
    }

    /**
     * 获取过去n天内所有用户的行为统计
     * @param n
     * @return
     */
    @GetMapping("/userSummary")
    public Object getUserSummary(@RequestParam(defaultValue = "7") Integer n
    ) {
        return Common.decorateReturnObject(newsConnectService.getUserSummary(n));
    }
}
