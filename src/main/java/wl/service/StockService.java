package wl.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.n3r.eql.EqlTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import wl.dao.StockDaoImpl;

@Service("stockService")
public class StockService {

    @Autowired
    @Qualifier("stockDao")
    private StockDaoImpl stockDaoImpl;

    public StockService() {
        stockDaoImpl = new StockDaoImpl();
    }

    public List<Map> qryAllProdToBePrinted() {

        return stockDaoImpl.qryProTobePrint();
    }

    public Map queryCertianProd(Map paraMap) {

        return stockDaoImpl.qryProdByprodCode(paraMap);
    }

    public void confirmAmountIn(Map amountMap) throws Exception {

        stockDaoImpl.savePrintInfo(amountMap);
        stockDaoImpl.updatePrintPageState(amountMap);

    }

    public List<Map> queryProdStockInfo(Map paraMap) {

        return stockDaoImpl.qryProdStockInfo(paraMap);
    }

    public void distributeStock(Map paraMap) throws Exception  {
        Object zzsAmt = paraMap.get("zzsAmt");
        Object yjAmt = paraMap.get("yjAmt");
        Object yscAmt = paraMap.get("yscAmt");
        Object stockFrom = paraMap.get("stockFrom");
        Object printCode = paraMap.get("printCode");
        EqlTran tran = stockDaoImpl.getEql().newTran();
        try {
            Map inMap = Maps.newHashMap();
            if (zzsAmt != null&&StringUtils.isNotEmpty(zzsAmt.toString())) {// 分配了才操作该库存
                inMap.put("Amount", parseInt(zzsAmt.toString()));
                inMap.put("PRINT_CODE", printCode);
                inMap.put("STOCK_TYPE", "1");
                stockDaoImpl.handlerAddStock(inMap);

            }
            if (yjAmt != null&&StringUtils.isNotEmpty(yjAmt.toString())) {// 分配了才操作该库存
                inMap.put("Amount", parseInt(yjAmt.toString()));
                inMap.put("PRINT_CODE", printCode);
                inMap.put("STOCK_TYPE", "2");
                stockDaoImpl.handlerAddStock(inMap);
            }
            if (yscAmt != null&&StringUtils.isNotEmpty(yscAmt.toString())) {
                inMap.put("Amount", parseInt(yscAmt.toString()));
                inMap.put("PRINT_CODE", printCode);
                inMap.put("STOCK_TYPE", "3");
                stockDaoImpl.handlerAddStock(inMap);
            }

            // fromstock 减数量
            paraMap.clear();
            int totalWantAmt = 0;
            if (zzsAmt != null&&StringUtils.isNotEmpty(zzsAmt.toString()))
                totalWantAmt += parseInt(zzsAmt.toString());
            if (yjAmt != null&&StringUtils.isNotEmpty(yjAmt.toString()))
                totalWantAmt += parseInt(yjAmt.toString());
            if (yscAmt != null&&StringUtils.isNotEmpty(yscAmt.toString()))
                totalWantAmt += parseInt(yscAmt.toString());
            paraMap.put("Amount", totalWantAmt);
            paraMap.put("PRINT_CODE", printCode);
            paraMap.put("STOCK_TYPE", stockFrom);
            stockDaoImpl.handlerMinusStock(paraMap);
            tran.commit();
        } catch (Exception e) {
            tran.rollback();
            throw e;
        }
        
    }

    private int parseInt(String s) {

        return Integer.parseInt(s);
    }

}
