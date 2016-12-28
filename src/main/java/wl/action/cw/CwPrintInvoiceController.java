package wl.action.cw;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import wl.service.InitIndexService;

import com.google.common.collect.Maps;
@Controller
public class CwPrintInvoiceController {
    @Autowired
    @Qualifier("initWorkInfoSrv")
    private InitIndexService workInfoSrv;

    // 财务待收款 财务值需要收取零售的钱
    @RequestMapping(value = "cwPrintInvoice")
    public String getFxInfo(HttpServletRequest req, Model model, EqlPage eqlPage) {
        Map paraMap = Maps.newHashMap();
        List<Map> resultList = workInfoSrv.invoiceToType(eqlPage);
               
        model.addAttribute("cwPrintInvoice", resultList);
        return "pages/content/cw/cwInvoicetoType";
    }
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
