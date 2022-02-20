package cn.edu.xmu.wishes.user.model.po;

import cn.edu.xmu.wishes.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@TableName("wishes_auth_privilege")
public class AuthPrivilege extends BaseEntity {
    private String name;
    private String url;
    private RequestType requestType;
    private Type state;

    public enum RequestType {
        GET(0, "GET"),
        POST(1, "POST"),
        PUT(2, "PUT"),
        DELETE(3, "DELETE");

        private static final Map<Integer, RequestType> TYPE_MAP;

        static {
            TYPE_MAP = new HashMap();
            for (RequestType enum1 : values()) {
                TYPE_MAP.put(enum1.code, enum1);
            }
        }

        @EnumValue
        private int code;

        private String description;

        RequestType(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static RequestType getTypeByCode(Integer code) {
            return TYPE_MAP.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

    }

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
