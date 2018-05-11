package com.fenlibao.dao.pms.da.bidType;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.fenlibao.model.pms.da.bidType.BidType;

public interface BidTypeMapper {

    /**
     * 查询标类型
     *
     * @param excludeCodes 排除列表
     * @return
     */
    List<BidType> getBidTypes(@Param("excludeCodes") List<String> excludeCodes);

    /**
     * 列出所有的标的类型(只是启用的)
     */
    List<BidType> getAllUsedBidType();

    /**
     * 根据返现券id查询对应的标的限制
     *
     * @param redPacketId
     * @return
     */
    List<String> getBidTypesByRedPacketId(Integer redPacketId);

    /**
     * 根据加息券id查询对应的标的限制
     *
     * @param rateCouponId
     * @return
     */
    List<Integer> getBidTypeIdsByRateCouponId(int rateCouponId);

    /**
     * 在新增的时候往中间表里面插入数据
     */
    Integer insertRedPackageBidType(@Param(value = "redPackageId") Integer redPackageId,
                                    @Param(value = "bidTypeId") Integer bidTypeId);

    /**
     * 根据红包id查询出对应的标的类型id
     *
     * @param redPacketId
     * @return
     */
    List<Integer> getBidTypeIdsByRedPacketId(Integer redPacketId);

    /**
     * 删除中间表对应记录
     *
     * @param oldType
     */
    void deleteRedPackageBidType(@Param(value = "redPacketId") Integer redPacketId, @Param(value = "oldTypeId") Integer oldTypeId);
}
