package online.iizvv.dao;

import online.iizvv.pojo.Package;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-28 19:25
 * @description：TODO
 * @modified By：
 * @version: 1.0
 */
public interface PackageDao {

    /**
      * create by: iizvv
      * description: 添加IPA
      * create time: 2019-06-28 20:09
      *
      * @return 是否添加成功
      */
    @Insert("INSERT INTO package (name, icon, version, build_version, mini_version, bundle_identifier, summary, link) " +
            "VALUES (#{name}, #{icon}, #{version}, #{buildVersion}, #{miniVersion}, #{bundleIdentifier}, #{summary}, #{link})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insertPackage(Package pck);

    /**
     * create by: iizvv
     * description: 更新证书信息
     * create time: 2019-07-04 11:36

     * @return void
     */
    @Update("UPDATE `signature`.`package` t SET t.`mobileconfig` = #{mobileconfig} WHERE t.`id` = #{id}")
    void updateMobileconfig(String mobileconfig, long id);

    /**
      * create by: iizvv
      * description: 更新IPA
      * create time: 2019-07-04 14:38

      * @return int
      */
    @Update("UPDATE `signature`.`package` t SET t.`name` = #{name}, t.`icon` = #{icon}, t.`version` = #{version}, " +
            "t.`build_version` = #{buildVersion}, t.`mini_version` = #{miniVersion}, t.`summary` = #{summary}, " +
            "t.`link` = #{link} WHERE t.`id` = #{id}")
    int updatePackage(Package pck);

    /**
      * create by: iizvv
      * description: 更新ipa下载量
      * create time: 2019-07-23 09:50

      * @return int
      */
    @Update("UPDATE package SET count = count+1 WHERE id = #{id}")
    int updatePackageCountById(long id);

    /**
      * create by: iizvv
      * description: 获取指定IPA
      * create time: 2019-07-03 16:47

      * @return Package
      */
    @Select("SELECT * FROM package WHERE id=#{id}")
    Package getPackageById(String id);
    @Select("SELECT * FROM package WHERE bundle_identifier=#{bundleIdentifier}")
    Package getPackageByBundleIdentifier(String bundleIdentifier);

    /**
      * create by: iizvv
      * description: 获取指定IPA下载名称
      * create time: 2019-07-06 09:12

      * @return String
      */
    @Select("SELECT link FROM package WHERE id=#{id}")
    String getPackageLinkById(String id);

    /**
      * create by: iizvv
      * description: 获取全部IPA
      * create time: 2019-07-03 16:27

      * @return List
      */
    @Select("SELECT * FROM package")
    List<Package> getAllPackage();

}
