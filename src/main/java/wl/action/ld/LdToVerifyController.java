package wl.action.ld;

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

import com.google.common.collect.Maps;

import wl.entity.LogInfo;
import wl.service.InitIndexService;

@Controller
public class LdToVerifyController {
    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;
    
    //领导待审批
    @RequestMapping(value="ldtoDoInfo")
    public String getFxInfo(HttpServletRequest req,Model model,EqlPage eqlPage){
        Map paraMap = Maps.newHashMap();
        List<Map> resultList = workInfoSrv.qryOrderToBeVerify(paraMap, eqlPage);
        model.addAttribute("ldtoDo", resultList);
        return "pages/content/ld/ldtodoFrag";
    }
    
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
