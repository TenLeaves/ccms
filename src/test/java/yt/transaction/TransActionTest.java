package yt.transaction;

import java.sql.SQLException;
import java.util.Map;

import org.n3r.eql.EqlTran;

import com.google.common.collect.Maps;

public class TransActionTest {

    public static void main(String[] args) {
        DaoDemo daoDemo =new DaoDemo();
        EqlTran tans = daoDemo.getEql().newTran();
        try {
            
            Map paraMap = Maps.newHashMap();
            paraMap.put("PRINT_CODE", "2011011");
            paraMap.put("STOCK_TYPE", "1");
            paraMap.put("OCCUPY_AMOUNT", "20");
            daoDemo.update(paraMap);
            
           // exception();
            
            
            paraMap.put("PRINT_CODE", "2011011");
            paraMap.put("STOCK_TYPE", "2");
            paraMap.put("OCCUPY_AMOUNT", "50");
            daoDemo.update(paraMap);
            
            tans.commit();
            
        } catch (Exception e) {
                tans.rollback();
            e.printStackTrace();
        }
        
        
        
    }

    private static void exception() throws Exception {
        throw new Exception("哈哈哈哈哈");
    }
}
