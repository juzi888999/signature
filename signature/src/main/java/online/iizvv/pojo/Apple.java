package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "开发者账号对象")
public class Apple {

  @ApiModelProperty(value = "id")
  private long id;

  @ApiModelProperty(value = "账号")
  private String account;

  @ApiModelProperty(value = "可用数量")
  private long count;

  @ApiModelProperty(value = "p8内容")
  private String p8;

  @ApiModelProperty(value = "iss")
  private String iss;

  @ApiModelProperty(value = "kid")
  private String kid;

  @ApiModelProperty(value = "p12文件地址")
  private String p12;

  @ApiModelProperty(value = "cerId")
  private String cerId;

  @ApiModelProperty(value = "开发者后台的通配证书id")
  private String bundleIds;

  @ApiModelProperty(value = "添加时间")
  private long create_time;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }


  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }


  public String getP8() {
    return p8;
  }

  public void setP8(String p8) {
    this.p8 = p8;
  }


  public String getIss() {
    return iss;
  }

  public void setIss(String iss) {
    this.iss = iss;
  }


  public String getKid() {
    return kid;
  }

  public void setKid(String kid) {
    this.kid = kid;
  }

  public String getP12() {
    return  p12;
  }

  public void setP12(String p12) {
    this.p12 = p12;
  }

  public String getCerId() {
    return cerId;
  }

  public void setCerId(String cerId) {
    this.cerId = cerId;
  }

  public String getBundleIds() {
    return bundleIds;
  }

  public void setBundleIds(String bundleIds) {
    this.bundleIds = bundleIds;
  }

  public long getCreate_time() {
    return create_time;
  }

  public void setCreate_time(long create_time) {
    this.create_time = create_time;
  }

  @Override
  public String toString() {
    return "Apple{" +
            "id=" + id +
            ", account='" + account + '\'' +
            ", count=" + count +
            ", p8='" + p8 + '\'' +
            ", iss='" + iss + '\'' +
            ", kid='" + kid + '\'' +
            ", p12='" + p12 + '\'' +
            ", cerId='" + cerId + '\'' +
            ", bundleIds='" + bundleIds + '\'' +
            ", create_time=" + create_time +
            '}';
  }
}
