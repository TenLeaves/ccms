package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Service;

@Service
public class StockCheckService {
    
    // 查询库存
    public List<Map> selStock(Map map,EqlPage eqlPage){
        
        List<Map> stockList = new Eql().select("selStock")
                .limit(eqlPage)
                .params(map)
                .execute();
        
        return stockList;
    }
    
}
