package com.fenlibao.service.pms.da.biz.loanmanage.impl;

import com.fenlibao.dao.pms.da.biz.loanList.LoanListMapper;
import com.fenlibao.dao.pms.da.biz.loanmanage.LoanManageMapper;
import com.fenlibao.model.pms.da.biz.form.LoanManageForm;
import com.fenlibao.model.pms.da.biz.viewobject.BidVO;
import com.fenlibao.model.pms.da.biz.viewobject.EntrustPayBid;
import com.fenlibao.p2p.model.xinwang.entity.user.XinwangUserInfo;
import com.fenlibao.p2p.model.xinwang.enums.account.AuditStatus;
import com.fenlibao.p2p.model.xinwang.exception.XWTradeException;
import com.fenlibao.p2p.service.xinwang.account.impl.XWUserInfoServiceImpl;
import com.fenlibao.p2p.service.xinwang.entrust.impl.XWEntrustServiceImpl;
import com.fenlibao.p2p.service.xinwang.project.impl.XWEstablishProjectServiceImpl;
import com.fenlibao.service.pms.da.biz.loanmanage.LoanManageService;
import com.fenlibao.service.pms.da.exception.BidVerificationException;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发标管理
 * <p>
 */
@Service
public class LoanManageServiceImpl implements LoanManageService {
    @Autowired
    private LoanManageMapper loanManageMapper;

    @Autowired
    private LoanListMapper loanListMapper;

    @Autowired
    private XWEstablishProjectServiceImpl xwEstablishProjectService;

    @Autowired
    private XWEntrustServiceImpl xwEntrustService;

    @Autowired
    private XWUserInfoServiceImpl xwUserInfoService;

    private static final Logger logger = LogManager.getLogger(LoanManageServiceImpl.class);

    @Override
    public List<BidVO> search(LoanManageForm loanManageForm, RowBounds bounds) throws Throwable {
        List<Integer> bidIds = new ArrayList<>();
        if(loanManageForm.getCreateTimeStart() != null && !loanManageForm.getCreateTimeStart().trim().equals("")){
            loanManageForm.setCreateTimeStartWork(loanManageForm.getCreateTimeStart() + " 00:00:00");
        }
        if(loanManageForm.getCreateTimeEnd() != null && !loanManageForm.getCreateTimeEnd().trim().equals("")){
            loanManageForm.setCreateTimeEndWork(loanManageForm.getCreateTimeEnd() + " 23:59:59");
        }
        List<BidVO> list = loanManageMapper.search(loanManageForm, bounds);
        if(list.size() > 0){
            for (BidVO bidVO: list) {
                if(!bidIds.contains(bidVO.getBidId())){
                    bidIds.add(bidVO.getBidId());
                }
            }
            List<Map<String, Object>> entrustPayBidList = new ArrayList<>();
            if(bidIds.size() > 0){
                entrustPayBidList = loanManageMapper.getEntrustPayBidState(bidIds);
            }
            for (BidVO bidVO: list) {
                Map<String, Object> entrustPayBidMap = buildEntrustPayBid(entrustPayBidList, bidVO.getBidId());
                if(entrustPayBidMap != null){
                    // 委托支付标
                    if (bidVO.getProjectType() != null && bidVO.getProjectType().equals("ENTRUST_PAY")){
                        String state = entrustPayBidMap.get("state") == null ? null : entrustPayBidMap.get("state").toString();
                        //委托支付标授权成功
                        if(state != null && state.equals("CG")){
                            bidVO.setIsRelease(0);//不可再次发起请求
                        }
                    }
                }
                if(bidVO.getRate() != null){
                    bidVO.setRate(new BigDecimal(bidVO.getRate()).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).toString());
                }
                if(bidVO.getBidAmount() != null && bidVO.getCanInvestAmount() != null){
                    bidVO.setTenderAmount(new BigDecimal(bidVO.getBidAmount()).subtract(new BigDecimal(bidVO.getCanInvestAmount())).toString());
                }
                if(bidVO.getDisplayTime() != null){
                    if(new Date().getTime() - bidVO.getDisplayTime().getTime() >= 0 ){
                        bidVO.setIsRelease(0);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public String release(int loanId) throws Exception {
        String code = null;
        BidVO bidVO = loanManageMapper.getProjictInfoByLoanId(loanId);
        // 预发布后，如果标已经显示就不能再按发布了
        if (bidVO.getDisplayTime() != null) {
            if (new Date().getTime() - bidVO.getDisplayTime().getTime() >= 0) {
                throw new BidVerificationException("该标已显示,不能进行发布操作!");
            }
        }
        String status = loanListMapper.getBidStatus(loanId);
        EntrustPayBid entrustPayBid = loanListMapper.getEntrustPayBidByLoanId(loanId);
        // 借款人必须是已经鉴权通过或审核通过
        if(entrustPayBid != null && entrustPayBid.getBorrowerPlatformUserNo() != null){
            XinwangUserInfo borrowersUserInfo = new XinwangUserInfo();
            try{
                borrowersUserInfo = xwUserInfoService.queryUserInfo(entrustPayBid.getBorrowerPlatformUserNo());
            }catch(Exception e){
                logger.error("[查询借款人存管信息异常:]" + e.getMessage(), e);
                throw e;
            }
            if(borrowersUserInfo != null){// 用户已注册新网
                //借款人审核状态不通过或者是鉴权不通过
                if((borrowersUserInfo.getAuditStatus() != null && !borrowersUserInfo.getAuditStatus().equals(AuditStatus.PASSED.getCode())) ||
                        (borrowersUserInfo.getAccessType() != null && borrowersUserInfo.getAccessType().equals("NOT_AUTH"))){
                    throw new BidVerificationException("借款人必须是已经鉴权通过或审核通过!");
                }
            }else{
                throw new BidVerificationException("查不到借款人存管开户信息!");
            }
        }
        // 受托方必须是已经鉴权通过或审核通过
        if(entrustPayBid != null && entrustPayBid.getEntrustedPlatformUserNo() != null){
            XinwangUserInfo entrustedUserInfo = new XinwangUserInfo();
            try{
                entrustedUserInfo = xwUserInfoService.queryUserInfo(entrustPayBid.getEntrustedPlatformUserNo());
            }catch(Exception e){
                logger.error("[查询受托方存管信息异常:]" + e.getMessage(), e);
                throw e;
            }
            if(entrustedUserInfo != null){
                // 受托方审核状态不通过或者是鉴权不通过
                if((entrustedUserInfo.getAuditStatus() != null && !entrustedUserInfo.getAuditStatus().equals(AuditStatus.PASSED.getCode())) ||
                        (entrustedUserInfo.getAccessType() != null && entrustedUserInfo.getAccessType().equals("NOT_AUTH"))){
                    throw new BidVerificationException("受托方必须是已经鉴权通过或审核通过!");
                }
            }else{
                throw new BidVerificationException("查不到受托方存管开户信息!");
            }
        }
        //没有
        if(status.equals("DFB")){
            try {
                xwEstablishProjectService.establishProject(loanId);
                status = loanListMapper.getBidStatus(loanId);
                if(!status.equals("TBZ")){
                    code = "1000";// 发布中/发布失败
                }else if (status.equals("TBZ")){
                    // 委托支付标授权
                    code = "2000";//发布成功
                }
            }catch (XWTradeException e) {
                logger.error("[调用存管发布产生的异常:]" + e.getMessage(), e);
                throw e;
            }
        }else{
            throw new BidVerificationException("不是待发布状态,不能发布");
        }
        return code;
    }

    @Override
    public void sealedBidding(int loanId) throws Exception {
        loanManageMapper.sealedBidding(loanId);
    }

    private Map<String, Object> buildEntrustPayBid(List<Map<String, Object>> entrustPayBidList, Integer bidId){
        if(entrustPayBidList.size() > 0){
            for (Map<String, Object> entrustPayBid : entrustPayBidList) {
                if (bidId.toString().equals(entrustPayBid.get("bidId").toString())) {
                    return entrustPayBid;
                }
            }
        }
        return null;
    }

}
