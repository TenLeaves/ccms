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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import wl.common.utils.ProDuctUtils;
import wl.entity.LogInfo;
import wl.service.InitIndexService;

@Controller
public class PrintInStockController {
    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;
    
  //库管待做
    @RequestMapping(value="showPrintTobeIn")
    public String getFxInfo(HttpServletRequest req,Model model,EqlPage eqlPage){
        HttpSession session = req.getSession();
         LogInfo loginfo = (LogInfo) session.getAttribute("logUser");
//        LogInfo loginfo = new LogInfo();
//        loginfo.setStaff_id("yeteng");
        Map paraMap = Maps.newHashMap();
        paraMap.put("ACCEPT_STAFF", loginfo.getStaff_id());
        List<Map> resultList = workInfoSrv.qryPrintTobeIn(paraMap, eqlPage);
        resultList = tansProdDesc(resultList);
        model.addAttribute("kgtoDo", resultList);
        return "pages/content/kg/kgtodoFrag";
    }
    
    private List<Map> tansProdDesc(List<Map> resultList) {
        List<Map >returnList = Lists.newArrayList();
        for(Map printMap :resultList){
            String printCode = printMap.get("PRINT_CODE").toString();
            printMap.put("PRODUCT_NAME", ProDuctUtils.prodToDesc(printCode));
            returnList.add(printMap);
        }
        return returnList;
    }


    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
