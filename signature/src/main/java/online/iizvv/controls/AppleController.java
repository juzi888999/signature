package online.iizvv.controls;

import cn.hutool.core.lang.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import online.iizvv.pojo.Apple;
import online.iizvv.pojo.Authorize;
import online.iizvv.core.pojo.Result;
import online.iizvv.service.AppleServiceImpl;
import online.iizvv.service.DeviceServiceImpl;
import online.iizvv.utils.FileManager;
import online.iizvv.utils.ITSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:24
 * @description：帐号操作
 * @version: 1.0
 */
@RestController
@Api(tags = {"帐号管理"})
@RequestMapping("/apple")
public class AppleController {

    @Autowired
    private AppleServiceImpl appleService;

    @Autowired
    private DeviceServiceImpl deviceService;

    @Autowired
    private FileManager fileManager;


    @ApiOperation(value = "/getAllAppleAccounts", notes = "获取全部账号")
    @GetMapping("/getAllAppleAccounts")
    public Result<List<Apple>> getAllAppleAccounts() {
        Result result = new Result();
        List<Apple> apples = appleService.getAllAppleAccounts();
        result.setCode(1);
        result.setMsg("数据获取成功");
        result.setData(apples);
        return result;
    }

    @ApiOperation(value = "/insertAppleAccount", notes = "添加苹果开发者账号", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account", value = "开发者账号", required = true),
            @ApiImplicitParam(name = "iss", value = "在Store Connect上可以点击复制 iss ID", required = true),
            @ApiImplicitParam(name = "kid", value = "与p8证书保持一致的密钥id", required = true),
            @ApiImplicitParam(name = "csr", value = "本机导出的csr文件内容", required = true),
            @ApiImplicitParam(name = "p8", value = "p8文件内容", required = true)
    })
    @PostMapping("/insertAppleAccount")
    public Result insertAppleAccount(String account, String iss, String kid, String csr, String p8) {
        Result result = new Result();
            if (appleService.getAppleAccountByAccount(account)==null) {
                // 可以写入数据库
                Map map = ITSUtils.getNumberOfAvailableDevices(new Authorize(p8, iss, kid, csr));
                String cerId = (String)map.get("cerId");
                String bundleIds = (String) map.get("bundleIds");
                int number = (int)map.get("number");
                Apple apple = new Apple();
                apple.setAccount(account);
                apple.setCount(number);
                apple.setP8(p8);
                apple.setIss(iss);
                apple.setKid(kid);
                apple.setCerId(cerId);
                apple.setBundleIds(bundleIds);
                int r = appleService.insertAppleAccount(apple);
                if (r==1) {
                    List<Map> devices = (List)map.get("devices");
                    for (Map<String, String> item : devices) {
                        deviceService.insertDevice(item.get("udid"), apple.getId(), item.get("deviceId"));
                    }
                    result.setCode(1);
                    result.setMsg("开发者账号添加成功");
                }else {
                    result.setMsg("数据添加失败，请检查证书文件是否正确");
                }
            }else {
                // 账号已存在
                result.setMsg("账号已存在， 请勿重复添加");
            }
        return result;
    }

    @ApiOperation(value = "/uploadP12", notes = "上传p12文件", produces = "application/json")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "开发者账号id", required = true),
            @ApiImplicitParam(name = "file", value = "p12文件", required = true)
    })
    @PostMapping("/uploadP12")
    public Result uploadP12(long id, MultipartFile file) throws IOException {
        Result result = new Result();
        String p12 = UUID.randomUUID().toString().replace("-", "") + ".p12";
        if (file.getContentType().equalsIgnoreCase("application/x-pkcs12")) {
            // p12文件
            uploadP12File(file.getBytes(), p12);
            int i = appleService.updateAppleAccountWithP12(p12, id);
            if (i==1) {
                result.setCode(1);
                result.setMsg("信息更新成功");
            }else {
                result.setMsg("信息更新失败");
            }
        }else {
            result.setMsg("文件类型错误，请上传p12文件");
        }
        return result;
    }

    /**
     * create by: iizvv
     * description: 将p12文件上传至阿里
     * create time: 2019-06-28 15:06
     *
     * @return 文件名
     */
    void uploadP12File(byte[] bytes, String fileName) {
        fileManager.uploadFile(bytes, fileName, false);
    }

}
