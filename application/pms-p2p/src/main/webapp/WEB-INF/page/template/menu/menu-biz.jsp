<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>业务管理</h2>
        <ul>
            <shiro:hasPermission name="loanApplication:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/biz/loanapplication">借款申请管理</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="loanOverview:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/biz/loanOverview">借款总览</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="plan:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/biz/plan">计划</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="loans:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/biz/plan/loans">计划标的</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="loanmanage:view">
            <li>
                <span class="icon icon-group"></span>
                <a href="${path}/biz/loanmanage">发标管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="loanList:view">
            <li>
                <span class="icon icon-group"></span>
                <a href="${path}/biz/loanList/loanList-index">放款管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="repayment:view">
            <li>
                <span class="icon icon-group"></span>
                <a href="${path}/biz/repayment">还款管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="errorRepayment:view">
            <li>
                <span class="icon icon-group"></span>
                <a href="${path}/biz/errorRepayment">异常还款管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="errorManagement:view">
            <li>
                <span class="icon icon-group"></span>
                <a href="${path}/biz/errorManagement">异常管理</a>
            </li>
            </shiro:hasPermission>


            <shiro:hasPermission name="aferLoan:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/biz/afterLoan/aferLoanList">贷后管理</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="interestManagement:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/biz/interestManagement">利息管理费设置</a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>
