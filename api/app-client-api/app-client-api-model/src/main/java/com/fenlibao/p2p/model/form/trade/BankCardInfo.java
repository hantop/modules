package com.fenlibao.p2p.model.form.trade;

/**
 * 
 * 银行卡信息
 * 
 * @author  heshiping
 * @version  [版本号, 2015年8月24日]
 */
public class BankCardInfo
{
    /**
     * 银行卡开户名
     */
    private String realName;
    
    /**
     * 银行卡号
     */
    private String bankCardNo;
    
    /**
     * 银行代码
     */
    private String bankCode;
    
    /**
     * 银行名称
     */
    private String bankName;
    
    /**
     * 对私对公；1：个人，2：公司'
     */
    private String flag;
    
    /**
     * 城市编码
     */
    private String cityCode;
    
    /**
     * 支行名称
     */
    private String branchName;
    
    private String bindStatus;
    
    
    public String getFlag()
    {
        return flag;
    }

    public void setFlag(String flag)
    {
        this.flag = flag;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getBankCardNo()
    {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo)
    {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCode()
    {
        return bankCode;
    }

    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }

    public String getBankName()
    {
        return bankName;
    }

    public void setBankName(String bankName)
    {
        this.bankName = bankName;
    }

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}
    
}