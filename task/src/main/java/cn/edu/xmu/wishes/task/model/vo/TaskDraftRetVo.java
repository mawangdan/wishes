package cn.edu.xmu.wishes.task.model.vo;

import cn.edu.xmu.wishes.core.util.JacksonUtil;
import com.alibaba.nacos.common.utils.JacksonUtils;
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

    public static void main(String[] args) {
        System.out.println(JacksonUtil.toJson(new TaskDraftRetVo()));
        System.out.println(JacksonUtils.toJson(new TaskDraftRetVo()));
    }
}
