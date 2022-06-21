package cn.edu.xmu.wishes.core.util;

import cn.edu.xmu.wishes.core.model.VoObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


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

    public static StringBuilder concatString(String sep, List<String> fields) {
        StringBuilder ret = new StringBuilder();

        ret.append(fields.get(0));
        for (int i = 1; i < fields.size(); i++) {
            ret.append(sep);
            ret.append(fields.get(i));
        }
        return ret;
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

    public static <T> T cloneVo(Object bo, Class<T> voClass) {
        Class boClass = bo.getClass();
        T newVo = null;
        try {
            //默认voClass有无参构造函数
            newVo = voClass.getDeclaredConstructor().newInstance();
            Field[] voFields = voClass.getDeclaredFields();
            List<Field> boFields = new ArrayList<>();
            while (boClass != Object.class) {
                boFields.addAll(Arrays.asList(boClass.getDeclaredFields()));
                boClass = boClass.getSuperclass();
            }
            Map<String, Field> fieldMap = boFields.stream().collect(Collectors.toMap(Field::getName, x -> x));
            for (Field voField : voFields) {
                //静态和Final不能拷贝
                int mod = voField.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                voField.setAccessible(true);

                Field boField;
                String voFieldName = voField.getName();
                boField = fieldMap.get(voFieldName);
                if (boField != null) {
                    boField.setAccessible(true);
                    Class<?> boFieldType = boField.getType();
                    //属性名相同，类型相同，直接克隆
                    if (voField.getType().equals(boFieldType)) {
                        boField.setAccessible(true);
                        Object newObject = boField.get(bo);
                        voField.set(newVo, newObject);
                    }
                }
                //bo中查找不到对应的属性
                //属性名相同，类型不同 不做处理 默认为null
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return newVo;
    }

    @FunctionalInterface
    public interface Transformer<T> {
        Object transformer(T var);
    }

    public static class Converter<T>  {
        String sourceFieldName;
        String targetFieldName;
        Transformer<T> transformer;

        public Converter(String sourceFieldName, String targetFieldName, Transformer<T> transformer) {
            this.sourceFieldName = sourceFieldName;
            this.targetFieldName = targetFieldName;
            this.transformer = transformer;
        }

        public static <T> Converter<T> of(String sourceFieldName, String targetFieldName, Transformer<T> transformer) {
            return new Converter<>(sourceFieldName,  targetFieldName, transformer);
        }
    }

    public static <T> T cloneVo(Object bo, Class<T> voClass, Collection<Converter> converterCollection) {
        Class<?> boClass = bo.getClass();
        T newVo = null;
        try {
            //默认voClass有无参构造函数
            newVo = voClass.getDeclaredConstructor().newInstance();
            Field[] voFields = voClass.getDeclaredFields();
            List<Field> boFields = new ArrayList<>();
            while (boClass != Object.class) {
                boFields.addAll(Arrays.asList(boClass.getDeclaredFields()));
                boClass = boClass.getSuperclass();
            }
            Map<String, Field> fieldMap = boFields.stream().collect(Collectors.toMap(Field::getName, x -> x));

            for (Field voField : voFields) {
                //静态和Final不能拷贝
                int mod = voField.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                voField.setAccessible(true);

                Field boField;
                String voFieldName = voField.getName();
                boField = fieldMap.get(voFieldName);
                if (boField != null) {
                    boField.setAccessible(true);
                    Class<?> boFieldType = boField.getType();
                    //属性名相同，类型相同，直接克隆
                    if (voField.getType().equals(boFieldType)) {
                        boField.setAccessible(true);
                        Object newObject = boField.get(bo);
                        voField.set(newVo, newObject);
                        continue;
                    }
                }

                //bo中查找不到对应的属性
                //属性名相同，类型不同
                for (Converter converter : converterCollection) {
                    if (voFieldName.equals(converter.targetFieldName)) {
                        try {
                            boField = boClass.getDeclaredField(converter.sourceFieldName);
                            boField.setAccessible(true);
                            boField.setAccessible(true);
                            Object newObject = converter.transformer.transformer(boField.get(bo));
                            voField.set(newVo, newObject);
                            break;
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                            logger.error(e.getMessage());
                        }
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


    public static ReturnObject getPageRetVo(IPage page, Class voClass, Collection<Converter> converterCollection){
        List records = page.getRecords();
        if (records != null){
            List<Object> voObjs = new ArrayList<>(records.size());
            for (Object data : records) {
                voObjs.add(cloneVo(data, voClass, converterCollection));
            }
            Map<String, Object> ret = new HashMap<>();
            ret.put("list", voObjs);
            ret.put("total", page.getTotal());
            ret.put("page", page.getCurrent());
            ret.put("pageSize", page.getSize());
            ret.put("pages", page.getPages());
            return new ReturnObject(ret);
        }else{
            return ReturnObject.INTERNAL_SERVER_ERR_RET;
        }
    }

    public static ReturnObject getPageRetVo(IPage page, Class voClass){
        List records = page.getRecords();
        if (records != null){
            List<Object> voObjs = new ArrayList<>(records.size());
            for (Object data : records) {
                voObjs.add(cloneVo(data, voClass));
            }
            Map<String, Object> ret = new HashMap<>();
            ret.put("list", voObjs);
            ret.put("total", page.getTotal());
            ret.put("page", page.getCurrent());
            ret.put("pageSize", page.getSize());
            ret.put("pages", page.getPages());
            return new ReturnObject(ret);
        }else{
            return ReturnObject.INTERNAL_SERVER_ERR_RET;
        }
    }

    public static ReturnObject<Map<String, Object>> getPageRet(IPage page){
        List records = page.getRecords();
        if (records != null){
            Map<String, Object> ret = new HashMap<>();
            ret.put("list", page.getRecords());
            ret.put("total", page.getTotal());
            ret.put("page", page.getCurrent());
            ret.put("pageSize", page.getSize());
            ret.put("pages", page.getPages());
            return new ReturnObject<>(ret);
        }else{
            return new ReturnObject<>(ReturnNo.INTERNAL_SERVER_ERR);
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
            case FILE_NOT_EXIST:
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

    public static <T, K> Page<T> transformPageVo(Page<K> page, Class<T> voClass) {
        List<K> records = page.getRecords();
        if (records != null){
            List<T> voObjs = new ArrayList<>(records.size());
            for (Object data : records) {
                voObjs.add(cloneVo(data,voClass));
            }
            Page<T> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            voPage.setRecords(voObjs);
            return voPage;
        } else{
            return new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        }
    }

    public static <T> List<T> transformListVo(List list, Class<T> voClass) {
        List<T> voObjs = new ArrayList<>(list.size());
        for (Object data : list) {
            voObjs.add(cloneVo(data,voClass));
        }
        return voObjs;
    }

    public static <T> List<T> transformListVo(List list, Class<T> voClass, Collection<Converter> converterCollection) {
        List<T> voObjs = new ArrayList<>(list.size());
        for (Object data : list) {
            voObjs.add(cloneVo(data,voClass, converterCollection));
        }
        return voObjs;
    }
}
