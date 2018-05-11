package com.fenlibao.p2p.model.vo.share;

/**
 * 奖励记录VO
 * @author Mingway.Xu
 * @date 2017/2/8 17:39
 */
public class ShareRecordVO {
    private int recordId;//投标分享红包记录 t_tender_share_record.id
    private int redEnvelopId;//红包ID（flb.t_red_packet.id）or couponId(flb.t_coupon.id)
    private int total;//已分享次数

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getRedEnvelopId() {
        return redEnvelopId;
    }

    public void setRedEnvelopId(int redEnvelopId) {
        this.redEnvelopId = redEnvelopId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
