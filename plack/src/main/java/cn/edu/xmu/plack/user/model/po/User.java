package cn.edu.xmu.plack.user.model.po;

import cn.edu.xmu.plack.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("user")
public class User extends BaseEntity{
    private String userName;

    private String password;

    private Type state;

    private String email;

    private String mobile;

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
