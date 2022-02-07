package cn.edu.xmu.wishes.task.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("wishes_task")
public class Task {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long initiatorId;

    private Long receiverId;

    private Byte type;

    private String description;

    private String imageUrl;

    private String price;

    private Byte state;

    @TableLogic(value = "0", delval = "1")
    private Byte beDeleted;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}