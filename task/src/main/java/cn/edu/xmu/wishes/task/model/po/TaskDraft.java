package cn.edu.xmu.wishes.task.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("wishes_task_draft")
public class TaskDraft {
    private Long taskId;

    private Long initiatorId;

    private Long receiverId;

    private Byte type;

    private String description;

    private String imageUrl;

    private String price;

    private Byte state;
}
