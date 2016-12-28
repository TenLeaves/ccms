package wl.action.fx;

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

@Controller
public class FxTodoController {
    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;
    
  //发行待做
    @RequestMapping(value="showFxtoDoInfo")
    public String getFxInfo(HttpServletRequest req,Model model,EqlPage eqlPage){
        HttpSession session = req.getSession();
         LogInfo loginfo = (LogInfo) session.getAttribute("logUser");
//        LogInfo loginfo = new LogInfo();
//        loginfo.setStaff_id("yeteng");
        List<Map> resultList = workInfoSrv.qryOrderToBind(loginfo, eqlPage);
        model.addAttribute("fxtoDo", resultList);
        return "pages/content/fx/fxtodoFrag";
    }
    
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
