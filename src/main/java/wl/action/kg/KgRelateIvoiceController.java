package wl.action.kg;

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

import com.google.common.collect.Maps;
@Controller
public class KgRelateIvoiceController {
    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;
    
  //库管待做
    @RequestMapping(value="showTypedInvoice")
    public String showTypedInvoice(HttpServletRequest req,Model model,EqlPage eqlPage){
        HttpSession session = req.getSession();
         LogInfo loginfo = (LogInfo) session.getAttribute("logUser");
//        LogInfo loginfo = new LogInfo();
//        loginfo.setStaff_id("yeteng");
        Map paraMap = Maps.newHashMap();
        paraMap.put("ACCEPT_STAFF", loginfo.getStaff_id());
        List<Map> resultList = workInfoSrv.invoiceHasTyped(paraMap, eqlPage);
        model.addAttribute("kgrelate", resultList);
        return "pages/content/kg/kgrelate";
    }
    
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
