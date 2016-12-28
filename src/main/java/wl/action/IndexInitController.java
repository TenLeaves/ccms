package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import wl.entity.LogInfo;
import wl.service.InitIndexService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mchange.lang.StringUtils;

@Controller
public class IndexInitController {
    

    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;

    @RequestMapping(value = "/initIndexAfterLogIn")
    public String initIndex(HttpServletRequest req, Model model, EqlPage eqlPage) {
        HttpSession session = req.getSession();
         LogInfo loginfo = (LogInfo) session.getAttribute("logUser");
        // 获取用户角色
        List<Map> roleList = workInfoSrv.qryRoleList(loginfo);
        String roleString = "";
        for (int i = 0; i < roleList.size(); i++) {
            Map roleMap = roleList.get(i);
            String roleValue = roleMap.get("ROLE_VALUE").toString();
            roleString +=roleValue;
        }
        if(roleString.contains("5")){
            roleString = roleString.replace("5", "");
        }
        if(roleString.length()==3){
            roleString.replace("3", "");
        }
        if("1".equals(roleString)){
            return "pages/content/fx/fxhome";
        }else if("2".equals(roleString)){
            return "pages/content/kg/kghome";
        }else if("3".equals(roleString)){
            return "pages/content/cw/cwhome";
        }else if("4".equals(roleString)){
            return "pages/content/ld/ldhome";
        }else if(roleString.contains("1")&&roleString.contains("2")){
            return "pages/content/fxkg/fxkghome";
        }
        model.addAttribute("indexFlag", "indexFlag");
        return "pages/leaderForm/leader_forms";
    }
 
}
