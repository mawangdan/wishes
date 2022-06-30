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
@TableName("news_connect")
public class NewsConnect extends BaseEntity {
    private Long userId;
    private Long newsId;
    private String connectType;
}
