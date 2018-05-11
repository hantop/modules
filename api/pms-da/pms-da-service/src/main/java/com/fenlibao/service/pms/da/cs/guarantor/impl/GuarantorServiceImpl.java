package com.fenlibao.service.pms.da.cs.guarantor.impl;

import com.fenlibao.dao.pms.da.cs.account.UserDetailInfoMapper;
import com.fenlibao.dao.pms.da.cs.guarantor.GuarantorMapper;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.guarantor.BankCodeInfo;
import com.fenlibao.model.pms.da.cs.guarantor.Guarantor;
import com.fenlibao.model.pms.da.cs.guarantor.form.GuarantorForm;
import com.fenlibao.p2p.model.api.exception.APIException;
import com.fenlibao.p2p.model.xinwang.enums.account.UserRole;
import com.fenlibao.p2p.model.xinwang.enums.exception.XWResponseCode;
import com.fenlibao.p2p.model.xinwang.param.account.EnterpriseRegisterRequestParams;
import com.fenlibao.p2p.model.xinwang.param.account.PersonalRegisterRequestParams;
import com.fenlibao.p2p.service.xinwang.account.XWEnterpriseRegisterService;
import com.fenlibao.p2p.service.xinwang.account.XWPersonalRegisterService;
import com.fenlibao.p2p.service.xinwang.enterprise.XWEnterpriseService;
import com.fenlibao.p2p.util.api.StringHelper;
import com.fenlibao.service.pms.da.cs.guarantor.GuarantorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GuarantorServiceImpl implements GuarantorService {

    private static final Logger LOGGER = LogManager.getLogger(GuarantorServiceImpl.class);

    @Resource
    private GuarantorMapper guarantorMapper;

    @Resource
    private UserDetailInfoMapper userDetailInfoMapper;
    @Resource
    private XWPersonalRegisterService xwPersonalRegisterService;
    @Resource
    private XWEnterpriseRegisterService xwEnterpriseRegisterService;
    @Resource
    private XWEnterpriseService xwEnterpriseService;

    private static final String XW_AUTH_LIST = "TENDER,REPAYMENT,CREDIT_ASSIGNMENT,COMPENSATORY,WITHDRAW,RECHARGE";
    private static final String XW_USER_TYPE_ORGANIZATION = "ORGANIZATION";
    private static final String XW_USER_TYPE_PERSONAL = "PERSONAL";
    private static final String OWNER_USER_TYPE_FZRR = "FZRR";
    private static final String OWNER_USER_TYPE_ZRR = "ZRR";

    @Override
    public List<Guarantor> getGuarantorList(GuarantorForm guarantorForm, RowBounds bounds) {
        return guarantorMapper.getGuarantorList(guarantorForm, bounds);
    }

    @Override
    public UserDetailInfo validAccount(String accountType, String account) {
        if (XW_USER_TYPE_ORGANIZATION.equals(accountType)) {
            accountType = OWNER_USER_TYPE_FZRR;
        } else if (XW_USER_TYPE_PERSONAL.equals(accountType)) {
            accountType = OWNER_USER_TYPE_ZRR;
        }
        return guarantorMapper.validAccount(accountType, account);
    }

    @Override
    public BussinessInfo getBussinessInfoByUserId(Integer userId, String accountType, String auditStatus) {
        return guarantorMapper.getBussinessInfoByUserId(userId, accountType, auditStatus);
    }


    @Transactional
    @Override
    public Map<String,Object> addEnterprise(BussinessInfo bussinessInfo, String redirectUrl) throws Exception{
        Map<String, Object> data = new HashMap<>();
        encodeField(bussinessInfo);
        boolean isExits = false;
        if (isExistEnterprise(bussinessInfo.getUserId())) {
            // 更新企业基本信息
            guarantorMapper.updateEnterpriseBaseInfo(bussinessInfo);
            // 更新企业联系人
            guarantorMapper.updateEnterpriseContactInfo(bussinessInfo);
            isExits = true;
        }
        // 如果是保存并提交， 則调用新网接口
        if ("AUDIT".equals(bussinessInfo.getAuditStatus())) {
            EnterpriseRegisterRequestParams params = initEnterpriseField(bussinessInfo, redirectUrl);
            data = xwEnterpriseRegisterService.getEnterpriseRegisterRequestData(params);
        } else if ("WAIT".equals(bussinessInfo.getAuditStatus()) && !isExits) {
            bussinessInfo.setBusinessNo(StringHelper.getAssetAccountName("QYZH", bussinessInfo.getUserId()));
            // 创建企业基本信息
            guarantorMapper.insertEnterpriseBaseInfo(bussinessInfo);
            // 企业联系人
            guarantorMapper.insertEnterpriseContactInfo(bussinessInfo);
        }
        return data;
    }

    private void encodeField(BussinessInfo bussinessInfo) throws Exception {
        if (bussinessInfo != null) {
            if(!StringUtils.isEmpty(bussinessInfo.getIdentification())) {
                bussinessInfo.setIdentification(StringHelper.encode(bussinessInfo.getIdentification()));
            }
            if(!StringUtils.isEmpty(bussinessInfo.getPublicAccount())) {
                bussinessInfo.setPublicAccount(StringHelper.encode(bussinessInfo.getPublicAccount()));
            }
        }
    }

    private EnterpriseRegisterRequestParams initEnterpriseField(BussinessInfo bussinessInfo, String redirectUrl) throws Exception {
        EnterpriseRegisterRequestParams params = new EnterpriseRegisterRequestParams();
        params.setUserId(bussinessInfo.getUserId());
        params.setUri(redirectUrl);
        params.setUserRole(UserRole.GUARANTEECORP.getCode());
        String bankcardNo = null;
        if (!StringUtils.isEmpty(bussinessInfo.getPublicAccount())) {
            bankcardNo = StringHelper.decode(bussinessInfo.getPublicAccount());
        }
        params.setBankcardNo(bankcardNo);
        params.setBankcode(bussinessInfo.getBankCode());
        params.setBankLicense(bussinessInfo.getBankLicenseNumber());
        params.setBusinessLicense(bussinessInfo.getBusinessLicenseNumber());
        params.setContact(bussinessInfo.getLinkman());
        params.setContactPhone(bussinessInfo.getPhone());
        params.setEnterpriseName(bussinessInfo.getBusinessName());
        params.setIdCardType(bussinessInfo.getIdentificationType());
        params.setLegal(bussinessInfo.getCorporateJurisdicalPersonalName());
        String IdCardNo = null;
        if (!StringUtils.isEmpty(bussinessInfo.getIdentification())) {
            IdCardNo = StringHelper.decode(bussinessInfo.getIdentification());
        }
        params.setLegalIdCardNo(IdCardNo);
        params.setOrgNo(bussinessInfo.getOrganizationCode());
        params.setTaxNo(bussinessInfo.getTaxID());
        params.setUnifiedCode(bussinessInfo.getUniformSocialCreditCode());

        return params;
    }

    /**
     * 根据用户id 判断该用户是否已保存企业信息
     * @param userId
     * @return
     */
    private Boolean isExistEnterprise(Integer userId) {
        return guarantorMapper.isExitsEnterprise(userId);
    }

    @Override
    public Map<String, Object> submitPersonal(String userId, String redirectUrl) throws Exception{
        PersonalRegisterRequestParams params = new PersonalRegisterRequestParams();
        try {
            UserDetail userDetail = userDetailInfoMapper.getUserDetailByUserId(userId);
            params.setUserId(userDetail.getUserId());
            params.setUserRole(UserRole.GUARANTEECORP.getCode());
            params.setUri(redirectUrl);
            params.setAuthList(XW_AUTH_LIST);
            params.setRealName(userDetail.getName());
            params.setIdCardNo(userDetail.getIdCard());
            return xwPersonalRegisterService.getPersonalRegisterRequestData(params);
        }catch (Exception e) {
            e.printStackTrace();
            throw new APIException(XWResponseCode.COMMON_PARAM_WRONG);
        }
    }

    @Override
    public List<BankCodeInfo> getBankCodes() {
        return guarantorMapper.getBankCodes();
    }


    @Override
    public Map<String, Object> getXwUpdateEnterpriseRequestData(String userId, String redirectUrl) throws Exception {
        Map<String, Object> data;
        Integer enpId = Integer.valueOf(userId);
        data = xwEnterpriseService.getModifyInfo(enpId, UserRole.GUARANTEECORP, redirectUrl);
        return data;
    }

    @Override
    @Transactional
    public void updateAuditStatusToAudit(String platformUserNo) {
        String userId = platformUserNo.replaceAll("\\D+", "");
        guarantorMapper.updateXWAuditStatusToAudit(platformUserNo);
        guarantorMapper.updateT61AuditStatusToAudit(userId);
    }

    @Override
    public String validAccountIsRegXw(String accountType, Integer userId) {
        return guarantorMapper.validAccountIsRegXw(accountType, userId);
    }
}
