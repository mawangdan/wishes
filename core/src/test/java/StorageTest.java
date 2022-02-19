import cn.edu.xmu.wishes.core.CoreApplication;
import cn.edu.xmu.wishes.core.util.storage.StorageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest(classes = CoreApplication.class)
public class StorageTest {
    @Autowired
    private StorageUtil storageUtil;

    @Test
    void storage() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File("C:\\Users\\wl\\Pictures\\下载.jpg"));
        String url = storageUtil.store(inputStream, 0L, null, "test");
        storageUtil.delete(url);
        System.out.println(1);
        System.out.println(1);
    }
}
