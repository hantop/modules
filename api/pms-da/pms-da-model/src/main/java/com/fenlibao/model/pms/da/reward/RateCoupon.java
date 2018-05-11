package com.fenlibao.model.pms.da.reward;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 加息券
 */
public class RateCoupon extends Coupon {
    //==================增加标的类型限制,投资期限,以及来源(实际是备注)add Lee==============
    @NotNull(message = "标的类型不能为空")
    private List<Integer> bidTypeIds = new ArrayList<Integer>(); //对象映射,来自对应的标的限制,一对多(不存入这个实体类中对应的数据库表)
    private boolean investDeadLineType;//默认为false,此时表示投资期限不限.反之,按天来计算
    private String bidTypeStr;
    private boolean canOperate; // 是否被发送名单和活动关联

    public List<Integer> getBidTypeIds() {
        return bidTypeIds;
    }

    public void setBidTypeIds(List<Integer> bidTypeIds) {
        this.bidTypeIds = bidTypeIds;
    }

    public boolean isInvestDeadLineType() {
        return investDeadLineType;
    }

    public void setInvestDeadLineType(boolean investDeadLineType) {
        this.investDeadLineType = investDeadLineType;
    }

    public String getBidTypeStr() {
        return bidTypeStr;
    }

    public void setBidTypeStr(String bidTypeStr) {
        this.bidTypeStr = bidTypeStr;
    }

    public boolean isCanOperate() {
        return canOperate;
    }

    public void setCanOperate(boolean canOperate) {
        this.canOperate = canOperate;
    }
}