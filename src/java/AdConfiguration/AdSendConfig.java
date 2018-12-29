package AdConfiguration;


import ZLYUtils.Network;
import ZLYUtils.NetworkHeaders;

import java.util.*;

class AdSendConfig {
    static final String SEL_APPNAME = "sel_appname";
    static final String SEL_VERSION = "sel_version";
    static final String VERSION = "version";
    static final String SEL_CHANNELID = "sel_channelid";
    static final String CHANNELID = "channelid";
    static final String SEL_AD_NO = "sel_advNo";
    static final String SEL_STATUS = "sel_status";
    static final String STATUS = "status";
    static final String CURRENT_PAGE = "currentPage";
    static final String APPNAME = "appname";
    static final String AD_NO = "adNo";
    static final String ADV_SING_NO = "advSingNo";
    static final String RDO_SINGLE = "rdo_single";
    static final String RDO_TOTAL = "rdo_total";
    static final String RES_START_DATE = "relStartDate";
    static final String REL_END_DATE = "relEndDate";
    static final String IS_CIRCLEAD = "iscirclead";
    static final String QZ = "qz";
    static final String TK_TIME = "tktime";
    static final String CYCP_NUM = "cycpnum";
    static final String LB_TIME = "lbtime";
    static final String SXDIS_NUM = "sxdisnum";
    static final String IS_SPECIAL = "isSpecial";
    static final String WIFI_STATE = "wifiState";
    static final String SB = "sb";
    static final String ENCODER = "UTF-8";
    static final String SB_JBUTTON = "提交";
    static final String MFDZS = "免费电子书";
    static final String MFZS = "免费追书";
    static final String IKS = "爱看书(使用免电广告类型)";
    static final int LFET_MARGIN = 5;//左边界
    static final String QZ_HINT = "数字类型,倒序,0为分量,相同非0权重按照加入时间排序,默认10";
    static final String HOST_TEST = "http://manage-cx-qa.ikanshu.cn";
    static final String GET_APP_TYPE_PATH = "/fm/getAdOptionAppName";
    static final String ADD_AD_RELEASE = "/fm/addadrelease";
    static Map<String, String> HEADERS;
    static final String ZHI_TOU = "直投(请在广告类型中选取直投广告)";
    static final String SINGLE_CLICK_NUM = "singleClickNum";
    static final String DAY_TOTAL_CLICK_NUM = "dayTotalClickNum";
    static final String TOTAL_CLICK_NUM = "totalClickNum";
    static final String TOTAL_EXPOSURE_NUM = "totalExposureNum";
    static final String DAY_TOTAL_EXPOSURE_NUM = "dayTotalExposureNum";
    static final String SINGLE_EXPOSURE_NUM = "singleExposureNum";
    static final String DATABASE_HOST = "db.miandian.qa:3306/freezwsc";
    static final String DATABASE_USERNAME = "APP_01";
    static final String DATABASE_PASSWORD = "Iwanvi@123";
    static final String DATABASE_AD_TABLE_NAME = "freeadrelease";
    private static final String GDTXXL = "广点通信息流Banner";
    public static final String[] AD_ANNOTATION = new String[]{
            "广告在应用中的位置",
            "GG-1:启动页全屏", "GG-2:书架公告", "GG-3:书架顶部通栏",
            "GG-6:精品页顶部通栏", "GG-10:排行(书库)顶部通栏",
            "GG-14:详情页顶部通栏", "GG-17:搜索顶部通栏",
            "GG-26:详情页下载弹出", "GG-27:目录页顶部通栏",
            "GG-30:阅读页底部通栏", "GG-31:阅读页插页",
            "GG-32:书架封面", "GG-37:图书详情(H5)",
            "GG-39:今日推荐(H5)", "GG-43:书架右下角悬浮",
            "GG-45:精品榜单1和2之间", "GG-46:精品右下角悬浮",
            "GG-55:赚钱顶部通栏", "GG-56:今日推荐底部通栏",
            "GG-57:精品榜单2和3之间", "GG-58:男频榜单1和2之间",
            "GG-59:男频榜单2和3之间", "GG-60:女频榜单1和2之间",
            "GG-61:女频榜单2和3之间", "GG-62:精品顶部轮播banner位置2",
            "GG-63:精品顶部轮播banner位置3", "GG-64:精品顶部轮播banner位置5",
            "GG-65:详情页公告", "GG-66:搜索公告", "GG-67:阅读页公告",
            "GG-69:精品页12榜间大banner", "GG-70:精品页23榜间大banner",
            "GG-71:精品页34榜间大banner"
    };

    /**
     * 获取内置广告位支持的广告类型
     *
     * @return
     */
    public static String[] getBuiltInAppType(String appName) {
        //更改以下名称时需要同步更改builtInAdGG()；
        switch (appName) {
            case MFDZS:
                return new String[]{"广点通", "头条SDK", "今日头条TT_API", "头条TT_API_02"
                        , "头条TT_API_03", "聚效", GDTXXL, ZHI_TOU, "猎鹰sdk",
                        "测试公告文字"};
            case MFZS:
                return new String[]{"广点通", "头条SDK", "今日头条TT_API", GDTXXL
                        , ZHI_TOU, "猎鹰sdk", "测试公告文字"};
            default:
                return new String[0];
        }
    }

    /**
     * 内置广告支持的广告位
     *
     * @return
     */
    public static List<Integer> builtInAdGG(String appName, String appType) {
        List<Integer> list = new ArrayList<>();
        int[] s = new int[]{};
        if (MFDZS.equals(appName)) {
            switch (appType) {
                case "广点通":
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 27, 30, 31, 32, 37, 39, 40, 45, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64};
                    break;
                case "头条SDK":
                    s = new int[]{1, 30, 31,};
                    break;
                case "今日头条TT_API":
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 27, 30, 31, 40, 55, 56, 62};
                    break;
                case "头条TT_API_02":
                    s = new int[]{30, 31};
                    break;
                case "头条TT_API_03":
                    s = new int[]{30, 31};
                    break;
                case "聚效":
                    s = new int[]{31, 32, 43, 46};
                    break;
                case GDTXXL:
                    s = new int[]{3, 6, 10, 14, 17, 27, 30, 40, 55, 56};
                    break;
                case ZHI_TOU:
                    s = new int[]{1, 2, 3, 6, 10, 14, 17, 26, 27, 30, 31, 32, 37, 39, 40, 43, 45, 46, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 69, 70, 71};
                    break;
                case "测试公告文字":
                    s = new int[]{65, 66, 67};
                    break;
                case "猎鹰sdk":
                    s = new int[]{1, 30, 31};
                    break;
            }
        } else if (MFZS.equals(appName)) {

            switch (appType) {
                case "广点通":
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 27, 30, 31, 32, 37, 39, 45, 56, 57, 58, 59, 60, 61, 62, 63, 64};
                    break;
                case "头条SDK":
                    s = new int[]{1, 30, 31,};
                    break;
                case "今日头条TT_API":
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 27, 31, 56};
                    break;
                case GDTXXL:
                    s = new int[]{3, 6, 10, 14, 17, 27, 30, 56};
                    break;
                case ZHI_TOU:
                    s = new int[]{1, 2, 3, 6, 10, 14, 17, 26, 27, 30, 31, 32, 37, 39, 40, 43, 45, 46, 56, 57, 58, 59, 60, 61, 62, 63, 64, 69, 70, 71};
                    break;
                case "测试公告文字":
                    s = new int[]{65, 66, 67};
                    break;
                case "猎鹰sdk":
                    s = new int[]{1, 30, 31};
                    break;
            }
        }
        for (int i : s) {
            list.add(i);
        }
        return list;
    }


    public static String getAppNameCode(String appName) {
        switch (appName) {
            case MFDZS:
                return "cxb";
            case MFZS:
                return "mfzs";
            case IKS:
                return "mfzs";
            default:
                return null;
        }
    }

    public static synchronized void loging() {
        synchronized (AdSendConfig.class) {
            HEADERS = new HashMap<>();
            NetworkHeaders networkHeaders = new NetworkHeaders();
            Network.sendPost(HOST_TEST + "/fm/login/login",
                    "username=admin&password=123456",
                    null, networkHeaders);
            String cookies = "";
            try {
                for (String s : networkHeaders.getHeaders().get("Set-Cookie")) {
                    cookies += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            HEADERS.put("Cookie", cookies);

        }
    }


}
