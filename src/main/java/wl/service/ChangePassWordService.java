package wl.service;

import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.common.utils.LogUtils;
import wl.common.utils.SecritUtils;
import wl.dao.LoginUserDao;
import wl.entity.LogInfo;
@Service("changePassSrv")
public class ChangePassWordService {
    @Autowired
    @Qualifier("loginUserDao")
    private LoginUserDao loginUserDao;
    private Log logInfo = LogFactory.getLog(this.getClass());
    
    public void changePass(Map inputMap) throws Exception {
        String prePass = inputMap.get("prePassword").toString();
        String newPass = inputMap.get("newPassword").toString();
        String confirmPass = inputMap.get("confirmPassword").toString();
        LogInfo loginUser= (LogInfo) inputMap.get("user");
        
        if(!newPass.equals(confirmPass)){
            throw new Exception("两次输入密码不一致，请重新输入");
        }
        
        Map log =  loginUserDao.qryUser(loginUser);
        if(!SecritUtils.generatePassword(prePass).equals(log.get("STAFF_PASSWORD").toString())){
            throw new Exception("原密码输入错误");
        }
        //记录密码修改
        String handelStr="操作:密码修改; 参数: 原密码:{0},新密码:{1},操作员工:{2},操作时间:{3}";
        String[]attr={prePass,newPass,log.get("STAFF_CODE").toString(),new Date().toString()}; 
        logInfo.info(LogUtils.fillStringByArgs(handelStr, attr));
        
        inputMap.put("staffId", log.get("STAFF_CODE").toString()) ;
        newPass = SecritUtils.generatePassword(newPass);
        inputMap.put("newPassword", newPass);
        loginUserDao.changePassWord(inputMap);
    }
}
