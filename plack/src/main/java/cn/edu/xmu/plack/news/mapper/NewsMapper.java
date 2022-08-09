package cn.edu.xmu.plack.news.mapper;

import cn.edu.xmu.plack.news.model.po.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper  extends BaseMapper<News> {
}
