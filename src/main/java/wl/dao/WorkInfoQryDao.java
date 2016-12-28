package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository("wordInfoQryDao")
public class WorkInfoQryDao {
    private Eql eql = new Eql();

    public List<Map> qryRoleList(Map paraMap) {

        return eql.select("qryRoleIdByStaffId").params(paraMap).execute();
    }

    public Eql getEql() {
        return eql;
    }

    public void setEql(Eql eql) {
        this.eql = eql;
    }

    public List<Map> qryAllPayMent(EqlPage eqlpage) {
        
        return eql.select("qryAllPayMentToBind").limit(eqlpage).execute();
    }

    public List<Map> qryOrderNotPassed(Map paraMap ,EqlPage eqlPage) {
        
        return eql.select("qryOrderToBeChecked").limit(eqlPage).params(paraMap).execute();
    }

    public List<Map> qrySaleOrderTobePay(Map paraMap ,EqlPage eqlpage) {
        List<Map> resultList = Lists.newArrayList();
        resultList = eql.select("qrySaleOrderTobePay").limit(eqlpage).params(paraMap).execute();
//        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean());
//        System.out.println(o.get(0).getRunSql());
        return resultList;
    }

    public List<Map> qryPrintTobeIn(Map paraMap,EqlPage eqlpage) {
        return eql.select("qryPrintTobeIn").limit(eqlpage).params(paraMap).execute();
    }

    public List<Map> invoiceToType(EqlPage eqlPage) {
        return eql.select("qryInvoiceToType").limit(eqlPage).execute();
    }

    public List<Map> invoiceHasType(Map paraMap, EqlPage eqlPage) {
        return eql.select("qryInvoiceHasType").limit(eqlPage).params(paraMap).execute();
    }

}
