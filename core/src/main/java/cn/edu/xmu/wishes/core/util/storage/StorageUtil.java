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
        storage.store(inputStream, contentLength, contentType, key);

        String url = generateUrl(key);
        return url;
    }

//    private String generateKey(String originalFilename) {
//        int index = originalFilename.lastIndexOf('.');
//        String suffix = originalFilename.substring(index);
//
//        String key = null;
//        LitemallStorage storageInfo = null;
//
//        do {
//            key = CharUtil.getRandomString(20) + suffix;
//            storageInfo = litemallStorageService.findByKey(key);
//        }
//        while (storageInfo != null);
//
//        return key;
//    }

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

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }
}
