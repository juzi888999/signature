package online.iizvv.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "安装包对象")
public class Package {

  @ApiModelProperty(value = "id")
  private long id;

  @ApiModelProperty(value = "包名")
  private String name;

  @ApiModelProperty(value = "图标")
  private String icon;

  @ApiModelProperty(value = "版本")
  private String version;

  @ApiModelProperty(value = "编译版本号")
  private String buildVersion;

  @ApiModelProperty(value = "最小支持版本")
  private String miniVersion;

  @ApiModelProperty(value = "安装包id")
  private String bundleIdentifier;

  @ApiModelProperty(value = "简介")
  private String summary;

  @ApiModelProperty(value = "获取UDID证书地址")
  private String mobileconfig;

  @ApiModelProperty(value = "下载地址")
  private String link;

  @ApiModelProperty(value = "总下载量")
  private long count;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }


  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  public String getBuildVersion() {
    return buildVersion;
  }

  public void setBuildVersion(String buildVersion) {
    this.buildVersion = buildVersion;
  }


  public String getMiniVersion() {
    return miniVersion;
  }

  public void setMiniVersion(String miniVersion) {
    this.miniVersion = miniVersion;
  }


  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getBundleIdentifier() {
    return bundleIdentifier;
  }

  public void setBundleIdentifier(String bundleIdentifier) {
    this.bundleIdentifier = bundleIdentifier;
  }


  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getMobileconfig() {
    return mobileconfig;
  }

  public void setMobileconfig(String mobileconfig) {
    this.mobileconfig = mobileconfig;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "Package{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", icon='" + icon + '\'' +
            ", version='" + version + '\'' +
            ", buildVersion='" + buildVersion + '\'' +
            ", miniVersion='" + miniVersion + '\'' +
            ", bundleIdentifier='" + bundleIdentifier + '\'' +
            ", summary='" + summary + '\'' +
            ", mobileconfig='" + mobileconfig + '\'' +
            ", link='" + link + '\'' +
            ", count=" + count +
            '}';
  }
}
