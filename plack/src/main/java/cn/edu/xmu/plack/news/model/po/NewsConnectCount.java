package cn.edu.xmu.plack.news.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsConnectCount {
    Long id;
    Long xiHuanCnt;
    Long shouCangCnt;
    Long liuLanCnt;
}
