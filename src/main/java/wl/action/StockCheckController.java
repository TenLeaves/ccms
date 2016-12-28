package wl.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSON;

import wl.service.StockCheckService;


@Controller
@SessionAttributes({"selStockPram"})
public class StockCheckController {
   
    @Resource
    StockCheckService stockCheckService;
    
    // 查询按钮 查询库存明细
    @RequestMapping(value="/selAllStock",method=RequestMethod.POST) 
    public String selAllStock(HttpServletRequest request, Model model,EqlPage eqlPage){
        
        eqlPage.setStartIndex(0);
        eqlPage.setPageRows(10);
        eqlPage.setTotalRows(0); // 初始化 eqlPage总数为0， 这样每次查询都是查的最新的总数
        
        Map requestMap=request.getParameterMap();
        String pramString = ((String [])requestMap.get("stockPram"))[0];
        Map map = (Map)JSON.parse(pramString);
        
        String month = (String)map.get("month");
        if(!StringUtils.isEmpty(month)){
            String[] months = month.split(",");
            List monthlist =  Arrays.asList(months);
            map.put("monthlist", monthlist);
        }

        List<Map> stockList = stockCheckService.selStock(map, eqlPage);
        
        // 格式化结果
        formatList(stockList);
        
        model.addAttribute("results", stockList);
        model.addAttribute("selStockPram", map);
        
        return "pages/stock/stocklist";
    }
    
    // 分页查询库存明细
    @RequestMapping(value="/selStock",method=RequestMethod.POST) 
    public String selStock(@ModelAttribute("selStockPram")Map map, Model model,EqlPage eqlPage){
        
        List<Map> stockList = stockCheckService.selStock(map, eqlPage);
        
        // 格式化结果
        formatList(stockList);
        
        model.addAttribute("results", stockList);
        
        return "pages/stock/stocklist";
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
    
    public void formatList(List<Map>  list){
        for(Map map : list){
            String productType = ""; // 产品类型
            String mo = ""; // 期数
            
            
            if("1".equals(map.get("productType"))){
                productType = "专业版";
             }else{
                 productType = "普及版";
             }
            
            if("13".equals(map.get("mo"))){
                mo = "合刊";
                map.put("product",  productType + (String)map.get("ye") + "年合刊");
             }else{
                mo = (String)map.get("mo");
                map.put("product",  productType + (String)map.get("ye") + "年第" + mo + "期");
             }
            
        }
    }
}
