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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class test {
    public static void main(String[] args) {
        //用户鉴权对象
        String user_ak = "38c734312fed4264b0c9192890de5317";
        String user_sk = "570f8bd7423b4e2a92107efa0e32bb9a";
        Credential credential = new Credential(user_ak,   user_sk);
        IECloudClient client = new ECloudDefaultClient(credential, Region.POOL_SZ);
        HashMap<String, Object> bankCardParam = new HashMap<>();
        JSONObject bankCardOptions = new JSONObject();
        bankCardOptions.put("ret_warncode_flag", false);
        bankCardOptions.put("ret_border_cut_image", false);
        bankCardOptions.put("enable_copy_check", false);
        bankCardOptions.put("enable_reshoot_check", false);
        bankCardOptions.put("enable_border_check", false);
        bankCardOptions.put("enable_recognize_warn_code", false);
        bankCardOptions.put("enable_quality_value", false);
        bankCardParam.put("options", bankCardOptions);
        File file = new File("D:\\dasanXia\\wishes\\task\\src\\main\\java\\cn\\edu\\xmu\\wishes\\util\\1.png");
        IECloudRequest bankCardRequest=null;
        try {
            byte[] fileByte = Files.readAllBytes(file.toPath());
             bankCardRequest = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/webimage", Base64.encodeBase64String(fileByte), bankCardParam);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONObject response = (JSONObject) client.call(bankCardRequest);
            Object body = response.get("body");
            StringBuffer s=new StringBuffer();
            JSONArray jsonArray = response.getJSONObject("body").getJSONObject("content").getJSONArray("prism_wordsInfo");
            for (int i = 0; i < jsonArray.size(); i++) {
                s.append(((LinkedHashMap) jsonArray.get(i)).get("word"));
            }
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
