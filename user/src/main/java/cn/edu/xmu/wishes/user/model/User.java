package cn.edu.xmu.wishes.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author bwly
 * @since 2022-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wishes_user")
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String userName;

    private String password;

    private Integer creditPoint;

    private String sign;

    private String address;

    private String realName;

    private Integer state;

    private String email;

    private String mobile;

    private String studentId;

    private Integer beDeleted;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private Date gmtCreate;

    private Date gmtModified;


}
