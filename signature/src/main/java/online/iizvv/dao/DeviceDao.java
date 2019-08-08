package online.iizvv.dao;

import online.iizvv.pojo.Device;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:23
 * @description：Device
 * @modified By：
 * @version: 1.0
 */
@Mapper
public interface DeviceDao {

    /**
     * create by: iizvv
     * description: 获取设备
     * create time: 2019-06-26 21:32
     *

     * @return Device
     */
    @Select("SELECT apple_id, device_id FROM device WHERE udid=#{udid}")
    Device getDeviceByUDID(String udid);

    /**
     * create by: iizvv
     * description: 添加设备到帐号
     * create time: 2019-06-29 13:50
     *

     * @return 是否添加成功
     */
    @Insert("INSERT INTO device (udid, apple_id, device_id) VALUES (#{udid}, #{appleId}, #{deviceId})")
    int insertDevice(String udid, Long appleId, String  deviceId);

}
