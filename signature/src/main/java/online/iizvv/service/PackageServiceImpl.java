package online.iizvv.service;

import online.iizvv.dao.PackageDao;
import online.iizvv.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-28 20:10
 * @description：IPA管理
 * @modified By：
 * @version: 1.0
 */
@Service
public class PackageServiceImpl {

    @Autowired
    private PackageDao packageDao;

    public int insertPackage(Package pck) {
        return packageDao.insertPackage(pck);
    }

    public void updateMobileconfig(String mobileconfig, long id) {
        packageDao.updateMobileconfig(mobileconfig, id);
    }

    public int updatePackage(Package pck) {
        return packageDao.updatePackage(pck);
    }

    public Package getPackageById(String id) {
        return packageDao.getPackageById(id);
    }

    public Package getPackageByBundleIdentifier(String bundleIdentifier) {
        return packageDao.getPackageByBundleIdentifier(bundleIdentifier);
    }

    public String getPackageLinkById(String id) {
        return packageDao.getPackageLinkById(id);
    }

    public int updatePackageCountById(long id) {
        return packageDao.updatePackageCountById(id);
    }

    public List<Package>getAllPackage() {
        return packageDao.getAllPackage();
    }

}
