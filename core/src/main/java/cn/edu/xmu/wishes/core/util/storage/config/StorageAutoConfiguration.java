package cn.edu.xmu.wishes.core.util.storage.config;

import cn.edu.xmu.wishes.core.util.storage.LocalStorage;
import cn.edu.xmu.wishes.core.util.storage.QiniuStorage;
import cn.edu.xmu.wishes.core.util.storage.Storage;
import cn.edu.xmu.wishes.core.util.storage.StorageUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    private final StorageProperties properties;

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    public StorageUtil storageUtil() {
        StorageUtil storageUtil = new StorageUtil();
        String active = this.properties.getActive();
        storageUtil.setActive(active);
        if (active.equals("local")) {
            storageUtil.setStorage(localStorage());
        }
        else if (active.equals("qiniu")) {
            storageUtil.setStorage(qiniuStorage());
        } else {
            throw new RuntimeException("当前存储模式 " + active + " 不支持");
        }

        return storageUtil;
    }

    @Bean
    @ConditionalOnProperty(prefix = "wishes.storage", name = "active", havingValue = "local")
    public Storage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        StorageProperties.Local local = this.properties.getLocal();
        localStorage.setAddress(local.getAddress());
        localStorage.setStoragePath(local.getStoragePath());
        return localStorage;
    }

    @Bean
    @ConditionalOnProperty(prefix = "wishes.storage", name = "active", havingValue = "qiniu")
    public Storage qiniuStorage() {
        QiniuStorage qiniuStorage = new QiniuStorage();
        StorageProperties.Qiniu qiniu = this.properties.getQiniu();
        qiniuStorage.setAccessKey(qiniu.getAccessKey());
        qiniuStorage.setSecretKey(qiniu.getSecretKey());
        qiniuStorage.setBucketName(qiniu.getBucketName());
        qiniuStorage.setEndpoint(qiniu.getEndpoint());
        return qiniuStorage;
    }
}
