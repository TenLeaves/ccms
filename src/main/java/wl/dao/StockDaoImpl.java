package wl.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.n3r.eql.Eql;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

@Repository("stockDao")
public class StockDaoImpl {
    private Eql eql = new Eql();

    public List<Map> qryProTobePrint() {
        
        return eql.select("selectProdToPrint").execute();
    }

    public Map qryProdByprodCode(Map paraMap) {
       
        return eql.selectFirst("qryCertainProdByCode").params(paraMap).execute();
    }

    public void savePrintInfo(Map amountMap) throws Exception{
        try {
            eql.getConnection().setAutoCommit(false);
            Map paraMap = Maps.newHashMap();
            paraMap.put("PRINT_ID", amountMap.get("printId"));
            
            paraMap.put("prodId", amountMap.get("prodId"));
            paraMap.put("stockType", "1");
            paraMap.put("amount", amountMap.get("zzsneedAmt"));
            eql.insert("insertPrintInfo").params(paraMap).execute();
            
            paraMap.put("stockType", "2");
            paraMap.put("amount", amountMap.get("officeneedAmt"));
            eql.insert("insertPrintInfo").params(paraMap).execute();
            
            paraMap.put("stockType", "3");
            paraMap.put("amount", amountMap.get("factryneedAmt"));
            eql.insert("insertPrintInfo").params(paraMap).execute();
            
            eql.getConnection().commit();
            
        } catch (Exception e) {
            try {
                eql.getConnection().rollback();
            } catch (SQLException e1) {
               throw new RuntimeException("数据库异常");
            }
        }
        
        
    }

    public void updatePrintPageState(Map amountMap) {
        Map paraMap = Maps.newHashMap();
        paraMap.put("PRINT_ID", amountMap.get("printId"));
        paraMap.put("PRINT_CODE", amountMap.get("prodId"));
        eql.update("updatePrintpage").params(paraMap).execute();
        
    }
public List<Map> qryProdStockInfo(Map paraMap) {
        
        return eql.select("selectProdStockInfo").params(paraMap).execute();
    }

public void handlerAddStock(Map inMap) throws Exception{
    eql.update("addStockAmount").params(inMap).execute();
    
}

public void handlerMinusStock(Map paraMap) throws Exception{
    eql.update("minusStockAmount").params(paraMap).execute();
    
}

public Eql getEql() {
    return eql;
}

public void setEql(Eql eql) {
    this.eql = eql;
}
}
