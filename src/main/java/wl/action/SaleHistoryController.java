package wl.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import wl.common.utils.OrderUtils;
import wl.service.SaleHistoryService;

import com.alibaba.fastjson.JSON;


@Controller
@SessionAttributes({"selSailPram"})
public class SaleHistoryController {
   
    @Resource
    SaleHistoryService saleHistoryService;
    
    // 查询按钮 查询库存明细
    @RequestMapping(value="/selAllSale",method=RequestMethod.POST) 
    public String selAllFinance(HttpServletRequest request, Model model,EqlPage eqlPage){
        
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); // 初始化 eqlPage总数为0， 这样每次查询都是查的最新的总数
        
        Map requestMap=request.getParameterMap();
        String pramString = ((String [])requestMap.get("salePram"))[0];
        Map map = (Map)JSON.parse(pramString);
        
        List<Map> saleList = saleHistoryService.selSale(map, eqlPage);
        
        // 封装一下参数
        formatList(saleList);
        
        model.addAttribute("results", saleList);
        model.addAttribute("selSailPram", map);
        
        return "pages/sale/salelist";
    }
    
    // 分页查询库存明细
    @RequestMapping(value="/selSale",method=RequestMethod.POST) 
    public String selFinance(@ModelAttribute("selSailPram")Map map, Model model,EqlPage eqlPage){
        
        List<Map> saleList = saleHistoryService.selSale(map, eqlPage);
        
        // 封装一下参数
        formatList(saleList);
        
        model.addAttribute("results", saleList);
        
        return "pages/sale/salelist";
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
    
    // 封装一下参数
    public void formatList(List<Map>  list){
        for(Map sale : list){
            String month = (String)sale.get("MONTH");
            if("13".equals(month)){
                month = "合刊";
            }else{
                month = "第" + sale.get("MONTH") + "期";
            }
        
            String product ="";
            if("1".equals(sale.get("PRODUCT_TYPE"))){
                product += "专业版" + sale.get("YEAR") + "年" + month;
            
            }else if("2".equals(sale.get("PRODUCT_TYPE"))){
                product += "普及版" + sale.get("YEAR") + "年" + month;
            }
        
            sale.put("PRODUCT_TYPE_DES", product);
        }
    }
    
}
