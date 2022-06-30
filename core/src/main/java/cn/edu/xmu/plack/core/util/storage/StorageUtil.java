package cn.edu.xmu.plack.core.util.storage;


import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

public class StorageUtil {
    private static String active;
    private static Storage storage;

    public static String getActive() {
        return active;
    }

    public static void setActive(String active) {
        StorageUtil.active = active;
    }

    public static Storage getStorage() {
        return storage;
    }

    public static void setStorage(Storage storage) {
        StorageUtil.storage = storage;
    }

    /**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param fileName      文件索引名
     */
    public static String store(InputStream inputStream, String fileName) {
        return storage.store(inputStream, 0L, "file", fileName);
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
