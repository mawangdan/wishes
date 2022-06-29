package cn.edu.xmu.wishes.news.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsConnectCount {
    Integer id;
    Integer xiHuanCnt;
    Integer shouCangCnt;
    Integer liuLanCnt;
}
