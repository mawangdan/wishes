package cn.edu.xmu.wishes.task.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class TaskVo {
    @NotNull(message = "任务类型为空")
    private Long typeId;

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    private String location;

    private String imageUrl;

    private String price;
}
