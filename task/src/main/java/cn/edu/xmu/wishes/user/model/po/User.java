package cn.edu.xmu.wishes.user.model.po;

import cn.edu.xmu.wishes.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author bwly
 * @since 2022-02-07
 */
@Data
@Accessors(chain = true)
@TableName("wishes_user")
public class User extends BaseEntity{
    private String userName;

    private String password;

    private Integer creditPoint;

    private String sign;

    private String address;

    private String realName;

    private Type state;

    private String email;

    private String preferTaskType;

    private String mobile;

    private String studentId;

    @TableLogic(value = "0", delval = "1")
    private Integer beDeleted;

    public enum Type {
        NORMAL(0, "正常"),
        BANNED(1, "封禁");

        private static final Map<Integer, Type> TYPE_MAP;

        static {
            TYPE_MAP = new HashMap();
            for (Type enum1 : values()) {
                TYPE_MAP.put(enum1.code, enum1);
            }
        }

        @EnumValue
        private int code;

        private String description;

        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Type getTypeByCode(Integer code) {
            return TYPE_MAP.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

    }
}
