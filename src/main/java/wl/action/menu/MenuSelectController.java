package wl.action.menu;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wl.action.ConfirmPrintInController;
import wl.common.utils.OrderUtils;
import wl.entity.CustomerInfo;
import wl.entity.OrderInfo;
import wl.entity.Payment;

import com.google.common.collect.Maps;

@Controller
public class MenuSelectController {
    @Autowired
    @Qualifier("confirmControler")
    private ConfirmPrintInController confirmPrintInController;
    //零售订单添加
    @RequestMapping(value="/saleOrderInfo",method=RequestMethod.GET) 
    public String toSaleRorderInfo(){
        return "pages/saleOrderInfo";
    }
  //发票确认
    @RequestMapping(value="/sureInvoice",method=RequestMethod.GET) 
    public String toSureInvoice(){
        return "pages/sureInvoice/sureInvoice";
    }
    //印刷单制表及申请历史查询
    @RequestMapping(value="/printTableHistory",method=RequestMethod.GET) 
    public String qryPrintTblHistory(){
        return "pages/printTable/printTableHistory";
    }
    //印刷单制表及申请
    @RequestMapping(value="/printTable",method=RequestMethod.GET) 
    public String toPrintTableRorderInfo(){
        return "pages/printTable/printTable";
    }
    
  //订单报表
    @RequestMapping(value="/order_forms",method=RequestMethod.GET) 
    public String toOrderForms(){
        return "pages/orderForm/order_forms";
    }
  //订单套数报表
    @RequestMapping(value="/orderDetail_forms",method=RequestMethod.GET) 
    public String toOrderDetailForms(){
        return "pages/orderForm/orderDetail_forms";
    }
  //配送报表
    @RequestMapping(value="/send_forms",method=RequestMethod.GET) 
    public String toSendForms(){
        return "pages/orderForm/send_forms";
    }
  //领导报表
    @RequestMapping(value="/leader_forms",method=RequestMethod.GET) 
    public String toLeaderForm(){
        return "pages/leaderForm/leader_forms";
    }
  //财务报表
    @RequestMapping(value="/fanance_forms",method=RequestMethod.GET) 
    public String toFananceForms(){
        return "pages/fananceform/fanance_forms";
    }
    //增刊样刊订单添加
    @RequestMapping(value="/supplementOrderInfo",method=RequestMethod.GET) 
    public String toSupplementRorderInfo(){
        return "pages/supplementOrderInfo";
    }
    //审批查询页面
    @RequestMapping(value="/approvalCheck",method=RequestMethod.GET) 
    public String toApprovalCheckRorderInfo(){
        return "pages/approval/approvalCheck";
    }
    //出库管理
    @RequestMapping(value = "orderOut",method=RequestMethod.GET)
    public String toOrderOut() {
        return "pages/orderOut/orderOut";
    }
    //订单查询修改
    @RequestMapping(value = "orderEdit",method=RequestMethod.GET)
    public String toOrderEdit() {
        return "pages/orderEdit/orderEdit";
    }
    //领导审批页面
    @RequestMapping(value="/ldapproval",method=RequestMethod.GET) 
    public String toLdApprovalRorderInfo(){
        return "pages/ldapproval/ldapproval";
    }
    //订阅订单信息填写
    @RequestMapping(value = "toOrderInfoInput")
    public String topage(Model model) {
        model.addAttribute("OrderInfo", new OrderInfo());
        return "pages/reForm";
    }
    
    //订单汇款单绑定
    @RequestMapping(value = "toOrdPayBind")
    public String toPayBindPage(Model model) {
        model.addAttribute("OrderInfo", new OrderInfo());
        return "pages/orderbinding/orderBinding";
    }
    
    //印刷确认入库
    @RequestMapping(value="toConfirmPrint",method=RequestMethod.GET)
    public String toConfirmPrint(Model model){
        List<Map> prodList= confirmPrintInController.initProductList(); 
        model.addAttribute("prodList",prodList);
        return "pages/stock/printConfirmIn";
    }
    //发票录入界面
    @RequestMapping(value="toInvoiceEntry",method=RequestMethod.GET)
    public String toInvoiceInPage(Model model){
        return "pages/invoice/invoiceEntry";
    }
    
    // 主要客户信息维护
    @RequestMapping(value="/customer",method=RequestMethod.GET) 
    public String toCustomer(Model model){
        model.addAttribute("customer", new CustomerInfo());
        return "pages/cust/customer_info_maintain";
    }
    
    // 回款单维护
    @RequestMapping(value="/payment",method=RequestMethod.GET) 
    public String toPayment(Model model){
        model.addAttribute("payment", new Payment());
        return "pages/payment/paymentService";
    }
    
    // 库存明细查询
    @RequestMapping(value="/stockCheck",method=RequestMethod.GET) 
    public String toStock(Model model){
        //model.addAttribute("payment", new Payment());
        return "pages/stock/stockCheck";
    }
    
    // 配送单状态修改
    @RequestMapping(value="/statusEdit",method=RequestMethod.GET) 
    public String toStatusEdit(Model model){
        return "pages/distribut/statusEdit";
    }
    
    // 财务确认收款
    @RequestMapping(value="/financeConfirm",method=RequestMethod.GET) 
    public String toFinance(Model model){
        return "pages/finance/financeConfirm";
    }
    
    // 销售情况查询
    @RequestMapping(value="/salehistory",method=RequestMethod.GET) 
    public String toSale(Model model){
        return "pages/sale/salehistory";
    }
    
  //订单详情,编辑页面
    @RequestMapping(value="toOrderEdit",method=RequestMethod.GET)
    public String toOrderEdit(Model model,String subscribeId,String key){
        Map paraMap = Maps.newHashMap();
        paraMap.put("SUBSCRIBE_ID", subscribeId);
        Map custMap = OrderUtils.queryOrders(paraMap).get(0);
        if(custMap.get("DISCOUNT_RATE")==null){
            custMap.put("DISCOUNT_RATE", "");
        }
        model.addAttribute("cust", custMap);
        model.addAttribute("optionKey",key);
        
        return "pages/detail";
    }
    //领导发行数据统计表 展示订阅和库存信息
    @RequestMapping(value = "/saleStockForm", method = RequestMethod.GET)
    public String saleStockForm() {
        return "pages/leaderForm/sale_stock_form";
    }
    
    @RequestMapping(value = "/postManage", method = RequestMethod.GET)
    public String toPsotManage() {
        return "pages/post/post_info_manage";
    }
    @RequestMapping(value = "/passModify", method = RequestMethod.GET)
    public String toPassModify() {
        return "pages/pass_change";
    }
    
    @ModelAttribute("eqlPage")
    public EqlPage getEqlPage(EqlPage eqlPage) {
        if (eqlPage.getPageRows() == 0)
            eqlPage.setPageRows(10);
        return eqlPage;
    }

}
