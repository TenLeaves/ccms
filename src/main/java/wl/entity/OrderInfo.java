package wl.entity;

public class OrderInfo {
    private String orderId;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String orderComplany;//
    private String contactPerson;
    private String contactPhone;// 移动电话
    private String custType; // 大客户 批发商
    private String contactTel;// 固话 前台需要做一个拼装
    private String discount;
    private String throughPerson;// 经联人
    private String throPsonPhone;// 经联人电话
    private String compyDetail;

    private String orderWay;// 电话 邮局 上门
    private String productType;
    private String orderStartDate;
    private String orderEndDate;
    private String orderCount;
    private String feeDeal;
    private String invoiceDeal;
    private String orderSeq;
    
    public String getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}

	public String getFeeDeal() {
		return feeDeal;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public void setFeeDeal(String feeDeal) {
		this.feeDeal = feeDeal;
	}

	public String getOrderComplany() {
        return orderComplany;
    }

    public void setOrderComplany(String orderComplany) {
        this.orderComplany = orderComplany;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getThroughPerson() {
        return throughPerson;
    }

    public void setThroughPerson(String throughPerson) {
        this.throughPerson = throughPerson;
    }

    public String getThroPsonPhone() {
        return throPsonPhone;
    }

    public String getInvoiceDeal() {
		return invoiceDeal;
	}

	public void setInvoiceDeal(String invoiceDeal) {
		this.invoiceDeal = invoiceDeal;
	}

	public void setThroPsonPhone(String throPsonPhone) {
        this.throPsonPhone = throPsonPhone;
    }

    public String getCompyDetail() {
        return compyDetail;
    }

    public void setCompyDetail(String compyDetail) {
        this.compyDetail = compyDetail;
    }

    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOrderStartDate() {
        return orderStartDate;
    }

    public void setOrderStartDate(String orderStartDate) {
        this.orderStartDate = orderStartDate;
    }

    public String getOrderEndDate() {
        return orderEndDate;
    }

    public void setOrderEndDate(String orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

}
