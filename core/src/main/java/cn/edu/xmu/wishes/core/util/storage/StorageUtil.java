package cn.edu.xmu.wishes.core.util.storage;


import lombok.Data;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

@Data
public class StorageUtil {
    private String active;
    private Storage storage;

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public String store(InputStream inputStream, long contentLength, String contentType, String key) {
        return storage.store(inputStream, contentLength, contentType, key);
    }

    public String storeImg(InputStream inputStream, String key) {
        return storage.store(inputStream, 0L, null, key);
    }

    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public Path load(String keyName) {
        return storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }
}
