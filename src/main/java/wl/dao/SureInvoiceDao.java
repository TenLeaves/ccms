package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;
@Repository("sureInvoiceDao")
public class SureInvoiceDao {
    private Eql eql = new Eql();
    
    public List<Map> selectTypedInvoice(Map inMap,EqlPage eqlPage) {
        
        List<Map> map= eql.select("selectTypedInvoice").limit(eqlPage).params(inMap).execute();
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        return map;
    }

    public void updateInvoice(Map inMap) {
        
       eql.select("updateInvoice").params(inMap).execute();
    }

    public Map selectSub(Map inMap) {
        
        return eql.selectFirst("selectSub").params(inMap).execute();
    }
    public void updateSub(Map inMap) {
        
        eql.select("updateSub").params(inMap).execute();
     }

    public List<Map> qryPayInfoAsSubId(String subId) {
        
        return eql.select("selectPayInfoBySubId").params(subId).execute();
    }
}
