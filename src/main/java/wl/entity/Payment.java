package wl.entity;

// 汇款单实体类
public class Payment {
    private String paymentId; // 汇款单号
    private String paymentName; // 付款人户名
    private String paymentValue; // 汇款金额
    private String bindFlag;// 是否绑定标识 1未绑定 2已绑定
    private String bindFlagDes; // 绑定标识说明  1未绑定 2已绑定
    private String subscribeId; // 绑定订单号
    private String updateTime;// 更新时间
    private String updateStaff ; // 更新员工
    private String remark ; // 备注
    private String paymentFlag;//钱款是否收到1未收到，2收到
    private String postCompany;//邮递单位
    private String paymentTime;//汇款时间
    private String paymentPhone;//汇款人电话
    private String paymentAddress;//汇款人地址
    private String buyCount;//购买数
    private String postCode;//邮编
    
    public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPaymentTime() {
		return paymentTime;
	}

	public String getPaymentPhone() {
		return paymentPhone;
	}

	public void setPaymentPhone(String paymentPhone) {
		this.paymentPhone = paymentPhone;
	}

	public String getPaymentAddress() {
		return paymentAddress;
	}

	public void setPaymentAddress(String paymentAddress) {
		this.paymentAddress = paymentAddress;
	}

	public String getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(String buyCount) {
		this.buyCount = buyCount;
	}

	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime+" 00:00:00";
	}

	public String getPostCompany() {
		return postCompany;
	}

	public void setPostCompany(String postCompany) {
		this.postCompany = postCompany;
	}

	public String getPaymentId() {
        return paymentId;
    }
    
    public String getPaymentFlag() {
		return paymentFlag;
	}

	public void setPaymentFlag(String paymentFlag) {
		this.paymentFlag = paymentFlag;
	}

	public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    public String getPaymentName() {
        return paymentName;
    }
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
    public String getPaymentValue() {
        return paymentValue;
    }
    public void setPaymentValue(String paymentValue) {
        this.paymentValue = paymentValue;
    }
    public String getBindFlag() {
        return bindFlag;
    }
    public void setBindFlag(String bindFlag) {
        this.bindFlag = bindFlag;
    }
    public String getSubscribeId() {
        return subscribeId;
    }
    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateStaff() {
        return updateStaff;
    }
    public void setUpdateStaff(String updateStaff) {
        this.updateStaff = updateStaff;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getBindFlagDes() {
        return bindFlagDes;
    }
    public void setBindFlagDes(String bindFlagDes) {
        this.bindFlagDes = bindFlagDes;
    }
    
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return this.paymentName+"；"+this.postCompany+";"+this.paymentValue+";"+this.paymentPhone
                +";"+this.paymentTime;
    }
    
}
