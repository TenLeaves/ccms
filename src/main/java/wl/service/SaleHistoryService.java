package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Service;

@Service
public class SaleHistoryService {
    
    private Eql eql = new Eql();
    
    // 查询已导出配送单
    public List<Map> selSale(Map map,EqlPage eqlPage){
        
        List<Map> saleList = eql.select("selSale")
                .limit(eqlPage)
                .params(map)
                .execute();
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
       
        return saleList;
    }
    
}
