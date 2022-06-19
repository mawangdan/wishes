package cn.edu.xmu.wishes.task.model.vo;

import lombok.Data;

@Data
public class TaskDraftRetVo {
    private Long taskId;

    private Long initiatorId;

    private Byte type;

    private String title;

    private String description;

    private String location;

    private String imageUrl;

    private String price;

    private Byte state;
}
