package cn.edu.xmu.wishes.core.util.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "wishes.storage")
@Data
public class StorageProperties {
    private String active;
    private Local local;
    private Qiniu qiniu;

    @Data
    public static class Local {
        private String address;
        private String storagePath;
    }

    @Data
    public static class Qiniu {
        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
    }
}
