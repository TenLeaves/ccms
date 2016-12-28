package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.ResponseBody;

import wl.entity.LogInfo;
import wl.service.OrderEditService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

@Controller
public class OrderEditController {
    @Autowired
    @Qualifier("orderEditService")
    private OrderEditService orderEditService;
    
    private Log log = LogFactory.getLog(this.getClass());
    
    @RequestMapping(value = "/selOrderEditInfo", method = RequestMethod.POST)
    public String toSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); 
        
    	Map map=request.getParameterMap();
		String selInfoList=((String[]) map.get("selInfo"))[0];
		Map userMap=(Map) JSON.parse(selInfoList);
		HttpSession session=request.getSession();
		List<Map> result=orderEditService.appCheck(userMap, eqlPage,session);
		
		model.addAttribute("results", result);
		
		session.setAttribute("userMap", userMap);
    	return "pages/orderEdit/orderEditList";
    }
    
  //分页查询订单信息
    @RequestMapping(value = "/custEditPageInfo", method = RequestMethod.POST)
    public String toPageSubmit(HttpServletRequest request, EqlPage eqlPage,Model model) {
    	HttpSession session=request.getSession();
		Map userMap=(Map)session.getAttribute("userMap");
		List<Map> result=orderEditService.appCheck(userMap, eqlPage,session);
		
		model.addAttribute("results", result);
    	return "pages/orderEdit/orderEditList";
    }
    //删除订单信息
    @RequestMapping(value = "/deleteSubOrder", method = RequestMethod.POST)
    @ResponseBody
    public String deleteSubOrder(HttpServletRequest request,Model model) {
        HttpSession session = request.getSession();
        LogInfo usr = (LogInfo) session.getAttribute("logUser");
        String parentId =  request.getParameter("SUBSCRIBE_ID");
        Map inMap = Maps.newHashMap();
        inMap.put("SUBSCRIBE_ID", parentId.trim());
        Map returnMap = Maps.newHashMap();
        returnMap.put("tag", "0");
        try {
            log.info("------------------删除订单-----------------");
            log.info("参数:"+inMap.toString()+"  操作人:"+usr.getStaff_id());
            orderEditService.deleteOrder(inMap);
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put("msg", "删除失败！"+e.getMessage());
            log.info("------------------删除订单失败-----------------");
            log.info("失败原因"+e.getMessage());
            return JSON.toJSONString(returnMap);
        }
        returnMap.put("msg", "删除成功");
        log.info("------------------删除订单结束-----------------");
        return JSON.toJSONString(returnMap);
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
