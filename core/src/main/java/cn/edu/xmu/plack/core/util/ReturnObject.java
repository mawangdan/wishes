package cn.edu.xmu.plack.core.util;

import lombok.Getter;

/**
 * 返回对象
 * @author Ming Qiu
 **/
@Getter
public class ReturnObject<T> {
    public static ReturnObject OK_RET = new ReturnObject(ReturnNo.OK);
    public static ReturnObject INTERNAL_SERVER_ERR_RET = new ReturnObject(ReturnNo.INTERNAL_SERVER_ERR);
    public static ReturnObject AUTH_INVALID_JWT_RET = new ReturnObject(ReturnNo.AUTH_INVALID_JWT);
    public static ReturnObject AUTH_JWT_EXPIRED_RET = new ReturnObject(ReturnNo.AUTH_JWT_EXPIRED);
    public static ReturnObject FIELD_NOTVALID_RET = new ReturnObject(ReturnNo.FIELD_NOTVALID);
    public static ReturnObject RESOURCE_FALSIFY_RET = new ReturnObject(ReturnNo.RESOURCE_FALSIFY);
    public static ReturnObject IMG_FORMAT_ERROR_RET = new ReturnObject(ReturnNo.IMG_FORMAT_ERROR);
    public static ReturnObject IMG_SIZE_EXCEED_RET = new ReturnObject(ReturnNo.IMG_SIZE_EXCEED);
    public static ReturnObject PARAMETER_MISSED_RET = new ReturnObject(ReturnNo.PARAMETER_MISSED);
    public static ReturnObject FILE_NOT_EXIST_RET = new ReturnObject(ReturnNo.FILE_NOT_EXIST);
    public static ReturnObject RESOURCE_ID_NOTEXIST_RET = new ReturnObject(ReturnNo.RESOURCE_ID_NOTEXIST);
    public static ReturnObject AUTH_NEED_LOGIN_RET = new ReturnObject(ReturnNo.AUTH_NEED_LOGIN);
    public static ReturnObject AUTH_NO_RIGHT_RET = new ReturnObject(ReturnNo.AUTH_NO_RIGHT);
    public static ReturnObject RESOURCE_ID_OUTSCOPE_RET = new ReturnObject(ReturnNo.RESOURCE_ID_OUTSCOPE);
    public static ReturnObject FILE_NO_WRITE_PERMISSION_RET = new ReturnObject(ReturnNo.FILE_NO_WRITE_PERMISSION);
    public static ReturnObject STATENOTALLOW_RET = new ReturnObject(ReturnNo.STATENOTALLOW);
    public static ReturnObject ORDERITEM_NOTSHARED_RET = new ReturnObject(ReturnNo.ORDERITEM_NOTSHARED);
    public static ReturnObject CUSTOMERID_NOTEXIST_RET = new ReturnObject(ReturnNo.CUSTOMERID_NOTEXIST);
    public static ReturnObject CUSTOMER_INVALID_ACCOUNT_RET = new ReturnObject(ReturnNo.CUSTOMER_INVALID_ACCOUNT);
    public static ReturnObject CUSTOMER_FORBIDDEN_RET = new ReturnObject(ReturnNo.CUSTOMER_FORBIDDEN);
    public static ReturnObject CUSTOMER_MOBILEEXIST_RET = new ReturnObject(ReturnNo.CUSTOMER_MOBILEEXIST);
    public static ReturnObject CUSTOMER_EMAILEXIST_RET = new ReturnObject(ReturnNo.CUSTOMER_EMAILEXIST);
    public static ReturnObject CUSTOMER_NAMEEXIST_RET = new ReturnObject(ReturnNo.CUSTOMER_NAMEEXIST);
    public static ReturnObject CUSTOMER_PASSWORDWRONG_RET = new ReturnObject(ReturnNo.CUSTOMER_PASSWORDWRONG);
    public static ReturnObject CUSTOMER_CAPTCHA_ERROR_RET = new ReturnObject(ReturnNo.CUSTOMER_CAPTCHA_ERROR);

    /**
     * 错误号
     */
    ReturnNo code = ReturnNo.OK;

    /**
     * 自定义的错误码
     */
    String errmsg = null;

    /**
     * 返回值
     */
    private T data = null;

    /**
     * 默认构造函数，错误码为OK
     */
    public ReturnObject() {
    }

    /**
     * 带值构造函数
     * @param data 返回值
     */
    public ReturnObject(T data) {
        this();
        this.data = data;
    }

    /**
     * 有错误码的构造函数
     * @param code 错误码
     */
    public ReturnObject(ReturnNo code) {
        this.code = code;
    }

    /**
     * 有错误码和自定义message的构造函数
     * @param code 错误码
     * @param errmsg 自定义message
     */
    public ReturnObject(ReturnNo code, String errmsg) {
        this(code);
        this.errmsg = errmsg;
    }

    /**
     * 有错误码，自定义message和值的构造函数
     * @param code 错误码
     * @param data 返回值
     */
    public ReturnObject(ReturnNo code, T data) {
        this(code);
        this.data = data;
    }

    /**
     * 有错误码，自定义message和值的构造函数
     * @param code 错误码
     * @param errmsg 自定义message
     * @param data 返回值
     */
    public ReturnObject(ReturnNo code, String errmsg, T data) {
        this(code, errmsg);
        this.data = data;
    }


    /**
     * 错误信息
     * @return 错误信息
     */
    public String getErrmsg() {
        if (null != this.errmsg) {
            return this.errmsg;
        }else{
            return this.code.getMessage();
        }
    }
}
