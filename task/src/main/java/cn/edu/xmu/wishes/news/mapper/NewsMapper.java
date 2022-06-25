package cn.edu.xmu.wishes.news.mapper;

import cn.edu.xmu.wishes.news.model.po.News;
import cn.edu.xmu.wishes.task.model.po.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper  extends BaseMapper<News> {
}
