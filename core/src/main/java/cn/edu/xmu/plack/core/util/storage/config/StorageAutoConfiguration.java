package cn.edu.xmu.plack.core.util.storage.config;

import cn.edu.xmu.plack.core.util.storage.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageAutoConfiguration {

    private final StorageProperties properties;

    public StorageAutoConfiguration(StorageProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "wishes.storage", name = "active", havingValue = "local")
    public Storage localStorage() {
        LocalStorage localStorage = new LocalStorage();
        StorageProperties.Local local = this.properties.getLocal();
        localStorage.setAddress(local.getAddress());
        localStorage.setStoragePath(local.getStoragePath());
        initStorageUtilInfo("local", localStorage);
        return localStorage;
    }

    @Bean
    @ConditionalOnProperty(prefix = "wishes.storage", name = "active", havingValue = "webdav")
    public WebDavStorage webDavStorage() {
        WebDavStorage webDavStorage = new WebDavStorage();
        StorageProperties.Webdav webdav = this.properties.getWebdav();
        webDavStorage.setUrl(webdav.getUrl());
        webDavStorage.setDirectory(webdav.getDirectory());
        webDavStorage.setUsername(webdav.getUsername());
        webDavStorage.setPassword(webdav.getPassword());
        webDavStorage.setReplaceStr(webdav.getReplaceStr());
        initStorageUtilInfo("webdav", webDavStorage);
        return webDavStorage;
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
        initStorageUtilInfo("qiniu", qiniuStorage);
        return qiniuStorage;
    }

    public void initStorageUtilInfo(String active, Storage storage) {
        Objects.requireNonNull(active, "active 不能为null");
        Objects.requireNonNull(storage, "storage 不能为null");
        StorageUtil.setActive(active);
        StorageUtil.setStorage(storage);
    }
}
