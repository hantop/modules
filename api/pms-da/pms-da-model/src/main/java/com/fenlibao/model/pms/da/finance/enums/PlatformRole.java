package com.fenlibao.model.pms.da.finance.enums;

/**
 * Created by Administrator on 2017/6/28.
 *
 */
public enum PlatformRole {
        PLATFORM_COMPENSATORY("SYS_GENERATE_001","PLATFORM_COMPENSATORY"),//平台代偿账户
        PLATFORM_MARKETING("SYS_GENERATE_002","PLATFORM_MARKETING"),//平台营销款账户
        PLATFORM_PROFIT("SYS_GENERATE_003","PLATFORM_PROFIT"),//平台分润账户
        PLATFORM_INCOME("SYS_GENERATE_004","PLATFORM_INCOME"),//平台收入账户
        PLATFORM_INTEREST("SYS_GENERATE_005","PLATFORM_INTEREST"),//平台派息账户
        PLATFORM_ALTERNATIVE_RECHARGE("SYS_GENERATE_006","PLATFORM_ALTERNATIVE_RECHARGE"),//平台代充值账户
        PLATFORM_FUNDS_TRANSFER("SYS_GENERATE_000","PLATFORM_FUNDS_TRANSFER");//平台总账户

        protected final String no;
        protected final String role;

        PlatformRole(String no,String role) {
            this.no = no;
            this.role = role;
        }

        public String getNo() {
            return no;
        }

        public String getNole() {
            return role;
        }

        public static PlatformRole parse(String role) throws Exception{
            PlatformRole result=null;
            for(PlatformRole item:PlatformRole.values()){
                if(item.getNole().equals(role)){
                    result=item;
                }
            }
            return result;
        }
    }
