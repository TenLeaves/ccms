package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Service;

@Service
public class FinanceConfirmService {
    
    private Eql eql = new Eql();
    
    // 更新状态
    public int updateFinance(Map map){
        
        int ret = eql.update("updateFinance")
                .params(map)
                .execute();
        
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        return ret;
    }

    public void addRemarkToOrder(Map map) {
        // TODO Auto-generated method stub
        eql.update("addRemarkToOrder")
        .params(map)
        .execute();
    }
}
