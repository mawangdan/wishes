package cn.edu.xmu.wishes.task.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskDraftVo {
    private Long initiatorId;

    private Byte type;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    private String imageUrl;

    private String price;
}
