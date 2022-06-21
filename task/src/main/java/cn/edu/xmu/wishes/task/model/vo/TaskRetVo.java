package cn.edu.xmu.wishes.task.model.vo;

import lombok.Data;

@Data
public class TaskRetVo {
    private Long id;

    private Long initiatorId;

    private Long receiverId;

    private String type;

    private String title;

    private String description;

    private String location;

    private String imageUrl;

    private String price;

    private Byte state;
}
