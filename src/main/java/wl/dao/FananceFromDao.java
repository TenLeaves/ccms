package wl.dao;

import java.util.List;
import java.util.Map;

import org.n3r.eql.Eql;
import org.n3r.eql.EqlPage;
import org.n3r.eql.map.EqlRun;
import org.springframework.stereotype.Repository;

import wl.service.PaymentService;

import com.google.common.collect.Lists;

@Repository("fananceFormDao")
public class FananceFromDao {
	private Eql eql = new Eql();
	

	

	
	//查询非现金的汇款单的信息
	public List<Map> qryNoneCashPayentInfo(Map paraMap) {
	    List<Map> payInfo=Lists.newArrayList();
		EqlPage eqlPage= (EqlPage) paraMap.get("eqlPage");
		String bindFlag=paraMap.get("BIND_FLAG")==null?"":paraMap.get("BIND_FLAG").toString();
		if(!"".equals(bindFlag)){
		    if("1".equals(bindFlag)){//no
		        payInfo=  eql.select("qryNoneCashPayentInfoNoBind")
                .limit(eqlPage)
                .params(paraMap)
                .execute();
		    }else{
		        payInfo=  eql.select("qryNoneCashPayentInfoBind")
		                .limit(eqlPage)
		                .params(paraMap)
		                .execute();
		    }
		}else{
//		    List noBind = eql.select("qryNoneCashPayentInfoNoBind")
//	                .limit(eqlPage)
//	                .params(paraMap)
//	                .execute();
//		    List bind = eql.select("qryNoneCashPayentInfoBind")
//                    .limit(eqlPage)
//                    .params(paraMap)
//                    .execute();
//		    payInfo.addAll(noBind);
//		    payInfo.addAll(bind);
		    payInfo = eql.select("qryNoneCashPaymentInfo").params(paraMap).limit(eqlPage).execute();
		}
		
		List<EqlRun> o = eql.getEqlRuns();
        System.out.println(o.get(0).getParamBean().toString());
        System.out.println(o.get(0).getRunSql());
        return payInfo;
	}


	public List<Map> qryCashPaymentInfo(Map paraMap) {
		// TODO Auto-generated method stub
		EqlPage eqlPage= (EqlPage) paraMap.get("eqlPage");
		List<Map> payInfo =eql.select("qryCashPayentInfo")
				.limit(eqlPage)
                .params(paraMap)
                .execute();
//		List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
		return payInfo;
	}

	//查询现金方式的汇款单
	public List<Map> selectFananceSumCash(Map paraMap) {
	    List<Map> payInfo =eql.select("selectSumInfoCash").params(paraMap).execute();
//        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        return payInfo;
	}

    //查询银行方式的汇款单
    public List<Map> selectFananceSumBank(Map paraMap) {
        List<Map> payInfo =eql.select("selectSumInfobank")
                .params(paraMap)
                .execute();
        List<EqlRun> o = eql.getEqlRuns();
        System.out.println(o.get(0).getParamBean().toString());
        System.out.println(o.get(0).getRunSql());
        return payInfo;
    }

    //查询邮局汇款方式的总数统计
    public List<Map> selectFananceSumPostOffice(Map paraMap) {
        List<Map> payInfo =eql.select("selectSumInfoOffice")
                .params(paraMap)
                .execute();
//        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        return payInfo;
    }
    
    public List<Map> qryCashPaymentInfoExport(Map paraMap) {
		List<Map> payInfo =new Eql().select("qryCashPayentInfo")
                .params(paraMap)
                .execute();
		return payInfo;
	}
    
    public List<Map> qryNoneCashPayentInfoExport(Map paraMap) {
		List<Map> payInfo =new Eql().select("qryNoneCashPayentInfo")
                .params(paraMap)
                .execute();
        return payInfo;
	}
  public List<Map> qryProdInfoById(Map paraMap) {
        
        return eql.select("qryProdInfoById").params(paraMap).execute();
    }


    public Map qryInvoiceMoneyAllById(Map paraMap) {
        // TODO Auto-generated method stub
        return eql.selectFirst("qryInvoiceMoneyAllById").params(paraMap).execute();
    }


    public List<Map> qrySendInfoByParentId(Map paraMap) {
        List<Map> payInfo =new Eql().select("qrySendInfoByParentId").params(paraMap).execute();
        List<EqlRun> o = eql.getEqlRuns();
//        System.out.println("##################################");
//        System.out.println(o.get(0).getParamBean().toString());
//        System.out.println(o.get(0).getRunSql());
        return payInfo;
    }


    public List<Map> qrySendInfoByOrderId(Map childMap) {
        
        return new Eql().select("qrySendInfoByOrderId").params(childMap).execute();
    }

}
