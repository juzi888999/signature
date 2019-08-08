package online.iizvv.core.config;

public class Config {

    /** 账号最大可容纳设备量 */
    public final static int total = 100;

    /** 安装证书时显示的标题 */
    public final static String payloadDisplayName = "查询设备UDID";

    /** 安装证书时显示的描述 */
    public final static String payloadDescription = "仅用作获取设备UDID";

    /** 获取UDID请求地址 */
    public final static String udidURL = "";

    /** 组织名称 */
    public final static String payloadOrganization = "";

    /** access key */
    public final static String accessKeyID = "";

    /** secret key */
    public final static String accessKeySecret = "";

    /** 地域节点 */
    public final static String endpoint = "";

    /** 主内容 Bucket 域名 */
    public final static String aliMainHost = "";

    /** 主内容空间名 */
    public final static String aliMainBucket = "";

    /** 临时内容 Bucket 域名 */
    public final static String aliTempHost = "";

    /** 临时内容空间名 */
    public final static String aliTempBucket = "";


    /** 内网地域节点 */
    public final static String vpcEndpoint = "";

    /** 内网主内容 Bucket 域名 */
    public final static String vpcAliMainHost = "";

    /** 内网临时内容 Bucket 域名 */
    public final static String vpcAliTempHost = "";

    /** 重定向地址 */
    public final static String redirect = "";


}


