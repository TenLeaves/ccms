package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.springframework.stereotype.Repository;


@Repository("saleStockInfoDao")
public class SaleStockInfoDao {

    public List <Map> qrySaleInfoForm(Map paraMap) {
        return new Eql().select("qrySaleinfoAsCustType").params(paraMap).execute() ;
    }

    public List<Map> qryStockInfoForm(Map paraMap) {
        
            return new Eql().select("qryStockInfoAsMonth").params(paraMap).execute();
        
    }

}
