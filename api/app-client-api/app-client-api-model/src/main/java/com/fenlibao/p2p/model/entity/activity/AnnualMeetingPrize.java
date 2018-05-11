package com.fenlibao.p2p.model.entity.activity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 年会活动奖品列表
 */
public class AnnualMeetingPrize implements Serializable {

    private String prizeName;

    private String prizeCode;

    private String type;
	private Integer qty;

    private int id;
    private BigDecimal amout;

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeCode() {
        return prizeCode;
    }

    public void setPrizeCode(String prizeCode) {
        this.prizeCode = prizeCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmout() {
        return amout;
    }

    public void setAmout(BigDecimal amout) {
        this.amout = amout;
    }

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}
}
