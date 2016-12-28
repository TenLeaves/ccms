package wl.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import wl.common.utils.LogUtils;
import wl.common.utils.SecritUtils;
import wl.dao.LoginUserDao;
import wl.entity.LogInfo;

import com.google.code.kaptcha.Constants;
import com.google.common.collect.Maps;

@Service("loginVerifyService")
public class LoginVerifyService {
    @Autowired
    @Qualifier("loginUserDao")
    private LoginUserDao loginUserDao;
    
    private Log log = LogFactory.getLog(this.getClass());
    public String verifyLogin(HttpSession session, LogInfo logUser,Model model) throws Exception {
        boolean isCheckCodeRight =verifyCheckCode(session, logUser, model);
        if(!isCheckCodeRight) return "pages/login";
        boolean isPassRight= verifyPassword(session, logUser, model);
        boolean isAllOk = isCheckCodeRight&&isPassRight;
        
        return isAllOk?"pages/index":"pages/login";
    }

    private boolean verifyPassword(HttpSession session, LogInfo logUser,
            Model model) throws Exception {
        Map qryUser = loginUserDao.qryUser(logUser);
       //Map qryUser = Maps.newHashMap();
        //用户名或者密码错误
        String passUsrInput = logUser.getPassword();
        passUsrInput = SecritUtils.generatePassword(passUsrInput);
        if(qryUser==null||!passUsrInput.equals(qryUser.get("STAFF_PASSWORD"))){
            model.addAttribute("errorInfo", "用户名或者密码错误");
            Object tiem = session.getAttribute("passErrorTimes");
            int times = tiem == null ? 1 : Integer.parseInt(tiem.toString()) + 1;
            session.setAttribute("passErrorTimes", times);
            model.addAttribute("logUser", logUser);
            model.addAttribute("passErrorTimes", times);
            return false;
        }
        //登录成功  状态保持 
        session.removeAttribute("passErrorTimes");
        session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        
        logUser.setUsername(qryUser.get("STAFF_NAME").toString());
        logUser.setRole_id(qryUser.get("ROLE_ID").toString());
       // logUser.setRoleName(getRoleName(logUser));
        String position = "未知";
        if(qryUser.get("POSITION")!=null){
            position=qryUser.get("POSITION").toString();
        }
        logUser.setRoleName(position);
        session.setAttribute("logUser", logUser);
        
        //记录日志：
        String logStr = "操作:员工登录    ; 参数:员工id{0},登录时间:{1}";
        String[]attr ={logUser.getStaff_id(),new Date().toString()};
        log.info(LogUtils.fillStringByArgs(logStr, attr));
        
        return true;
    }

    private  String getRoleName(LogInfo logUser) {
        Map paraMap = Maps.newHashMap();
        paraMap.put("staff_id", logUser.getStaff_id());
        
        List<Map> roleList = loginUserDao.qryRoleInfo(paraMap);
        if(roleList.size()==0){
            return "无";
        }
        
        StringBuffer sbBuffer = new StringBuffer();
        for(Map roleName:roleList){
            sbBuffer.append(roleName.get("ROLE_DESC").toString()+",");
        }
        String roleStr=sbBuffer.toString();
        roleStr=roleStr.equals("")?"":roleStr.substring(0, roleStr.length()-1);
        return roleStr;
    }

    private boolean verifyCheckCode(HttpSession session, LogInfo logUser,
            Model model) {
        String checkCode = session.getAttribute(Constants.KAPTCHA_SESSION_KEY) == null ? null
                : session.getAttribute(Constants.KAPTCHA_SESSION_KEY)
                        .toString();
        if(checkCode!=null){
            if(!checkCode.equals(logUser.getCheckcode())){//验证码不对
                model.addAttribute("errorInfo", "验证码错误");
                Object tiem = session.getAttribute("passErrorTimes");
                int times = tiem == null ? 1 : Integer.parseInt(tiem.toString()) + 1;
                session.setAttribute("passErrorTimes", times);
                model.addAttribute("logUser", logUser);
                model.addAttribute("passErrorTimes", times);
                return false;
            }
        }
        return true;
    }

}
