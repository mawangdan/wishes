package cn.edu.xmu.wishes.task.model.po;

import cn.edu.xmu.wishes.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TableName("wishes_task")
public class Task extends BaseEntity {
    private Long initiatorId;

    private Long receiverId;

    private Long typeId;

    private String title;

    private String description;

    private String location;

    private String imageUrl;

    private String price;

    private StateType state;

    @TableLogic(value = "0", delval = "1")
    private Byte beDeleted;

    public enum StateType {
        NOT_ACCEPTED(0, "未接取"),
        ACCEPTED(1, "已接取"),
        FINISHED(2, "已完成"),
        OFFLINE(3, "下线"),
        BANNED(4, "封禁");

        private static final Map<Integer, StateType> TYPE_MAP;

        static {
            TYPE_MAP = new HashMap();
            for (StateType enum1 : values()) {
                TYPE_MAP.put(enum1.code, enum1);
            }
        }

        @EnumValue
        private int code;

        private String description;

        StateType(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static StateType getTypeByCode(Integer code) {
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