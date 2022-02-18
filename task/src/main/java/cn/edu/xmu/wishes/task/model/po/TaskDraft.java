package cn.edu.xmu.wishes.task.model.po;

import cn.edu.xmu.wishes.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TableName("wishes_task_draft")
public class TaskDraft extends BaseEntity {
    private Long taskId;

    private Long initiatorId;

    private Long receiverId;

    private Byte type;

    private String title;

    private String description;

    private String imageUrl;

    private String price;

    private Byte state;
}
