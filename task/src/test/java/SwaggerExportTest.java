//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@SpringBootTest
//public class SwaggerExportTest {
//    private final String swaggerUrl = "http://localhost:8080/v2/api-docs";
//
//    /**
//     * 生成AsciiDocs格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocs() throws Exception {
//        //    输出Ascii格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(swaggerUrl))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("src/docs/generated/ascii"));
//    }
//
//    /**
//     * 生成Markdown格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocs() throws Exception {
//        //    输出Markdown格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(swaggerUrl))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("src/docs/generated/markdown"));
//
//
//        /**
//         * 生成AsciiDocs格式文档,并汇总成一个文件
//         * @throws Exception
//         */
//        @Test
//        public void generateAsciiDocsToFile() throws Exception {
//            //    输出Ascii到单文件
//            Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                    .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                    .withOutputLanguage(Language.ZH)
//                    .withPathsGroupedBy(GroupBy.TAGS)
//                    .withGeneratedExamples()
//                    .withoutInlineSchema()
//                    .build();
//
//            Swagger2MarkupConverter.from(new URL(swaggerUrl))
//                    .withConfig(config)
//                    .build()
//                    .toFile(Paths.get("src/docs/generated/ascii/all")); //all表示的是文件名
//        }
//
//        /**
//         * 生成Markdown格式文档,并汇总成一个文件
//         * @throws Exception
//         */
//        @Test
//        public void generateMarkdownDocsToFile() throws Exception {
//            //    输出Markdown到单文件
//            Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                    .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                    .withOutputLanguage(Language.ZH)
//                    .withPathsGroupedBy(GroupBy.TAGS)
//                    .withGeneratedExamples()
//                    .withoutInlineSchema()
//                    .build();
//
//            Swagger2MarkupConverter.from(new URL(swaggerUrl))
//                    .withConfig(config)
//                    .build()
//                    .toFile(Paths.get("src/docs/generated/markdown/all")); //all表示的是文件名
//        }
//}
