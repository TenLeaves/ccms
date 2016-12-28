package wl.service;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Service;

import wl.entity.Payment;

@Service
public class PaymentService {
    
    // 添加回款单
    public int addPayment(Payment pay){
        
        int ret = new Eql().insert("addPay")
                .params(pay)
                .execute();
        
        return ret;
    }
    
    // 更新回款单
    public int updatePay(Payment pay){
        
        int ret = new Eql().update("updatePay")
                .params(pay)
                .execute();
        
        return ret;
    }
    
    // 查询客户信息
    public List<Payment> selPay(Payment pay,EqlPage eqlPage){
        Eql eql=new Eql();
        List<Payment> payList = eql.select("selPay")
                .limit(eqlPage)
                .returnType(Payment.class)
                .params(pay)
                .execute();
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        
        return payList;
    }

    public void deletePay(Map paraMap) {
        new Eql().delete("deletePayMentById").params(paraMap).execute();
    }
}
