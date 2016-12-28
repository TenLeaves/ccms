package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import wl.dao.WorkInfoQryDao;
import wl.entity.LogInfo;

@Service("initWorkInfoSrv")
public class InitIndexService {
    
    @Autowired
    @Qualifier("wordInfoQryDao")
    private WorkInfoQryDao workInfoQryDao;
    public List<Map> qryRoleList(LogInfo loginfo) {
        Map paraMap = Maps.newHashMap();
        paraMap.put("STAFF_CODE", loginfo.getStaff_id());
        
        return workInfoQryDao.qryRoleList(paraMap);
    }
    
    //发行 查询需要绑定的汇款单
    public List<Map> qryOrderToBind(LogInfo loginfo,EqlPage eqlpage) {
        return workInfoQryDao.qryAllPayMent(eqlpage);
    }
    //待领导审批
    public List<Map> qryOrderToBeVerify(Map paraMap,EqlPage eqlpage) {
        
        return workInfoQryDao.qryOrderNotPassed(paraMap, eqlpage);
    }
    
    //待财务确认收款
    public List<Map> qrySaleOrderTobePay(Map paraMap,EqlPage eqlpage) {
        return workInfoQryDao.qrySaleOrderTobePay(paraMap,eqlpage);
    }
    
    //库管  确认入库 
    public List<Map> qryPrintTobeIn(Map paraMap,EqlPage eqlpage) {
        return workInfoQryDao.qryPrintTobeIn(paraMap,eqlpage);
    }

    public List<Map> invoiceToType(EqlPage eqlPage) {
        
        return workInfoQryDao.invoiceToType(eqlPage);
    }
    
    //查询已经打印的发票
    public List<Map> invoiceHasTyped(Map paraMap, EqlPage eqlPage) {
       
        return workInfoQryDao.invoiceHasType(paraMap,eqlPage);
    }

}
