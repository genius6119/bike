package com.zwx.bike.common.constants;

/**
 * Create By Zhang on 2018/3/4
 */
public class Constants {

    /**自定义状态码 start**/
    public static final int RESP_STATUS_OK = 200;

    public static final int RESP_STATUS_NOAUTH = 401;

    public static final int RESP_STATUS_INTERNAL_ERROR = 500;

    public static final int RESP_STATUS_BADREQUEST = 400;

    public static final  String REQUEST_TOKEN_KEY="user-token";

    public static final String REQUEST_VERSION_KEY = "version";  /**为以后控制app版本做准备*/

    /**秒滴SMS短信发送服务*/
    public static final String MDSMS_ACCOUNT_SID="43de2b50f4a744d9adc2977b5308135a";

    public static final String MDSMS_AUTH_TOKEN="107b272828b343f2a1e30f8458f3523c";

    public static final String MDSMS_REST_URL="https://api.miaodiyun.com/20150822";

    public static final String MDSMS_VERCODE_TPLID="199714035";

    /**七牛云存储*/

    public static final String QINIU_ACCESS_KEY="hjTKoPX-NBZ44uH7a4oBi78KWRM_1hdj-mNTSRUy";

    public static final String QINIU_SECRET_KEY ="lOZp5SmT3rAPMMAMIf72w6BSeFKMNI25crB4RXKT";

    public static final String QINIU_HEAD_IMG_BUCKET_NAME="mybike";

    public static final String QINIU_HEAD_IMG_BUCKET_URL="p68xk1nzm.bkt.clouddn.com";

    /**百度云推送*/

    public static final String BAIDU_YUN_PUSH_API_KEY="CyWG81nLG3eoHHsMbcHoMMpZ";

    public static final String BAIDU_YUN_PUSH_SECRET_KEY="BVT1CewKWsGgI8wWr3oG0G2G7x9epTG";

    public static final String CHANNEL_REST_URL = "api.push.baidu.com";


}
