package cn.edu.xmu.wishes.core.util.storage;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

@Data
@Slf4j
public class WebDavStorage implements Storage {
    private String url;
    private String directory;
    private String username;
    private String password;
    private String replaceStr;

    @Override
    public String store(InputStream inputStream, long contentLength, String contentType, String keyName) {
        try {
            Sardine sardine = SardineFactory.begin(username, password);

            String baseUrl = url + directory + "/";

            //没有权限创建则抛出IOException
            if (!sardine.exists(baseUrl)) {
                sardine.createDirectory(baseUrl);
            }

            String fileName = UUID.randomUUID() + "_" + keyName;
            String fileUrl = baseUrl + fileName;

            sardine.put(fileUrl, inputStream);

            sardine.shutdown();
            return fileUrl.replace(replaceStr, "");
        }
        catch (Exception e) {
            log.error("webdav Storage method:store" + e.getMessage());
            return null;
        }
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String keyName) {
        return null;
    }

    @Override
    public Resource loadAsResource(String keyName) {
        return null;
    }

    @Override
    public void delete(String filename) {
        Sardine sardine = SardineFactory.begin(username,password);
        try{
            sardine.delete(filename);
            sardine.shutdown();
        }
        catch(IOException e){
            log.error("delete fail: "+ filename);
        }
    }
}
