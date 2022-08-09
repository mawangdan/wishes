package cn.edu.xmu.plack.news.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserNewsSummary {
    Integer favorCount;
    Integer collectCount;
    Integer browseCount;

    public UserNewsSummary() {
        this.favorCount = 0;
        this.collectCount = 0;
        this.browseCount = 0;
    }
}
