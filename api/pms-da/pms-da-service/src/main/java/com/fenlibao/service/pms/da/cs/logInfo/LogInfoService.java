package com.fenlibao.service.pms.da.cs.logInfo;


import com.fenlibao.model.pms.da.cs.LogInfo;
import com.fenlibao.model.pms.da.cs.form.LogInfoForm;
import org.apache.ibatis.session.RowBounds;
import java.util.List;

public interface LogInfoService {


    List<LogInfo> getLogInfoList(LogInfoForm logInfoForm, RowBounds bounds);

    /**
     * 保存用户操作日志
     * @param loanId
     */
     void addUserLog(int loanId,int status);

}
