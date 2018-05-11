package com.fenlibao.dao.pms.da.cs;

import com.fenlibao.model.pms.da.cs.Invite;
import com.fenlibao.model.pms.da.cs.form.PageForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface InviteMapper {
   List<Invite> inviteList(PageForm pageForm,RowBounds bounds);

   /**
    * 被邀请人处于投标中和待放款的投资金额
    * @param beinvitedIds
    * @return
    */
   List<Map<String, Object>> getInvestMoney(
           @Param("beinvitedIds") List<Integer> beinvitedIds
   );

   /**
    * 被邀请人持有债权
    * @param beinvitedIds
    * @return
    */
   List<Map<String, Object>> getHolddebt(
           @Param("beinvitedIds") List<Integer> beinvitedIds
   );

   /**
    * 被邀请人已还金额
    * @param beinvitedIds
    * @return
    */
   List<Map<String, Object>> getRepayment(
           @Param("beinvitedIds") List<Integer> beinvitedIds
   );
}
