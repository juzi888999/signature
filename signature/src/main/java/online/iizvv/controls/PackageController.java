package online.iizvv.controls;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ZipUtil;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.pojo.Package;
import online.iizvv.core.pojo.Result;
import online.iizvv.service.PackageServiceImpl;
import online.iizvv.core.config.Config;
import online.iizvv.utils.FileManager;
import online.iizvv.utils.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-28 15:08
 * @description：IPA包管理
 * @version: 1.0
 */
@RestController
@RequestMapping("/package")
@Api(tags = {"IPA包管理"})
public class PackageController {

    @Autowired
    private PackageServiceImpl packageService;

    @Autowired
    private FileManager fileManager;

    @ApiOperation(value = "/getAllPackage", notes = "获取全部IPA")
    @GetMapping("/getAllPackage")
    public Result<List<Package>> getAllPackage(HttpServletRequest request) {
        Result result = new Result();
        List<Package> allPackage = packageService.getAllPackage();
        for (Package aPackage : allPackage) {
            aPackage.setIcon(Config.aliMainHost + "/" + aPackage.getIcon());
            aPackage.setMobileconfig(Config.aliMainHost + "/" + aPackage.getMobileconfig());
        }
        result.setMsg("数据获取成功");
        result.setData(allPackage);
        result.setCode(1);
        return result;
    }


    @ApiOperation(value = "/uploadPackage", notes = "上传ipa", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "ipa文件", required = true),
            @ApiImplicitParam(name = "summary", value = "简介")
    })
    @PostMapping("/uploadPackage")
    public Result uploadPackage(MultipartFile file, String summary) {
        Result result = new Result();
        if (file!=null) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.equalsIgnoreCase("ipa")) {
                // 上传的文件为ipa文件
                try {
                    int i = analyze(file, summary);
                    if (i==1) {
                        result.setCode(1);
                        result.setMsg("ipa文件提交成功");
                    }else {
                        result.setMsg("ipa文件解析失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result.setMsg("ipa解析失败");
                }
            }else {
                // 上传的文件非ipa文件
                result.setMsg("请检查文件类型");
            }
        }else {
            result.setMsg("文件不存在， 检查路径是否正确");
        }
        return result;
    }

    @ApiOperation(value = "/getPackageById", notes = "获取指定ipa")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "ipaId", required = true)
    })
    @GetMapping("/getPackageById")
    public Result getPackageById(String id) {
        Result result = new Result();
        Package pck = packageService.getPackageById(id);
        if (pck==null) {
            result.setMsg("内容不存在");
        }else {
            pck.setIcon(Config.aliMainHost + "/" + pck.getIcon());
            pck.setMobileconfig(Config.aliMainHost + "/" + pck.getMobileconfig());
            result.setCode(1);
            result.setMsg("获取成功");
            result.setData(pck);
        }
        return result;
    }

    /**
      * create by: iizvv
      * description: 解析ipa
      * create time: 2019-06-28 15:59

      * @return void
      */
    int analyze(MultipartFile file, String summary) throws ParserConfigurationException, ParseException, SAXException, PropertyListFormatException, IOException {
        File excelFile = File.createTempFile(UUID.randomUUID().toString(), ".ipa");
        file.transferTo(excelFile);
        File ipa = ZipUtil.unzip(excelFile);
        File app = getAppFile(ipa);
        File info = new File(app.getAbsolutePath()+"/Info.plist");
        NSDictionary parse = (NSDictionary) PropertyListParser.parse(new FileReader(info).readBytes());
        String name = parse.get("CFBundleName").toString();
        if (parse.containsKey("CFBundleDisplayName")) {
            name = parse.get("CFBundleDisplayName").toString();
        }
        String version = parse.get("CFBundleShortVersionString").toString();
        String buildVersion = parse.get("CFBundleVersion").toString();
        String miniVersion = parse.get("MinimumOSVersion").toString();
        String id = parse.get("CFBundleIdentifier").toString();
        NSDictionary icons = null;
        if (parse.containsKey("CFBundleIcons")) {
            icons = (NSDictionary) parse.get("CFBundleIcons");
        }else if (parse.containsKey("CFBundleIcons~ipad")) {
            icons = (NSDictionary) parse.get("CFBundleIcons~ipad");
        }
        String iconLink = null;
        if (icons.toJavaObject()!=null) {
            List list = ((NSDictionary) icons.get("CFBundlePrimaryIcon")).get("CFBundleIconFiles").toJavaObject(List.class);
            iconLink = (String) list.get(list.size()-1);
            String iconPath = app.getAbsolutePath() + "/" + iconLink;
            File icon = new File( iconPath + "@3x.png");
            if (icon==null) {
                icon = new File(iconPath+"@2x.png");
            }
            iconLink = uploadIcon(icon);
            icon.delete();
        }
        String appLink = uploadAppFile(excelFile);
        if (appLink!=null) {
            System.out.println("ipa文件上传完成");
        }
        Package pck = packageService.getPackageByBundleIdentifier(id);
        if (pck!=null) {
            pck.setName(name);
            pck.setIcon(iconLink);
            pck.setVersion(version);
            pck.setBuildVersion(buildVersion);
            pck.setMiniVersion(miniVersion);
            pck.setLink(appLink);
            if (summary!=null) {
                pck.setSummary(summary);
            }
            int i = packageService.updatePackage(pck);
            return i;
        }
        pck = new Package();
        pck.setName(name);
        pck.setIcon(iconLink);
        pck.setVersion(version);
        pck.setBuildVersion(buildVersion);
        pck.setBundleIdentifier(id);
        pck.setMiniVersion(miniVersion);
        pck.setLink(appLink);
        pck.setSummary(summary);
        int i = packageService.insertPackage(pck);
        String mobileconfig = creatUDIDMobileconfig(pck.getId());
        if (mobileconfig!=null) {
            packageService.updateMobileconfig(mobileconfig, pck.getId());
        }else {
            i = 0;
        }
        FileUtil.del(excelFile);
        FileUtil.del(ipa);
        FileUtil.del(app);
        FileUtil.del(info);
        return i;
    }

    /**
      * create by: iizvv
      * description: 上传app
      * create time: 2019-06-28 17:21

      * @return app
      */
    String uploadAppFile(File file) {
        System.out.println("开始上传原始ipa文件");
        String objName = UUID.randomUUID().toString().replace("-", "") + ".ipa";
        fileManager.uploadFile(file, objName, false);
        return objName;
    }

    /**
     * create by: iizvv
     * description: 上传icon
     * create time: 2019-06-29 12:19

     * @return icon名称
     */
    String uploadIcon(File file) {
        String objName = UUID.randomUUID().toString().replace("-", "")+".png";
        fileManager.uploadFile(file, objName, false);
        return objName;
    }


    /**
      * create by: iizvv
      * description: 创建获取UDID所用证书
      * create time: 2019-07-04 11:01

      * @return 证书名称
      */
    String creatUDIDMobileconfig(long id) {
        System.out.println("创建获取UDID所用证书");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" +
                "<plist version=\"1.0\">\n" +
                "    <dict>\n" +
                "        <key>PayloadContent</key>\n" +
                "        <dict>\n" +
                "            <key>URL</key>\n" +
                "            <string>"+ Config.udidURL +"/udid/getUDID?id="+ id +"</string> <!--接收数据的接口地址-->\n" +
                "            <key>DeviceAttributes</key>\n" +
                "            <array>\n" +
                "                <string>SERIAL</string>\n" +
                "                <string>MAC_ADDRESS_EN0</string>\n" +
                "                <string>UDID</string>\n" +
                "                <string>IMEI</string>\n" +
                "                <string>ICCID</string>\n" +
                "                <string>VERSION</string>\n" +
                "                <string>PRODUCT</string>\n" +
                "            </array>\n" +
                "        </dict>\n" +
                "        <key>PayloadOrganization</key>\n" +
                "        <string>" + Config.payloadOrganization +"</string>  <!--组织名称-->\n" +
                "        <key>PayloadDisplayName</key>\n" +
                "        <string>" + Config.payloadDisplayName + "</string>  <!--安装时显示的标题-->\n" +
                "        <key>PayloadVersion</key>\n" +
                "        <integer>1</integer>\n" +
                "        <key>PayloadUUID</key>\n" +
                "        <string>"+ UUID.randomUUID().toString().replace("-", "") +"</string>  <!--自己随机填写的唯一字符串-->\n" +
                "        <key>PayloadIdentifier</key>\n" +
                "        <string>online.iizvv.profile-service</string>\n" +
                "        <key>PayloadDescription</key>\n" +
                "        <string>"+Config.payloadDescription+"</string>   <!--描述-->\n" +
                "        <key>PayloadType</key>\n" +
                "        <string>Profile Service</string>\n" +
                "    </dict>\n" +
                "</plist>";
        String tempName = "udid_"+ id + "_" + UUID.randomUUID().toString().replace("-", "");
        String tempMobileconfig = tempName + ".mobileconfig";
        FileWriter writer = new FileWriter(tempMobileconfig);
        writer.write(xml);
        System.out.println("开始执行shell");
        String mobileconfig = tempName + "_.mobileconfig";
        String com = "/root/mobileconfig.sh " + writer.getFile().getAbsolutePath() + " " + mobileconfig;
        try {
            Shell.run(com);
            System.out.println("shell执行成功, 文件位置为: " + mobileconfig);
            File file = new File("/root/" + mobileconfig);
            mobileconfig = uploadMobileconfig(file);
            file.delete();
        } catch (Exception e) {
            System.out.println("shell执行失败");
            mobileconfig = uploadMobileconfig(writer.getFile());
            e.printStackTrace();
        }finally {
            writer.getFile().delete();
        }
        System.out.println("mobileconfig文件上传结束");
        return mobileconfig;
    }


    /**
      * create by: iizvv
      * description: 上传mobileconfig
      * create time: 2019-07-04 11:18

      * @return mobileconfig名称
      */
    String uploadMobileconfig(File file) {
        String objName = UUID.randomUUID().toString().replace("-", "")+".mobileconfig";
        fileManager.uploadFile(file, objName, false);
        return objName;
    }


    /**
      * create by: iizvv
      * description: 获取ipa中的app文件
      * create time: 2019-07-24 10:57

      * @return app文件
      */
    File getAppFile(File ipaFile) {
        File payload = new File(ipaFile.getAbsolutePath() + "/Payload/");
        if (payload != null) {
            for (File file : payload.listFiles()) {
                System.out.println(fileManager.getSuffixName(file));
                if (fileManager.getSuffixName(file).equalsIgnoreCase("app")) {
                    System.out.println(file.getName());
                    return file;
                }
            }
        }
        return null;
    }


}
