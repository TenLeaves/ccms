package wl.action;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.EqlTran;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import wl.entity.CustomerInfo;
import wl.entity.LogInfo;
import wl.service.StatusEditService;

import com.alibaba.fastjson.JSON;


@Controller
@SessionAttributes({"selStatusPram"})
public class StatusEditController {
   
    @Resource
    StatusEditService statusEditService;
    
    // 查询按钮 查询库存明细
    @RequestMapping(value="/selAllStatus",method=RequestMethod.POST) 
    public String selAllStatus(HttpServletRequest request, Model model,EqlPage eqlPage){
        
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); // 初始化 eqlPage总数为0， 这样每次查询都是查的最新的总数
        
        Map requestMap=request.getParameterMap();
        String pramString = ((String [])requestMap.get("statusPram"))[0];
        Map map = (Map)JSON.parse(pramString);
        
        List<Map> statusList = statusEditService.selStatus(map, eqlPage);
        
        model.addAttribute("results", statusList);
        model.addAttribute("selStatusPram", map);
        
        return "pages/distribut/statuslist";
    }
    
    // 分页查询库存明细
    @RequestMapping(value="/selStatus",method=RequestMethod.POST) 
    public String selStatus(@ModelAttribute("selStatusPram")Map map, Model model,EqlPage eqlPage){
        
        List<Map> statusList = statusEditService.selStatus(map, eqlPage);
        
        model.addAttribute("results", statusList);
        
        return "pages/distribut/statuslist"; 
    }
    
    // 更新状态
    @RequestMapping(value="/updateStatus",method=RequestMethod.POST) 
    @ResponseBody
    public Map updateStatus(HttpServletRequest request ){
        
        Map requestMap=request.getParameterMap();
        LogInfo logUser = (LogInfo) request.getSession().getAttribute("logUser");
        String pramString = ((String [])requestMap.get("distribut"))[0];
        Map map = (Map)JSON.parse(pramString);
        
        // 1 正常 2 暂停 3 退货
        if("1".equals(map.get("returnFlag"))){ // 退货
            map.put("updateStatus", "3");  // 变为退货
        }else{
            if("1".equals(map.get("status"))){
                map.put("updateStatus", "2"); // 从正常变为暂停
            }else if("2".equals(map.get("status"))){
                map.put("updateStatus", "1");  // 从暂停变为正常
            }
        }
        
        String[] date=((String)map.get("date")).split("-");
        // 月份12月变为 13，为了查询更新合刊
        String month = date[1];
        String year = date[0];
        map.put("year", year);
        map.put("month", month);
        map.put("staffId", logUser.getStaff_id());
        if("1".equals(map.get("returnFlag"))){ // 退货
            Eql eql=statusEditService.getEql();
            EqlTran tran=eql.newTran();
            try{
            statusEditService.setEql(eql);
            statusEditService.updateStatusNew(map);
            map.put("exportInfo","1");
            Map inMap=statusEditService.selectStatusNew(map);
            inMap.put("staffId", map.get("staffId"));
            inMap.put("printCode",inMap.get("YEAR")+""+inMap.get("MONTH")+""+inMap.get("PRODUCT_TYPE"));
            statusEditService.updatestock(inMap);
            tran.commit();
            }catch(Exception e){
            	e.printStackTrace();
            	tran.rollback();
            }
        }else{
        	statusEditService.updateStatus(map);
        }
        
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
