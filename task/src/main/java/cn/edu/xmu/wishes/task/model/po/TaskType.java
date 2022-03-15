package cn.edu.xmu.wishes.task.model.po;

import cn.edu.xmu.wishes.core.model.BaseEntity;
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
@TableName("wishes_task_type")
public class TaskType extends BaseEntity {
    private String name;

    private Byte state;

    @TableLogic(value = "0", delval = "1")
    private Byte beDeleted;
}
