package checkurl.service.entity;

public class Texts {

    public static final String config_error="";
    public static final String bad_Url="bad url";
    public static final String run_CheckUrlThread="CheckURLThread|run| Config ";
    public static final String timer_many_threads ="CheckURLThread|startCheckUrl(urls,threadCount.sizePack)|Checking urls: ";
    public static final String timer_no_threads="CheckURLThread|startCheckUrl(urls)|Checking urls: ";
    public static final String error_check_url="error while check url";
    public static final String url_unreachable="url unreachable";

    public static final String post_setConfig="Servlet /setConfig. Config: ";
    public static final String config_correct = "Config correct. Check url service was starter.";
    public static final String config_wrong ="Write config in right format: {url:['http://..'],timer:int, mode:long}. " +
            "Mode<=0 then service works with one thread, mode>=0 then service works with number of threads equals mode";

    public static final String get_getURLStatus="Servlet /getURLStatus. Result: ";
    public static final String no_config ="send config first";

}
