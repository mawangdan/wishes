package cn.edu.xmu.plack.news.model.dto;

import lombok.Data;

@Data
public class NewsDTO {
    String title;
    String author;
    String type;
    String content;
    String browseOrder;
    String favorOrder;
    String collectOrder;
}
