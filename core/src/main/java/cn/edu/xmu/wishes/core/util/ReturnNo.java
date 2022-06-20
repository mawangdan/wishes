package cn.edu.xmu.wishes.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回的错误码
 * @author Ming Qiu
 */
public enum ReturnNo {
    //不加说明的状态码为200或201（POST）
    OK(0,"成功"),
    /***************************************************
     *    系统级错误
     **************************************************/
    //状态码 500
    INTERNAL_SERVER_ERR(500,"服务器内部错误"),

    //所有需要登录才能访问的API都可能会返回以下错误
    //状态码 401
    AUTH_INVALID_JWT(501,"JWT不合法"),
    AUTH_JWT_EXPIRED(502,"JWT过期"),

    //以下错误码提示可以自行修改
    //--------------------------------------------
    //状态码 400
    FIELD_NOTVALID(503,"字段不合法"),
    RESOURCE_FALSIFY(511, "信息签名不正确"),
    IMG_FORMAT_ERROR(508,"图片格式不正确"),
    IMG_SIZE_EXCEED(509,"图片大小超限"),
    PARAMETER_MISSED(510, "缺少必要参数"),
    FILE_NOT_EXIST(511, "文件不存在"),


    //所有路径带id的API都可能返回此错误
    //状态码 404
    RESOURCE_ID_NOTEXIST(504,"操作的资源id不存在"),

    //状态码 403
    //状态码 403
    AUTH_NEED_LOGIN(704, "需要先登录"),
    AUTH_NO_RIGHT(705, "无权限"),
    RESOURCE_ID_OUTSCOPE(505,"操作的资源id不是自己的对象"),
    FILE_NO_WRITE_PERMISSION(506,"目录文件夹没有写入的权限"),

    //状态码 200
    STATENOTALLOW(507,"当前状态禁止此操作"),

    ORDERITEM_NOTSHARED(606,"订单明细无分享记录"),
    CUSTOMERID_NOTEXIST(608,"登录用户id不存在"),
    CUSTOMER_INVALID_ACCOUNT(609, "用户名不存在或者密码错误"),
    CUSTOMER_FORBIDDEN(610,"用户被禁止登录"),
    CUSTOMER_MOBILEEXIST(611,"电话已被注册"),
    CUSTOMER_EMAILEXIST(612,"邮箱已被注册"),
    CUSTOMER_NAMEEXIST(613,"用户名已被注册"),
    CUSTOMER_PASSWORDWRONG(614,"旧密码错误"),
    CUSTOMER_CAPTCHA_ERROR(615,"验证码错误");


    private int code;
    private String message;
    private static final Map<Integer, ReturnNo> returnNoMap;
    static {
        returnNoMap = new HashMap();
        for (ReturnNo enum1 : values()) {
            returnNoMap.put(enum1.code, enum1);
        }
    }
    ReturnNo(int code, String message){
        this.code = code;
        this.message = message;
    }

    public static ReturnNo getByCode(int code1) {
        ReturnNo[] all=ReturnNo.values();
        for (ReturnNo returnNo :all) {
            if (returnNo.code==code1) {
                return returnNo;
            }
        }
        return null;
    }
    public static ReturnNo getReturnNoByCode(int code){
        return returnNoMap.get(code);
    }
    public int getCode() {
        return code;
    }

    public String getMessage(){
        return message;
    }

}
