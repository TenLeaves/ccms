package wl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import wl.common.utils.OrderUtils;
import wl.entity.LogInfo;
import wl.service.FinanceConfirmService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;


@Controller
@SessionAttributes({"selFinancePram"})
public class FinanceConfirmController {
   
    @Resource
    FinanceConfirmService financeConfirmService;
    private Log log = LogFactory.getLog(this.getClass());
    // 查询按钮 查询库存明细
    @RequestMapping(value="/selAllFinance",method=RequestMethod.POST) 
    public String selAllFinance(HttpServletRequest request, Model model,EqlPage eqlPage){
        
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); // 初始化 eqlPage总数为0， 这样每次查询都是查的最新的总数
        
        Map requestMap=request.getParameterMap();
        String pramString = ((String [])requestMap.get("financePram"))[0];
        Map map = (Map)JSON.parse(pramString);
        map.put("eqlPage", eqlPage);
        map.put("SUBSCRIBE_STATE", map.get("orderState"));
        map.put("CUST_TYPE", "3");
        List<Map> subscribeList = OrderUtils.queryOrders(map);
        
        for(Map subscribe : subscribeList){
            if("1".equals(subscribe.get("PAY_FLAG"))){
                subscribe.put("PAY_FLAG_DES", "待付款");
            }else if("2".equals(subscribe.get("SUBSCRIBE_STATE"))){
                subscribe.put("PAY_FLAG_DES", "已付款");  
            }
        }
        
        model.addAttribute("results", subscribeList);
        model.addAttribute("selFinancePram", map);
        
        return "pages/finance/financelist";
    }
    
    // 分页查询库存明细
    @RequestMapping(value="/selFinance",method=RequestMethod.POST) 
    public String selFinance(@ModelAttribute("selFinancePram")Map map, Model model,EqlPage eqlPage){
        
        map.put("eqlPage", eqlPage);
        
        List<Map> subscribeList = OrderUtils.queryOrders(map);
        
        model.addAttribute("results", subscribeList);
        
        return "pages/finance/financelist";
    }
    
    // 订单确认付款，更新状态
    @RequestMapping(value="/financeConfirm",method=RequestMethod.POST) 
    @ResponseBody
    public Map financeConfirm(HttpServletRequest request ){
        
        Map requestMap=request.getParameterMap();
        String pramString = ((String [])requestMap.get("subscribe"))[0];
        Map map = (Map)JSON.parse(pramString);
        
        map.put("FIRST_SEND", "2"); // 是否先发货 全部改成 不发货
        map.put("PAY_FLAG", "2"); // 已付款
        
        String status = (String)map.get("SUBSCRIBE_STATE"); // 订单的原始状态
        if("1".equals(status)){
            
            map.put("TO_SUBSCRIBE_STATE", "2"); // 待付款的状态 变更为 待发货
        }else if("2".equals(status)){
            
            map.put("TO_SUBSCRIBE_STATE", "2"); // 待发货的状态 还是 待发货
        }else if("3".equals(status)){
            
            map.put("TO_SUBSCRIBE_STATE", "3"); // 完成的订单 还是 完成
        }else if("4".equals(status)){
            
            map.put("TO_SUBSCRIBE_STATE", "2"); // 审批不通过 变更为 待发货
        }
        
        LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
        String staffCode = logInfo.getStaff_id();
        map.put("UPDATE_STAFF", staffCode);
        
        log.info("---------------------订单确认收款---------------------");
        log.info("参数:"+map.toString()+"   操作人:"+staffCode);
        
        financeConfirmService.updateFinance(map); 
        
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("status", "success");
        
        log.info("---------------------订单确认收款---------------------");
        return returnMap;
    }
   
    // 订单确认付款，添加备注
    @RequestMapping(value="/addReMarkToOrderWhenConfirm",method=RequestMethod.POST) 
    @ResponseBody
    public Map addReMarkToOrderWhenConfirm(HttpServletRequest request ){
        
        String subscribeId = request.getParameter("SUBSCRIBE_ID");
        String remark = request.getParameter("REMARK");
        
        LogInfo logInfo = (LogInfo)(request.getSession().getAttribute("logUser"));
        String staffCode = logInfo.getStaff_id();
        Map map = Maps.newHashMap();
        map.put("UPDATE_STAFF", staffCode);
        map.put("SUBSCRIBE_ID", subscribeId);
        map.put("REMARK", remark);
        
        financeConfirmService.addRemarkToOrder(map); 
        
        Map<String, String> returnMap = new HashMap<String, String>();
        returnMap.put("status", "success");
        return returnMap;
    }
    
    /**
     * 该方法会在该controler所有的的方法执行器执行这个函数在model中添加了 eqlPage对象
     * @param eqlPage
     * @return
     */
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(10);
        return eqlPage;
    }
    
}
