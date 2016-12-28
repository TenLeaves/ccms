package wl.service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import wl.common.utils.OrderUtils;
import wl.dao.EditDao;
import wl.dao.LoginUserDao;
import wl.entity.LogInfo;

import com.google.common.collect.Maps;

@Service("editService")
public class EditService {
    @Autowired
    @Qualifier("editDao")
    private EditDao editDao;
    
    @Autowired
    @Qualifier("loginUserDao")
    private LoginUserDao loginUserDao;
    private Log log = LogFactory.getLog(this.getClass());

    public void deleteSendInfo(Map paraMap,HttpServletRequest req) throws Exception {
         try {
            editDao.getEql().getConnection().setAutoCommit(false);
            
            Map delsendMap = editDao.qrydeletedSendInfo(paraMap);
            
           //年+月+产品类型  确定唯一期刊 
            String printCode=(String)delsendMap.get("YEAR")+
                    (String)delsendMap.get("MONTH")+
                    (String)delsendMap.get("PRODUCT_TYPE");
            Map stockparaMap = Maps.newHashMap();
            stockparaMap.put("STOCK_TYPE", delsendMap.get("STOCK_ADDRESS"));
            stockparaMap.put("PRINT_CODE", printCode);
            Map stockInfoMap = editDao.getStockInfo(stockparaMap);
            LogInfo logUser = (LogInfo) req.getSession().getAttribute("logUser");
            stockInfoMap.put("staffId", logUser.getStaff_id());
            revocerStock(delsendMap, stockInfoMap);
            
            log.info("配送单信息:"+delsendMap.toString()+"操作人："+logUser.getStaff_id());
            OrderUtils.deleteSendInfo(paraMap);
            
            editDao.getEql().getConnection().commit();
        } catch (SQLException e) {
            try {
                editDao.getEql().getConnection().rollback();
                throw new Exception("删除失败");
            } catch (SQLException e1) {
                throw new RuntimeException("删除配送单失败");
            }
        }
         
    
    }
    /**
     * 恢复库存信息
     * @param delsendMap
     * @param stockMap
     */
    private void revocerStock(Map delsendMap, Map stockMap) {
        String updateMount = ""+Integer.parseInt(delsendMap.get("DISTRIBUT_COUNT").toString())+
                Integer.parseInt(stockMap.get("REMAIN_COUNT").toString());
        Map updateMap = Maps.newHashMap();
        updateMap.put("STOCK_TYPE", delsendMap.get("STOCK_ADDRESS"));
        updateMap.put("REMAIN_COUNT", updateMount);
        String printCode=(String)delsendMap.get("YEAR")+(String)delsendMap.get("MONTH")+(String)delsendMap.get("PRODUCT_TYPE");
        updateMap.put("PRINT_CODE", printCode);
        updateMap.put("staffId",(String) stockMap.get("staffId"));
        editDao.recoverStock(updateMap);
    }
    /**
     * 添加配送信息
     * @param paraMap
     * @throws Exception 
     */
    public void AddSendInfo(Map paraMap) throws Exception {
        String lianxuTag = (String) paraMap.get("ISLIANXU");
        String start=(String)paraMap.get("PRODUCT_BEGIN");
        String end= (String)paraMap.get("PRODUCT_END");
        
        //判断添加的配送单的数量是否大于可分配的数量
        checkAmountBeforeHandle(paraMap);
        
        editDao.getEql().startBatch();
        if("1".equals(lianxuTag)){//连续
            dealWhenConstant(paraMap, start, end);
            
        }else{//非连续月
            String periodArr[]=start.split(",");
            for(int i=0;i<periodArr.length;i++){
                paraMap.put("YEAR", periodArr[i].substring(0,2));
                paraMap.put("MONTH", periodArr[i].substring(5));
                editDao.insertSendInfo(paraMap);
            }
        }
        editDao.getEql().executeBatch();
    }
    
    /**
     * 修改配送信息
     * @param paraMap
     * @throws Exception 
     */
    public void modifySendInfo(Map paraMap) throws Exception {
        checkAmountBeforeHandle(paraMap);
        editDao.updateSendInfo(paraMap);
        
    }
    /**
     * 校验操作后的配送但数量是否超出了可分配的数量
     * @param paraMap
     * @throws Exception
     */
    private void checkAmountBeforeHandle(Map paraMap) throws Exception {
        Map orderMap = OrderUtils.qryChildOrderOnly(paraMap).get(0);
        int amountInOrder = paseInt(orderMap.get("PRODUCT_PER_COUNT").toString());
        
        int currentMon = Calendar.getInstance().get(Calendar.MONTH)+1;
        String year = Calendar.getInstance().get(Calendar.YEAR)+"";
        String month = StringUtils.leftPad(""+currentMon,2,"0");
        paraMap.put("CURRENT_MONTH", month);
        paraMap.put("CURRENT_YEAR", year);
        
        List<Map> sendList = OrderUtils.qrySendListOnly(paraMap);
        //已经分配的数量
        int amountHasSend =0;
        for(int i=0;i<sendList.size();i++){
            int eachSendMnt= paseInt(sendList.get(i).get("DISTRIBUT_COUNT").toString());
            amountHasSend = amountHasSend+eachSendMnt;
        }
        int wantAmount = paseInt(paraMap.get("DISTRIBUT_COUNT").toString());
        if(wantAmount>amountInOrder-amountHasSend){//如果修改后的配送单数量大于剩余可分配的数量
            throw new Exception("该订单剩余可分配最大数量为："+(amountInOrder-amountHasSend));
        }
    }
    
    private void dealWhenConstant(Map paraMap, String start, String end) {
        int totalYear =caculateYear(start,end);
        if(totalYear!=0){//跨年订阅
            for(int i=0;i<=totalYear;i++){
                int beginIndex=0,endIndex=0;
                if(i==0){//第一年
                    beginIndex=paseInt(start.substring(5));
                    endIndex=12;
                    for(int j=beginIndex;j<=12;j++){
                        paraMap.put("YEAR", paseInt(start.substring(0,4))+i+"");
                        paraMap.put("MONTH", StringUtils.leftPad(""+j, 2, "0"));
                        editDao.insertSendInfo(paraMap);
                    }
                }
                else if(i!=totalYear){//中间年  1-12月
                    for(int m=1;m<=12;m++){
                        paraMap.put("YEAR", paseInt(start.substring(0,4))+i+"");
                        paraMap.put("MONTH", StringUtils.leftPad(""+m, 2, "0"));
                        editDao.insertSendInfo(paraMap);
                    }
                }else{//最后一年
                    beginIndex=1;
                    endIndex=paseInt(end.substring(5));
                    for(int n=1;n<=endIndex;n++){
                        paraMap.put("YEAR", paseInt(start.substring(0,4))+i+"");
                        paraMap.put("MONTH", StringUtils.leftPad(""+n, 2, "0"));
                        editDao.insertSendInfo(paraMap);
                    }
                }
            } 
        }else{
            String year = start.substring(0,4);
            for(int n=paseInt(start.substring(5));n<=paseInt(end.substring(5));n++){
                paraMap.put("YEAR", year);
                paraMap.put("MONTH", StringUtils.leftPad(""+n, 2, "0"));
                editDao.insertSendInfo(paraMap);
            }
        }
    }
    
    private int paseInt(String s) {
        return Integer.parseInt(s);
    }
    private int caculateYear(String start, String end) {
        return paseInt(end.substring(0,4))-paseInt(start.substring(0,4));
    }
    
    //查询用户的角色信息
    public List<Map> qryUserRol(Map userMap) {
        
        return loginUserDao.qryRoleInfo(userMap);
    }
    
    
    /**
     * 查询发票信息
     * @param paraMap
     * @return
     */
    public List<Map> queryInvoice(Map paraMap) {
        
        return editDao.qryInvoice(paraMap);
    }
    
	public void addInvoice(Map paraMap) throws Exception {
		// TODO Auto-generated method stub
	    String moneyEachInvoice = paraMap.get("INVOICE_AMOUNT").toString();
	    String amount = paraMap.get("INVOICE_COUNT").toString();
	    int wantMoney = paseInt(moneyEachInvoice)*paseInt(amount);
	    String invoiceId = paraMap.get("INVOICE_ID").toString();
	    paraMap.put("INVOICE_ID","");
	    int totalMoney = 0;
	     Map subscribleMap = OrderUtils.queryOrders(paraMap).get(0);
	     String subState=subscribleMap.get("SUBSCRIBE_STATE").toString();
	     if("1".equals(subState)){
	         throw new Exception("该订单状态为代付款，无法添加发票信息");  
	     }
	     totalMoney = paseInt(subscribleMap.get("TOTAL_MONEY").toString());
	    
	    List<Map> invoiceList = editDao.qryInvoice(paraMap);
	    int usedMoney=0,leftMoney=0;
	    for(Map invoiceMap:invoiceList){
	        String money = invoiceMap.get("INVOICE_AMOUNT").toString();
	        String nums = invoiceMap.get("INVOICE_COUNT").toString();
	        usedMoney+=paseInt(money)*paseInt(nums);
	    }
	    leftMoney=totalMoney-usedMoney;
	    if(wantMoney>leftMoney){
	        throw new Exception("该订单只剩下"+leftMoney+" 元没有开发票，您添加的发票总金额为:"+wantMoney);
	    }
	    paraMap.put("INVOICE_ID",invoiceId);
	    
		editDao.insertInvoice(paraMap);
		if((wantMoney+usedMoney)==totalMoney){//添加发票信息后开票金额打达到总金额
		   paraMap.put("INVOICE_FLAG", "3");
		}else{
		    paraMap.put("INVOICE_FLAG", "2");
		}
		updateInvioceStateInSubScribe(paraMap);
		
	}
	public void addNewInvoice(Map paraMap,int data) throws Exception {
		// TODO Auto-generated method stub
	    String moneyEachInvoice = paraMap.get("INVOICE_AMOUNT").toString();
	    String amount = paraMap.get("INVOICE_COUNT").toString();
	    int wantMoney = paseInt(moneyEachInvoice)*paseInt(amount);
	    String invoiceId = paraMap.get("INVOICE_ID").toString();
	    paraMap.put("INVOICE_ID","");
	    int totalMoney = 0;
	     Map subscribleMap = OrderUtils.queryOrders(paraMap).get(0);
	     totalMoney = paseInt(subscribleMap.get("TOTAL_MONEY").toString());
	    
	    List<Map> invoiceList = editDao.qryInvoice(paraMap);
	    int usedMoney=0,leftMoney=0;
	    for(Map invoiceMap:invoiceList){
	        String money = invoiceMap.get("INVOICE_AMOUNT").toString();
	        String nums = invoiceMap.get("INVOICE_COUNT").toString();
	        usedMoney+=paseInt(money)*paseInt(nums);
	    }
	    leftMoney=totalMoney-usedMoney;
	    if(data == 0){
	    	listMoney=leftMoney;
	    }
	    if(wantMoney>leftMoney){
	        throw new Exception("该订单只剩下"+listMoney+" 元没有开发票，您添加的发票总金额超过剩余可开发票金额");
	    }
	    paraMap.put("INVOICE_ID",invoiceId);
		editDao.insertInvoice(paraMap);
		
	}
	private int listMoney=0;
	public void deleteInvoiceInfo(Map paraMap) {
		
		editDao.deleteInvoice(paraMap);
		
	}
    public void upadateSendFirst(Map paraMap) {
        editDao.updateSendFirst(paraMap);
        
    }
    public void updateInvoiceInfo(Map paraMap) throws Exception{
        List<Map> invoiceList = editDao.qryInvoice(paraMap);
        Map invoice = invoiceList.get(0);
        if("1".equals(invoice.get("INVOICE_STATE").toString())){
            throw new Exception("修改失败！该发票已经打印不允许修改");
        }
        editDao.updateInvoice(paraMap);
        
    }
    public void updateInvioceStateInSubScribe(Map paraMap) {
        editDao.updateInvioceStateInSubScribe(paraMap);
        
    }
}
