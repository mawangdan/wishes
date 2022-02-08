package cn.edu.xmu.wishes.core.util;

import cn.edu.xmu.wishes.core.model.VoObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 通用工具类
 * @author Ming Qiu
 **/
public class Common {

    private static Logger logger = LoggerFactory.getLogger(Common.class);

    /**
     * 生成九位数序号
     * 要保证同一服务的不同实例生成出的序号是不同的
     * @param  platform 机器号 如果一个服务有多个实例，机器号需不同，目前从1至36
     * @return 序号
     */
    public static String genSeqNum(int platform) {
        int maxNum = 36;
        int i;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssS");
        LocalDateTime localDateTime = LocalDateTime.now();
        String strDate = localDateTime.format(dtf);
        StringBuffer sb = new StringBuffer(strDate);

        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Random r = new Random();
        while (count < 2) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                sb.append(str[i]);
                count++;
            }
        }
        if (platform > 36){
            platform = 36;
        } else if (platform < 1){
            platform = 1;
        }

        sb.append(str[platform-1]);
        return sb.toString();
    }

    /**
     * 处理BindingResult的错误
     * @param bindingResult
     * @return
     */
    public static Object processFieldErrors(BindingResult bindingResult, HttpServletResponse response) {
        Object retObj = null;
        if (bindingResult.hasErrors()){
            StringBuffer msg = new StringBuffer();
            //解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
            for (FieldError error : bindingResult.getFieldErrors()) {
                msg.append(error.getDefaultMessage());
                msg.append(";");
            }
            logger.debug("processFieldErrors: msg = "+ msg.toString());
            retObj = ResponseUtil.fail(ReturnNo.FIELD_NOTVALID, msg.toString());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
        return retObj;
    }

    /**
     * 处理返回对象
     * @param returnObject 返回的对象
     * @return
     */
    public static ReturnObject getRetObject(ReturnObject<VoObject> returnObject) {
        ReturnNo code = returnObject.getCode();
        switch (code){
            case OK:
            case RESOURCE_FALSIFY:
                VoObject data = returnObject.getData();
                if (data != null){
                    Object voObj = data.createVo();
                    return new ReturnObject(voObj);
                }else{
                    return new ReturnObject();
                }
            default:
                return new ReturnObject(returnObject.getCode(), returnObject.getErrmsg());
        }
    }

    /**
     * @param bo      business object
     * @param voClass vo对象类型
     * @return 浅克隆的vo对象
     * @author xucangbai
     * @date 2021/11/13
     * 根据clazz实例化一个对象，并深度克隆bo中对应属性到这个新对象
     * 其中会自动实现modifiedBy和createdBy两字段的类型转换
     */
    public static <T> T cloneVo(Object bo, Class<T> voClass) {
        Class boClass = bo.getClass();
        T newVo = null;
        try {
            //默认voClass有无参构造函数
            newVo = voClass.getDeclaredConstructor().newInstance();
            Field[] voFields = voClass.getDeclaredFields();
            Field[] boFields = boClass.getDeclaredFields();
            for (Field voField : voFields) {
                //静态和Final不能拷贝
                int mod = voField.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                voField.setAccessible(true);
                Field boField = null;
                try {
                    boField = boClass.getDeclaredField(voField.getName());
                    boField.setAccessible(true);
                }
                //bo中查找不到对应的属性，那就有可能为特殊情况xxx，需要由xxxId与xxxName组装
                catch (NoSuchFieldException e) {
                    //提取头部
                    String head = voField.getName();
                    Field boxxxNameField = null;
                    Field boxxxIdField = null;
                    for (Field bof : boFields) {
                        if (bof.getName().matches(head + "Name")) {
                            boxxxNameField = bof;
                        } else if (bof.getName().matches(head + "Id")) {
                            boxxxIdField = bof;
                        }
                    }
                    //找不到xxxName或者找不到xxxId
                    if (boxxxNameField == null || boxxxIdField == null) {
                        voField.set(newVo, null);
                        continue;
                    }

                    Object newSimpleRetVo = voField.getType().getDeclaredConstructor().newInstance();
                    Field newSimpleRetVoIdField = newSimpleRetVo.getClass().getDeclaredField("id");
                    Field newSimpleRetVoNameField = newSimpleRetVo.getClass().getDeclaredField("name");
                    newSimpleRetVoIdField.setAccessible(true);
                    newSimpleRetVoNameField.setAccessible(true);

                    //bo的xxxId和xxxName组装为SimpleRetVo的id,name
                    boxxxIdField.setAccessible(true);
                    boxxxNameField.setAccessible(true);
                    Object boxxxId = boxxxIdField.get(bo);
                    Object boxxxName = boxxxNameField.get(bo);

                    newSimpleRetVoIdField.set(newSimpleRetVo, boxxxId);
                    newSimpleRetVoNameField.set(newSimpleRetVo, boxxxName);

                    voField.set(newVo, newSimpleRetVo);
                    continue;
                }
                Class<?> boFieldType = boField.getType();
                //属性名相同，类型相同，直接克隆
                if (voField.getType().equals(boFieldType)) {
                    boField.setAccessible(true);
                    Object newObject = boField.get(bo);
                    voField.set(newVo, newObject);
                }
                //属性名相同，类型不同
                else {
                    boolean boFieldIsIntegerOrByteAndVoFieldIsEnum = ("Integer".equals(boFieldType.getSimpleName()) || "Byte".equals(boFieldType.getSimpleName())) && voField.getType().isEnum();
                    boolean voFieldIsIntegerOrByteAndBoFieldIsEnum = ("Integer".equals(voField.getType().getSimpleName()) || "Byte".equals(voField.getType().getSimpleName())) && boFieldType.isEnum();
                    boolean voFieldIsLocalDateTimeAndAndBoFieldIsZonedDateTime = ("LocalDateTime".equals(voField.getType().getSimpleName()) && "ZonedDateTime".equals(boField.getType().getSimpleName()));
                    boolean voFieldIsZonedDateTimeAndBoFieldIsLocalDateTime = ("ZonedDateTime".equals(voField.getType().getSimpleName()) && "LocalDateTime".equals(boField.getType().getSimpleName()));

                    try{
                        //整形或Byte转枚举
                        if (boFieldIsIntegerOrByteAndVoFieldIsEnum) {
                            Object newObj = boField.get(bo);
                            if ("Byte".equals(boFieldType.getSimpleName())) {
                                newObj = ((Byte) newObj).intValue();
                            }
                            Object[] enumer = voField.getType().getEnumConstants();
                            voField.set(newVo, enumer[(int) newObj]);
                        }
                        //枚举转整形或Byte
                        else if (voFieldIsIntegerOrByteAndBoFieldIsEnum) {
                            Object value = ((Enum) boField.get(bo)).ordinal();
                            if ("Byte".equals(voField.getType().getSimpleName())) {
                                value = ((Integer) value).byteValue();
                            }
                            voField.set(newVo, value);
                        }
                        //ZonedDateTime转LocalDateTime
                        else if(voFieldIsLocalDateTimeAndAndBoFieldIsZonedDateTime)
                        {
                            ZonedDateTime newObj = (ZonedDateTime) boField.get(bo);
                            LocalDateTime localDateTime = newObj.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                            voField.set(newVo, localDateTime);
                        }
                        //LocalDateTime转ZonedDateTime
                        else if(voFieldIsZonedDateTimeAndBoFieldIsLocalDateTime)
                        {
                            LocalDateTime newObj = (LocalDateTime) boField.get(bo);
                            ZonedDateTime zdt = newObj.atZone( ZoneId.systemDefault() );
                            voField.set(newVo, zdt);
                        }
                        else {
                            voField.set(newVo, null);
                        }
                    }
                    //如果为空字段则不复制
                    catch (Exception e)
                    {
                        voField.set(newVo, null);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return newVo;
    }

    /**
     * @author xucangbai
     * @param returnObject
     * @param voClass
     * @return
     */
    public static ReturnObject getRetVo(ReturnObject<Object> returnObject,Class voClass) {
        ReturnNo code = returnObject.getCode();
        switch (code){
            case OK:
            case RESOURCE_FALSIFY:
                Object data = returnObject.getData();
                if (data != null){
                    Object voObj = cloneVo(data,voClass);
                    return new ReturnObject(voObj);
                }else{
                    return new ReturnObject();
                }
            default:
                return new ReturnObject(returnObject.getCode(), returnObject.getErrmsg());
        }
    }


    /**
     * 处理返回对象
     * @param returnObject 返回的对象
     * @return
     * TODO： 利用cloneVo方法可以生成任意类型v对象,从而把createVo方法从bo中移除
     */

    public static ReturnObject getListRetObject(ReturnObject<List> returnObject) {
        ReturnNo code = returnObject.getCode();
        switch (code){
            case OK:
            case RESOURCE_FALSIFY:
                List objs = returnObject.getData();
                if (objs != null){
                    List<Object> ret = new ArrayList<>(objs.size());
                    for (Object data : objs) {
                        if (data instanceof VoObject) {
                            ret.add(((VoObject)data).createVo());
                        }
                    }
                    return new ReturnObject(ret);
                }else{
                    return new ReturnObject();
                }
            default:
                return new ReturnObject(returnObject.getCode(), returnObject.getErrmsg());
        }
    }

    /**
     * @author xucangbai
     * @param returnObject
     * @param voClass
     * @return
     */
    public static ReturnObject getListRetVo(ReturnObject<List> returnObject,Class voClass)
    {
        ReturnNo code = returnObject.getCode();
        switch (code){
            case OK:
            case RESOURCE_FALSIFY:
                List objs = returnObject.getData();
                if (objs != null){
                    List<Object> ret = new ArrayList<>(objs.size());
                    for (Object data : objs) {
                        if (data instanceof Object) {
                            ret.add(cloneVo(data,voClass));
                        }
                    }
                    return new ReturnObject(ret);
                }else{
                    return new ReturnObject();
                }
            default:
                return new ReturnObject(returnObject.getCode(), returnObject.getErrmsg());
        }
    }

    /**
     * 处理分页返回对象
     * @param returnObject 返回的对象
     * @return
     * TODO： 利用cloneVo方法可以生成任意类型v对象,从而把createVo方法从bo中移除
     */
    public static ReturnObject getPageRetObject(ReturnObject<PageInfo<VoObject>> returnObject) {
        ReturnNo code = returnObject.getCode();
        switch (code){
            case OK:
            case RESOURCE_FALSIFY:
                PageInfo<VoObject> objs = returnObject.getData();
                if (objs != null){
                    List<Object> voObjs = new ArrayList<>(objs.getList().size());
                    for (Object data : objs.getList()) {
                        if (data instanceof VoObject) {
                            voObjs.add(((VoObject)data).createVo());
                        }
                    }

                    Map<String, Object> ret = new HashMap<>();
                    ret.put("list", voObjs);
                    ret.put("total", objs.getTotal());
                    ret.put("page", objs.getPageNum());
                    ret.put("pageSize", objs.getPageSize());
                    ret.put("pages", objs.getPages());
                    return new ReturnObject(ret);
                }else{
                    return new ReturnObject();
                }
            default:
                return new ReturnObject(returnObject.getCode(), returnObject.getErrmsg());
        }
    }

    /**
     * @author xucangbai
     * @param returnObject
     * @param voClass
     * @return
     */
    public static ReturnObject getPageRetVo(ReturnObject<PageInfo<Object>> returnObject,Class voClass){
        ReturnNo code = returnObject.getCode();
        switch (code){
            case OK:
            case RESOURCE_FALSIFY:
                PageInfo<Object> objs = returnObject.getData();
                if (objs != null){
                    List<Object> voObjs = new ArrayList<>(objs.getList().size());
                    for (Object data : objs.getList()) {
                        if (data instanceof Object) {
                            voObjs.add(cloneVo(data,voClass));
                        }
                    }
                    Map<String, Object> ret = new HashMap<>();
                    ret.put("list", voObjs);
                    ret.put("total", objs.getTotal());
                    ret.put("page", objs.getPageNum());
                    ret.put("pageSize", objs.getPageSize());
                    ret.put("pages", objs.getPages());
                    return new ReturnObject(code,ret);
                }else{
                    return new ReturnObject(code);
                }
            default:
                return new ReturnObject(returnObject.getCode(), returnObject.getErrmsg());
        }
    }



    /**
     * 根据 errCode 修饰 API 返回对象的 HTTP Status
     * @param returnObject 原返回 Object
     * @return 修饰后的返回 Object
     */
    public static Object decorateReturnObject(ReturnObject returnObject) {
        switch (returnObject.getCode()) {
            case RESOURCE_ID_NOTEXIST:
                // 404：资源不存在
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.NOT_FOUND);

            case AUTH_INVALID_JWT:
            case AUTH_JWT_EXPIRED:
                // 401
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.UNAUTHORIZED);

            case INTERNAL_SERVER_ERR:
                // 500：数据库或其他严重错误
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.INTERNAL_SERVER_ERROR);

            case FIELD_NOTVALID:
            case IMG_FORMAT_ERROR:
            case IMG_SIZE_EXCEED:
            case LATE_BEGINTIME:
            case ACT_LATE_PAYTIME:
            case ACT_EARLY_PAYTIME:
            case COUPON_LATE_COUPONTIME:
                // 400
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.BAD_REQUEST);

            case RESOURCE_ID_OUTSCOPE:
            case  FILE_NO_WRITE_PERMISSION:
            case AUTH_NO_RIGHT:
            case AUTH_NEED_LOGIN:
                // 403
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.FORBIDDEN);

            case OK:
                // 200: 无错误
                Object data = returnObject.getData();
                if (data != null){
                    return ResponseUtil.ok(data);
                }else{
                    return ResponseUtil.ok();
                }

            default:
                data = returnObject.getData();
                if (data != null){
                    return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg(), returnObject.getData());
                }else{
                    return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg());
                }

        }
    }

    /**
     * 分桶策略
     * @param groupNum
     * @param whole
     * @return
     * @author yujie
     */
    public static int[] getAvgArray(Integer groupNum, Integer whole) {
        // 将数量尽量平均分到多个桶
        int[] incr = new int[groupNum];
        // 数量小于等于组数，随机把数量加到桶中
        Random r = new Random();
        if(whole<=groupNum){
            for(int i=0;i<whole;i++){
                int init = r.nextInt(groupNum);
                incr[init]++;
            }
            return incr;
        }
        // 数量大于组数，先将余数先加到前面的桶中，再将其余相同的增量加到各自随机的桶中
        int unit=whole/groupNum;
        int other=whole-unit*groupNum;
        for (int i = 0; i < groupNum ; i++) {
            if (i<other) {
                incr[i]+=unit+1;
            } else {
                incr[i] += unit;
            }
        }
        return incr;
    }

}
