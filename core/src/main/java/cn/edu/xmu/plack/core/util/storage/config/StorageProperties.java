package cn.edu.xmu.plack.core.util.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wishes.storage")
@Data
public class StorageProperties {
    private String active;
    private Local local;
    private Webdav webdav;
    private Qiniu qiniu;

    @Data
    public static class Local {
        private String address;
        private String storagePath;
    }

    @Data
    public static class Webdav {
        private String url;
        private String directory;
        private String username;
        private String password;
        private String replaceStr;
    }

    @Data
    public static class Qiniu {
        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
    }
}
