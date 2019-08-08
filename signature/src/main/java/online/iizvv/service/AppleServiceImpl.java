package online.iizvv.service;

import online.iizvv.dao.AppleDao;
import online.iizvv.pojo.Apple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-06-26 21:44
 * @description：开发者帐号管理
 * @modified By：
 * @version: 1.0
 */
@Service
public class AppleServiceImpl {

    @Autowired
    private AppleDao appleDao;

    public int insertAppleAccount(Apple apple) {
        return appleDao.insertAppleAccount(apple);
    }

    public int updateAppleAccountWithP12(String p12, long id) {
        return appleDao.updateAppleAccountWithP12(p12, id);
    }

    public int updateAppleAccountDevicesCount(long id) {
        return appleDao.updateAppleAccountDevicesCount(id);
    }

    public Apple getAppleAccountByAccount(String account) {
        return appleDao.getAppleAccountByAccount(account);
    }

    public Apple getAppleAccountById(long id) {
        return appleDao.getAppleAccountById(id);
    }

    public Apple getBeUsableAppleAccount() {
        return appleDao.getBeUsableAppleAccount();
    }

    public List<Apple> getAllAppleAccounts() {
        return appleDao.getAllAppleAccounts();
    }

}
