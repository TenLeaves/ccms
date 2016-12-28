package wl.action;

import java.net.SocketException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.n3r.eql.ex.EqlConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import wl.entity.LogInfo;
import wl.service.LoginVerifyService;
import wl.service.MenuInitService;

@Controller
public class IndexController {
    @Autowired
    @Qualifier("menuInitService")
    private MenuInitService menuInitSrv;

    @Autowired
    @Qualifier("loginVerifyService")
    private LoginVerifyService loginVerifySrv;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String toIndex(Model model,HttpServletRequest request) {
        model.addAttribute("logUser", new LogInfo());
        
        LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
        if(logInfo == null){
            return "pages/login";
        }
            return "pages/index";
        
        
    }
    
    @RequestMapping(value = "/deleteSession", method = RequestMethod.GET)
    @ResponseBody
    public void toDeleteSession(HttpServletRequest request) {
        HttpSession session=request.getSession();
        session.removeAttribute("logUser");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String toIndex(LogInfo logUser, HttpServletRequest req, Model model) {

        HttpSession session = req.getSession();

        String returnStr=null;
        try {
            returnStr = loginVerifySrv.verifyLogin(session, logUser, model);
        } catch (Exception e) {
            if(e instanceof EqlConfigException){
                throw new RuntimeException("数据库连接超时");
            }
           model.addAttribute("logUser", new LogInfo());
           return "pages/login"; 
        }

        return returnStr;
    }
    
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String toMenu(HttpServletRequest req, Model model) {
        HttpSession session = req.getSession();
         LogInfo logUsr= (LogInfo) session.getAttribute("logUser");
        Map menuListMap ;
        try {
            menuListMap = menuInitSrv.initMenu(logUsr);
            model.addAttribute("menuList1", menuListMap.get("menuList1"));
            model.addAttribute("menuList2", menuListMap.get("menuList2"));
            model.addAttribute("menuList3", menuListMap.get("menuList3"));
            model.addAttribute("menuList4", menuListMap.get("menuList4"));
            model.addAttribute("menuList5", menuListMap.get("menuList5"));
            return "pages/menu";
        } catch (Exception e) {
            if(e instanceof SocketException){
                throw new RuntimeException("数据库连接异常");
            }
        }
        return null;
    }

   @RequestMapping(value="/top",method=RequestMethod.GET) 
   public String toTop(HttpServletRequest request,Model model){
	   HttpSession session=request.getSession();
	   LogInfo logInfo=(LogInfo) session.getAttribute("logUser");
	   model.addAttribute("username", logInfo.getUsername());
	   model.addAttribute("roleName",logInfo.getRoleName());
       return "pages/top";
   }
    @RequestMapping(value = "/mainf", method = RequestMethod.GET)
    public String toMain() {
        return "pages/mainf";
    }
}
