package cn.edu.xmu.wishes.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.ocr.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.ocr.IECloudClient;
import com.chinamobile.cmss.sdk.ocr.http.constant.Region;
import com.chinamobile.cmss.sdk.ocr.http.signature.Credential;
import com.chinamobile.cmss.sdk.ocr.request.IECloudRequest;
import com.chinamobile.cmss.sdk.ocr.request.ocr.OcrRequestFactory;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class test {
    public static void main(String[] args) {
        //用户鉴权对象
        File file = new File("D:\\dasanXia\\wishes\\task\\src\\main\\java\\cn\\edu\\xmu\\wishes\\util\\2.jpg");
        try {
            byte[] fileByte = Files.readAllBytes(file.toPath());
            System.out.println();

            String path = "D:\\dasanXia\\wishes\\task\\src\\main\\java\\cn\\edu\\xmu\\wishes\\util\\1.txt";
            String word =Base64.encodeBase64String(fileByte);
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path,true)));
            out.write(word);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
