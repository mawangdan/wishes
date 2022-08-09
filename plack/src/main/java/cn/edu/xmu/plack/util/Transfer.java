package cn.edu.xmu.plack.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.ocr.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.ocr.IECloudClient;
import com.chinamobile.cmss.sdk.ocr.http.constant.Region;
import com.chinamobile.cmss.sdk.ocr.http.signature.Credential;
import com.chinamobile.cmss.sdk.ocr.request.IECloudRequest;
import com.chinamobile.cmss.sdk.ocr.request.ocr.OcrRequestFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
@Slf4j
@Component
public class Transfer implements InitializingBean {
    String user_ak = "38c734312fed4264b0c9192890de5317";
    String user_sk = "570f8bd7423b4e2a92107efa0e32bb9a";
    Credential credential;
    IECloudClient client;
    HashMap<String, Object> bankCardParam = new HashMap<>();
    JSONObject bankCardOptions = new JSONObject();
    public String OCR(String base64){
        try {
            //File file = new File("D:\\dasanXia\\wishes\\task\\src\\main\\java\\cn\\edu\\xmu\\wishes\\util\\2.jpg");
            IECloudRequest bankCardRequest;
            //byte[] fileByte = Files.readAllBytes(file.toPath());
            bankCardRequest = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/webimage", base64, bankCardParam);
            JSONObject response = (JSONObject) client.call(bankCardRequest);
            StringBuilder s=new StringBuilder();
            JSONArray jsonArray = response.getJSONObject("body").getJSONObject("content").getJSONArray("prism_wordsInfo");
            for (int i = 0; i < jsonArray.size(); i++) {
                s.append(((LinkedHashMap) jsonArray.get(i)).get("word"));
            }
            return s.toString();
        } catch (Exception e) {
            log.info(e.getMessage());
            return "";
        }
    }
    @Override
    public void afterPropertiesSet(){
        credential = new Credential(user_ak,   user_sk);
        client = new ECloudDefaultClient(credential, Region.POOL_SZ);
        bankCardOptions.put("ret_warncode_flag", false);
        bankCardOptions.put("ret_border_cut_image", false);
        bankCardOptions.put("enable_copy_check", false);
        bankCardOptions.put("enable_reshoot_check", false);
        bankCardOptions.put("enable_border_check", false);
        bankCardOptions.put("enable_recognize_warn_code", false);
        bankCardOptions.put("enable_quality_value", false);
        bankCardParam.put("options", bankCardOptions);
    }

}
