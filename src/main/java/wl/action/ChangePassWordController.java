package wl.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.entity.LogInfo;
import wl.service.ChangePassWordService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

@Controller
public class ChangePassWordController {
    @Autowired
    @Qualifier("changePassSrv")
    private ChangePassWordService changePassSrv;
    
    @RequestMapping(value="changePass",method=RequestMethod.POST)
    @ResponseBody
     public String changePass(HttpServletRequest req){
        String prepass=  (String) req.getParameter("input1");
        String newpass=  (String) req.getParameter("input2");
        String confirmpass=  (String) req.getParameter("input3");
        
        HttpSession session = req.getSession();
        LogInfo log = (LogInfo) session.getAttribute("logUser");
        Map inputMap = Maps.newHashMap();
        inputMap.put("prePassword", prepass);
        inputMap.put("newPassword", newpass);
        inputMap.put("confirmPassword", confirmpass);
        inputMap.put("user", log);
        try {
            changePassSrv.changePass(inputMap);
        } catch (Exception e) {
            inputMap.clear();
            inputMap.put("tag", "1");
            inputMap.put("msg", e.getMessage());
            
            e.printStackTrace();
            return JSON.toJSONString(inputMap);
        }
        inputMap.clear();
        inputMap.put("tag", "0");
        inputMap.put("msg", newpass);
        
        return JSON.toJSONString(inputMap);
     }
}
