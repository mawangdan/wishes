package cn.edu.xmu.plack.core.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 5315085444565595538L;

    @TableId(type = IdType.AUTO)
    protected Long id;

    @TableField(fill = FieldFill.INSERT)
    protected Long creatorId;

    @TableField(fill = FieldFill.INSERT)
    protected String creatorName;

    @TableField(fill = FieldFill.UPDATE)
    protected Long modifierId;

    @TableField(fill = FieldFill.UPDATE)
    protected String modifierName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime gmtModified;
}
