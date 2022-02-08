package cn.edu.xmu.wishes.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 实体基类
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 5315085444565595538L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @TableField(fill = FieldFill.UPDATE)
    private Long modifierId;

    @TableField(fill = FieldFill.UPDATE)
    private String modifierName;

    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime gmtModified;
}
