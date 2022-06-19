import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import io.github.yedaxia.apidocs.plugin.markdown.MarkdownDocPlugin;

public class GenerateDoc {
    public static void main(String[] args) {
        DocsConfig config = new DocsConfig();
        String prefix = "D:\\IdeaProjects\\wishes";
        String module = "\\user";
        config.setProjectPath(prefix + module); // 项目根目录
        config.setProjectName("Wishes"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath(prefix + "\\doc" + module); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        config.addPlugin(new MarkdownDocPlugin());
        Docs.buildHtmlDocs(config); // 执行生成文档
    }
}
