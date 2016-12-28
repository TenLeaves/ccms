package wl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wl.service.SureInvoiceService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

@Controller
public class SureInvoiceController {
    @Autowired
    @Qualifier("sureInvoiceSrv")
    private SureInvoiceService sureInvoiceService;
    
    private Log log = LogFactory.getLog(this.getClass());

    @RequestMapping(value = "sureInvoiceInfoPage", method = RequestMethod.POST)
    public String searchOrdersByCondition(HttpServletRequest req,
            EqlPage eqlPage, Model model) {
    	eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); 
        
        Map paraMap = req.getParameterMap();
        Object conditionObj = paraMap.get("condition");
        Object condition = ((String[]) conditionObj)[0];
        Map conditionMap = (Map) JSON.parse(condition.toString());

        List<Map> result=sureInvoiceService.queryOrders(conditionMap, eqlPage);

		model.addAttribute("results", result);
		HttpSession session=req.getSession();
		session.setAttribute("userMap", conditionMap);
        return "pages/sureInvoice/sureInvoiceList";
    }
    
  //分页查询发票信息
    @RequestMapping(value = "/csureInvoiceInfo", method = RequestMethod.POST)
    public String toPageSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=sureInvoiceService.queryOrders(userMap, eqlPage);
		
		model.addAttribute("results", result);
    	return "pages/sureInvoice/sureInvoiceList";
    }

    //发票打印确认
    @RequestMapping(value = "updateSureInvoice",method=RequestMethod.POST)
    public String tosureStock(String invoiceId,String orderId,String invoiceNo,HttpServletRequest request,Model model,EqlPage eqlPage) {
    	Map map=new HashMap();
    	map.put("orderId", orderId);
    	map.put("invoiceId", invoiceId);
    	map.put("invoiceNo", invoiceNo);
    	log.info("--------------------------发票打印确认-----------------------");
    	log.info("参数:"+map);
    	sureInvoiceService.updateInfo(map);
    	log.info("--------------------------发票打印确认结束-----------------------");
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=sureInvoiceService.queryOrders(userMap, eqlPage);
		model.addAttribute("results", result);
    	return "pages/sureInvoice/sureInvoiceList";
    }

    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(5);
        return eqlPage;
    }
}
