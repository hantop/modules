package com.fenlibao.model.pms.common.global;

/**
 * <pre>
//前缀表示消息的权重（1=给开发者，2=用户弱提示，3=用户强提示）
 //定义状态码之前先确认相应的状态有没有
错误码前缀	       错误码描述	                     错误码
 COMMON_	        公用错误码	                     10xxx
 USER_	           用户相关错误码	                 11xxx
 TRADE_	           交易相关错误码	                 12xxx
 WEIXIN_            微信公众号相关错误码            13xxx
 ACTIVITY_          活动相关错误码	                 14xxx
 BID_               标相关错误码                   2xxxx
 ZQZR_	           债权转让相关错误码              23xxx
 ORDER_             订单相关错误码		             30xxx
 MP_                积分系统相关错误码	             31xxx
 PAYMENT_           支付模块			             5xxxx
 DM_                存管模块                       6xxxx

 OTHER_             其他                          99xxx

 NOTE: 1 10 x xx
 1  开发者看
 10 为大类型，如common
 x  为子类型，如1为不合法参数。。   2.为缺少参数。。  3.
 xx 为错误码，如01、02
 * </pre>
 * API响应信息接口
 * trade/user等module的响应信息实现该接口
 * (为了和现有的统一这里code使用String)
 * Created by zcai on 2016/10/21.
 */
public interface IResponseMessage {

    String getCode();
    String getMessage();

}
