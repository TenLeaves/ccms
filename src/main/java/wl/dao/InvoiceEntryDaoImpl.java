package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.springframework.stereotype.Repository;

import freemarker.template.utility.StringUtil;

import wl.common.utils.OrderUtils;
@Repository("invoiceEntryDao")
public class InvoiceEntryDaoImpl {
    private Eql eql = new Eql();
    
    public List<Map> selectOrders(Map inMap) {
        EqlPage eqlPage = (EqlPage) inMap.get("eqlPage");
        return eql.select("queryOrderCanAddInvice").limit(eqlPage).params(inMap).execute();
    }
    
	public String CreateInvoiceId(String str){
		Object s = new Eql().selectFirst("seq").params(str).execute();
		String num=StringUtil.leftPad(""+s, 6, '0');
		return num;
	}
	
    public List<Map> selectChildOrders(Map inMap) {
        
        return eql.select("selectChildOrdersById").params(inMap).execute();
    }

    public List<Map> selectTypedInvoice(Map inMap) {
        
        return eql.select("selectTypedInvoice").params(inMap).execute();
    }

    public List<Map> selectTypedInvoice1(Map inMap) {
        
        return eql.select("selectTypeInvoice1").params(inMap).execute();
    }

}
