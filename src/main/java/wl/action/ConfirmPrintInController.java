package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.n3r.eql.ex.EqlConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import wl.common.utils.ProDuctUtils;
import wl.entity.LogInfo;
import wl.service.StockService;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@Repository("confirmControler")
public class ConfirmPrintInController {
    @Autowired
    @Qualifier("stockService")
    private StockService stockService;
    
    private Log log = LogFactory.getLog(this.getClass());
    
    public List<Map> initProductList() {
        List<Map> returnList = Lists.newArrayList();

        // 查询产品 2014031
        List<Map> resultList = stockService.qryAllProdToBePrinted();
        // 转化产品 2014年第3期专业版
        returnList = transferProdToDesc(resultList);

        return returnList;
    }

    @RequestMapping(value = "qryCertainProInfo")
    public String getCertainProDInfo(HttpServletRequest req, Model model) {
        String prodCode = req.getParameter("prodCode");
        String printId = req.getParameter("printId");
        // 查询产品信息
        Map paraMap = Maps.newHashMap();
        paraMap.put("PRINT_CODE", prodCode);
        paraMap.put("PRINT_ID", printId);
        Map prodInfo = stockService.queryCertianProd(paraMap);
        
        prodInfo.put("PRINT_CODE", ProDuctUtils.prodToDesc(prodInfo.get(
                    "PRINT_CODE").toString()));
         
        model.addAttribute("prodMap", prodInfo);
        return "pages/stock/printdetailInfoFrag";
    }
    //库存调拨
    @RequestMapping(value = "distributeStock")
    @ResponseBody
    public String distributeStock(HttpServletRequest req, Model model) {
        String zzsAmt = req.getParameter("zzsAmt");
        String yjAmt = req.getParameter("yjAmt");
        String yscAmt = req.getParameter("yscAmt");
        String stockFrom = req.getParameter("stockFrom");
        String printCode = req.getParameter("printCode");
        
        Map paraMap = Maps.newHashMap();
        paraMap.put("zzsAmt", zzsAmt);
        paraMap.put("yjAmt", yjAmt);
        paraMap.put("yscAmt", yscAmt);
        paraMap.put("printCode", printCode);
        paraMap.put("stockFrom", stockFrom);
        
        HttpSession session = req.getSession();
        LogInfo usr = (LogInfo) session.getAttribute("logUser");
        
        try {
            log.info("-----------------库存调拨--------------------");
            log.info("参数:"+paraMap.toString()+"操作员工:"+usr.getStaff_id());
            stockService.distributeStock(paraMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("-----------------库存调拨失败--------------------");
            log.info("失败原因："+e.getMessage());
            return  e.getMessage();
        }
        log.info("-----------------库存调拨结束--------------------");
        return "分配成功";
    }
    //查询某个期刊在各个仓库的库存信息
    @RequestMapping(value = "queryEachStockByProd")
    @ResponseBody
    public String queryEachStockByProd(HttpServletRequest req, Model model) {
        String period = req.getParameter("period");//2012-01
        String prodType = req.getParameter("prodType");
        // 查询产品信息
        Map paraMap = Maps.newHashMap();
        String prodCode=period.subSequence(0, 4)+period.substring(5)+prodType;
        paraMap.put("PRINT_CODE", prodCode);
        
        List <Map>prodStockInfoList = stockService.queryProdStockInfo(paraMap);
        
        Map stockMap = Maps.newHashMap();
        for(Map tempMap:prodStockInfoList){
            String stockType=tempMap.get("STOCK_TYPE").toString();
            if("1".equals(stockType)){//1 杂志社 2 邮局 3 印刷厂
                stockMap.put("zzsAmt", tempMap.get("REMAIN_COUNT"));
                stockMap.put("printCode", tempMap.get("PRINT_CODE"));
            }else if("2".equals(stockType)){
                stockMap.put("yjAmt", tempMap.get("REMAIN_COUNT"));
            }else{
                stockMap.put("yscAmt", tempMap.get("REMAIN_COUNT"));
            }
        }
        
        Map returnMap = Maps.newHashMap();
        returnMap.put("stockInfo", stockMap);
        return JSON.toJSONString(returnMap);
    }

    // 印刷单确认入库
    @RequestMapping(value = "confirmPrintIn")
    @ResponseBody
    public String confirmPrintIn(HttpServletRequest req) {
        Map paraMap = req.getParameterMap();
        Object amount = ((String[]) paraMap.get("amount"))[0];
        Map amountMap = JSON.parseObject(amount.toString());

        HttpSession session = req.getSession();
        LogInfo usr = (LogInfo) session.getAttribute("logUser");
        try {
            log.info("-----------------印刷确认入库---------------------");
            log.info("参数:"+amountMap.toString()+"操作人："+usr.getStaff_id());
            
            stockService.confirmAmountIn(amountMap);
        } catch (Exception e) {
            log.info("-----------------印刷确认入库失败---------------------");
            if(e instanceof EqlConfigException ){
                log.info("错误原因:网络异常(数据库连接超时)");
                return "网络异常(数据库连接超时)";
            }
            log.info("错误原因:"+e.getMessage());
            return e.getMessage();
        }
        log.info("-----------------印刷确认入库结束---------------------");
        return "该期刊印刷单确认入库成功";
    }

    private List<Map> transferProdToDesc(List<Map> resultMap) {
        List returnList = Lists.newArrayList();
        for (Map proMap : resultMap) {
            Map tempMap = Maps.newHashMap();
            String prodCode = proMap.get("PRINT_CODE").toString();
            tempMap.put("prodCode", prodCode);
            tempMap.put("PRINT_ID", proMap.get("PRINT_ID").toString());
            tempMap.put("prodDesc", ProDuctUtils.prodToDesc(prodCode));
            returnList.add(tempMap);
        }
        return returnList;
    }

}
