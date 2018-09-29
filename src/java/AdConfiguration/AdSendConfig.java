package AdConfiguration;


import ZLYUtils.Network;
import ZLYUtils.NetworkHeaders;
import sun.dc.pr.PRError;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdSendConfig {
    public static final String SEL_APPNAME = "sel_appname";
    public static final String SEL_VERSION = "sel_version";
    public static final String VERSION = "version";
    public static final String SEL_CHANNELID = "sel_channelid";
    public static final String CHANNELID = "channelid";
    public static final String SEL_AD_NO = "sel_advNo";
    public static final String SEL_STATUS = "sel_status";
    public static final String STATUS = "status";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String APPNAME = "appname";
    public static final String AD_NO = "adNo";
    public static final String ADV_SING_NO = "advSingNo";
    public static final String RDO_SINGLE = "rdo_single";
    public static final String RDO_TOTAL = "rdo_total";
    public static final String RES_START_DATE = "relStartDate";
    public static final String REL_END_DATE = "relEndDate";
    public static final String IS_CIRCLEAD = "iscirclead";
    public static final String QZ = "qz";
    public static final String TK_TIME = "tktime";
    public static final String CYCP_NUM = "cycpnum";
    public static final String LB_TIME = "lbtime";
    public static final String SXDIS_NUM = "sxdisnum";
    public static final String IS_SPECIAL = "isSpecial";
    public static final String WIFI_STATE = "wifiState";
    public static final String SB = "sb";
    public static final String ENCODER = "UTF-8";
    public static final String SB_JBUTTON = "提交";
    public static final String MFDZS = "免费电子书";
    public static final String MFZS = "免费追书";
    public static final int LFET_MARGIN = 5;//左边界
    public static final String QZ_HINT = "数字类型,倒序,0为分量,相同非0权重按照加入时间排序,默认10";
    public static final String HOST_TEST = "http://192.168.1.242:9037";
    public static final String GET_APP_TYPE_PATH = "/fm/getAdOptionAppName";
    public static final String ADD_AD_RELEASE = "/fm/addadrelease";
    public static Map<String, String> HEADERS;
    public static final String ZHI_TOU = "直投(请在广告类型中选取直投广告)";
    public static final String SINGLE_CLICK_NUM = "singleClickNum";
    public static final String DAY_TOTAL_CLICK_NUM = "dayTotalClickNum";
    public static final String TOTAL_CLICK_NUM = "totalClickNum";
    public static final String TOTAL_EXPOSURE_NUM = "totalExposureNum";
    public static final String DAY_TOTAL_EXPOSURE_NUM = "dayTotalExposureNum";
    public static final String SINGLE_EXPOSURE_NUM = "singleExposureNum";
    public static final String DATABASE_HOST = "192.168.1.246:3306/freezwsc";
    public static final String DATABASE_USERNAME = "root_rw";
    public static final String DATABASE_PASSWORD = "loto5522";
    public static final String DATABASE_AD_TABLE_NAME = "freeadrelease";
    private static final String GDTXXL = "广点通信息流Banner";
    public static final String[] AD_ANNOTATION = new String[]{
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
        if (MFDZS.equals(appName)) {
            return new String[]{"广点通", "头条SDK", "今日头条TT_API", "头条TT_API_02"
                    , "头条TT_API_03", "聚效", GDTXXL, ZHI_TOU,
                    "测试公告文字"};
        } else {
            return new String[]{"广点通", "头条SDK", "今日头条TT_API", GDTXXL
                    , ZHI_TOU, "测试公告文字"};
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
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 30, 31, 32, 37, 39, 40, 45, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64};
                    break;
                case "头条SDK":
                    s = new int[]{1, 30, 31,};
                    break;
                case "今日头条TT_API":
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 27, 30, 31, 40, 55, 56, 62, 63, 64};
                    break;
                case "头条TT_API_02":
                    s = new int[]{30, 31};
                    break;
                case "头条TT_API_03":
                    s = new int[]{30, 31};
                    break;
                case "聚效":
                    s = new int[]{26, 31, 32, 37, 39, 43, 46};
                    break;
                case GDTXXL:
                    s = new int[]{3, 6, 10, 14, 17, 27, 30, 31, 40, 55, 56};
                    break;
                case ZHI_TOU:
                    s = new int[]{1, 2, 3, 6, 10, 14, 17, 26, 27, 30, 31, 32, 37, 39, 40, 43, 45, 46, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 69, 70, 71};
                    break;
                case "测试公告文字":
                    s = new int[]{65, 66, 67};
                    break;
            }
        } else if (MFZS.equals(appName)) {

            switch (appType) {
                case "广点通":
                    s = new int[]{1, 3, 6, 10, 14, 17, 26, 30, 31, 32, 37, 39, 45, 56, 57, 58, 59, 60, 61, 62, 63, 64};
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
