package wl.action.cw;

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

import wl.service.InitIndexService;

@Controller
public class CwToGetPayController {
    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;
    
    //财务待收款  财务值需要收取零售的钱
    @RequestMapping(value="cwToGetPay")
    public String getFxInfo(HttpServletRequest req,Model model,EqlPage eqlPage){
        Map paraMap = Maps.newHashMap();
        paraMap.put("CUSTOMER_TYPE", "3");
        List<Map> resultList = workInfoSrv.qrySaleOrderTobePay(paraMap, eqlPage);
        model.addAttribute("cwGetPay", resultList);
        return "pages/content/cw/getPayFrag";
    }
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
