<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@page language="java" pageEncoding="utf-8" %>
<link rel="stylesheet" type="text/css" href="/static/theme/body.css"/>
<script type="text/javascript" src="/static/script/index/body.js"></script>
<shiro:hasPermission name="index:view">
    <div class="container">
        <div id="total_div">
            <div class="title" id="title_statistics">统计数据</div>
            <ul>
                <li>
                    <span class="stitle">平台累计注册人数 <img title="分利宝全平台全渠道自开通以来累计注册成功的人数" src="/static/image/help.png"/></span>
                    <span class="scontent">${platformstatisticsTotal.registerNum}人</span>
                    <span class="stitle">平台累计投资人数 <img title="分利宝全平台全渠道自开通以来累计投资人数总和,一人投资多次只计算一个投资人数.包含债权转入,新手标，,投资即计算。流标，债权转出不减去" src="/static/image/help.png"/></span>
                    <span class="scontent">${platformstatisticsTotal.investNum}人</span>
                </li>
                <br/>
                <li>
                    <span class="stitle">平台累计成交服务费 <img title="债权转让平台收取的费用总和以及借款人服务费的总和" src="/static/image/help.png"/></span>
                    <span class="scontent">${platformstatisticsTotal.turnoverFee}元</span>
                    <span class="stitle">平台累计待回款本金 <img title="目前处于收益中的所有待回款本金" src="/static/image/help.png"/></span>
                    <span class="scontent">${platformstatisticsTotal.receivableMoney}元</span>
                </li>
                <br/>
                <li>
                    <span class="stitle">平台累计投资金额<img title="平台累计交易金额,包含:普通标的购买金额、新手标购买金额、债权转让转入金额（转出的不减去） 按实际交易金额计算" src="/static/image/help.png"/></span>
                    <span class="scontent">${platformstatisticsTotal.investMoeny}元</span>
                    <span class="stitle">平台累计为投资者赚取收益<img title="包含:普通标的购买的到期付息及逾期罚息、体验金的收益、激活的返现红包、所发送的现金奖励、债权转入吃的差价（标的1000，转出价950，这50元要算）提前还款的违约金" src="/static/image/help.png"/></span>
                    <span class="scontent">${platformstatisticsTotal.profitMoneyForInvestor}元</span>
                </li>
            </ul>
        </div>

        <div id="statistics_div">
            <div class="title" id="select_div">
                <div id="select_div_1">选择查看日期</div>
                <div id="select_div_2">
                    <select name="historyType" id="historyType" class="form-control" onchange="getPlatformstatistics(this.value);">
                        <option value="yesterday">查看昨日数据</option>
                        <option value="lastweek">查看上周数据</option>
                        <option value="lastmonth">查看上月数据</option>
                    </select>
                </div>
            </div>
            <div>
                <ul>
                    <li>
                        <span class="stitle">新增注册人数 <img title="所选期间内全渠道全平台注册成功的人数" src="/static/image/help.png"/></span>
                        <span class="scontent" id="registerNum"></span>
                        <span class="stitle">充值金额 <img title="某时段内全渠道全平台充值的总额" src="/static/image/help.png"/></span>
                        <span class="scontent" id="rechargeMoney"></span>
                    </li>
                    <br/>
                    <li>
                        <span class="stitle">投标人数 <img title="某时段内全渠道全平台产生投资行为的人数,投资即计算,包含新手标不含体验金与债权转入。流标，债权转出后不会减去" src="/static/image/help.png"/></span>
                        <span class="scontent" id="tenderNum"></span>
                        <span class="stitle">投标金额 <img title="某时段内全渠道全平台投资的总金额,投资即计算,包含新手标,不包含体验金与债权转入。流标，债权转出后不会减去" src="/static/image/help.png"/></span>
                        <span class="scontent" id="tenderMoney"></span>
                    </li>
                    <br/>
                    <li>
                        <span class="stitle">债权转入人数 <img title="投资债权产生债权转入成功的人数" src="/static/image/help.png"/></span>
                        <span class="scontent" id="transferinNum"></span>
                        <span class="stitle">债权转入金额 <img title="购买债权转出成功的金额" src="/static/image/help.png"/></span>
                        <span class="scontent" id="transferinMoney"></span>
                    </li>
                    <br/>
                    <li>
                        <span class="stitle">首投人数 <img title="用户的首次投资，（债权转让，新手标也算），流标不重置首投状态.，就是产生第一次动作的那次都算首投，注意查询时段内去重。" src="/static/image/help.png"/></span>
                        <span class="scontent" id="firstInvestNum"></span>
                    </li>
                    <br/>
                    <li>
                        <span class="stitle">平台提现金额 <img title="某时段内执行提现成功的金额(以到账为准)" src="/static/image/help.png"/></span>
                        <span class="scontent" id="withdrawMoney"></span>
                        <span class="stitle">平台回款金额 <img title="某段时间内的回款本金+利息总额,含新手标,不含体验金" src="/static/image/help.png"/></span>
                        <span class="scontent" id="earningsMoney"></span>
                    </li>
                    <br/>
                    <li id="hideshow">
                        <span class="stitle">平台放款金额 <img title="周期内平台所有总放款的金额" src="/static/image/help.png"/></span>
                        <span class="scontent" id="loanMoney"></span>
                        <span class="stitle">平台成交服务费 <img title="周期内债权转让成功收取的服务费总和与收取借款人的手续费总和" src="/static/image/help.png"/></span>
                        <span class="scontent" id="turnoverFee"></span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</shiro:hasPermission>