package cn.edu.xmu.plack.news.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewsVo {
    @NotBlank(message = "新闻标题不能为空")
    public String newsTitle;
    public String newsType;
    public String author;
    @NotBlank(message = "新闻内容不能为空")
    public String content;
}
