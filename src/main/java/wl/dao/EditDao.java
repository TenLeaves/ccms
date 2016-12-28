package wl.dao;

import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.tool.Extension.Param;
import org.n3r.eql.Eql;
import org.springframework.stereotype.Repository;

import wl.common.utils.OrderUtils;

@Repository("editDao")
public class EditDao {
    private static Eql eql = new Eql();
    /**
     * 获取查询到的需要删除的配送单的信息
     * @param paraMap
     * @return
     */
    public Map qrydeletedSendInfo(Map paraMap) {
        return OrderUtils.qrySendListOnly(paraMap).get(0);
    }
    /**
     * 回复库存信息
     * @param paraMap
     */
    public void recoverStock(Map paraMap) {
        eql.update("updateStockInfo").params(paraMap).evaluate();
    }
   /**
    * 根据配送单中查询到的仓库类别获取库存信息
    * @param stockType
    * @return
    */
    public Map getStockInfo(Map paraMap) {
       return (Map) eql.selectFirst("selectStockInfo").params(paraMap).execute();
    }

    public Eql getEql() {
        return eql;
    }

    public void setEql(Eql eql) {
        this.eql = eql;
    }
    public void insertSendInfo(Map paraMap) {
        eql.insert("inserSendinfo").params(paraMap).execute();
    }
    
    public void updateSendInfo(Map paraMap) {
        eql.update("updateSendInfo").params(paraMap).execute();
        
    }
    public List<Map> qryInvoice(Map paraMap) {
        
        return eql.select("selectInvoice").params(paraMap).execute();
    }
	public void insertInvoice(Map paraMap) {
		eql.insert("addNewInvoice").params(paraMap).execute();
	}
	public void deleteInvoice(Map paraMap) {
		eql.delete("deleteInvoiceInfo").params(paraMap).execute();
		
	}
    public void updateSendFirst(Map paraMap) {
        eql.update("updateSendFirst").params(paraMap).execute();
        
    }
    public void updateInvoice(Map paraMap) {
        eql.update("updateInvoce").params(paraMap).execute();
        
    }
    public void updateInvioceStateInSubScribe(Map paraMap) {
        eql.update("updateSubScribleInvoiceState").params(paraMap).execute();
        
    }
    

}
