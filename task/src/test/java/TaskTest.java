import cn.edu.xmu.wishes.task.TaskApplication;
import cn.edu.xmu.wishes.task.mapper.TaskDraftMapper;
import cn.edu.xmu.wishes.task.model.po.Task;
import cn.edu.xmu.wishes.task.model.po.TaskDraft;
import cn.edu.xmu.wishes.task.service.TaskService;
import cn.edu.xmu.wishes.task.service.imp.TaskServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TaskApplication.class)
public class TaskTest {
    @Autowired
    private TaskDraftMapper taskDraftMapper;

    @Test
    void dbTest() {

        TaskDraft taskDraft = TaskDraft.builder().title("测试").description("test").state((byte) 1).build();
        int insert = taskDraftMapper.insert(taskDraft);
        System.out.println(taskDraft.getId());
        TaskDraft taskDraft1 = new TaskDraft();
        taskDraft1.setId(taskDraft.getId());
        int i = taskDraftMapper.updateById(taskDraft1);
        System.out.println(1);
        System.out.println(1);
    }
}
