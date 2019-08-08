package online.iizvv.controls;

import io.swagger.annotations.*;
import online.iizvv.pojo.Apple;
import online.iizvv.pojo.Authorize;
import online.iizvv.pojo.Device;
import online.iizvv.core.pojo.Result;
import online.iizvv.service.AppleServiceImpl;
import online.iizvv.service.DeviceServiceImpl;
import online.iizvv.utils.ITSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"设备管理"})
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private AppleServiceImpl appleService;

    @Autowired
    private DeviceServiceImpl deviceService;

    @ApiOperation(value="/insertDevice", notes="添加一台设备至随机账号", produces = "application/json")
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "udid", value = "设备UDID", required = true)
            }
    )
    @PostMapping("/insertDevice")
    public Result insertDevice(String udid) {
        Result result = new Result();
        Device device = deviceService.getDeviceByUDID(udid);
        if (device==null) {
            Apple apple = appleService.getBeUsableAppleAccount();
            if (apple==null) {
                // 无可用账号
                result.setMsg("未发现可用账号");
            }else {
                // 发现可用账号
                String devId = ITSUtils.insertDevice(udid, new Authorize(apple.getP8(), apple.getIss(), apple.getKid()));
                int i = deviceService.insertDevice(udid, apple.getId(), devId);
                if (i==1) {
                    appleService.updateAppleAccountDevicesCount(apple.getId());
                    result.setCode(1);
                    result.setMsg("设备已添加至: " + apple.getAccount());
                }else {
                    result.setMsg("设备添加失败");
                }
            }
        }else {
            result.setMsg("此设备已存在, 请勿重复添加");
        }
        return result;
    }


}
