package cn.edu.xmu.plack.news.model.po;

import cn.edu.xmu.plack.core.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("news")
public class News extends BaseEntity {
    private String newsTitle;
    private String newsType;
    private String author;
    private String content;
    private Long favorCount;
    private Long collectCount;
    private Long browseCount;


    public enum NewsType {
        Business(0, "商业"),
        Entertainment(1,"娱乐"),
        General(2, "推荐"),
        Health(3, "健康"),
        Science(4, "科学"),
        Sports(5, "运动"),
        Technology(6, "技术");

        int code;
        String desc;

        NewsType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
