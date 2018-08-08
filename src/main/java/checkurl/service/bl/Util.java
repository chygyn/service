package checkurl.service.bl;

import checkurl.service.entity.Config;
import checkurl.service.entity.Texts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    public static boolean checkConfig(Config conf) {
        if (conf.getTimer() > 0 && conf.getURL().size() > 0) {
           //use for delete wrong url without http
            /* for (String url :conf.getURL()){
                if (!url.contains("http")) {
                    conf.getURL().remove(url);
                }
            }*/
            return true;
        } else {
            return false;
        }
    }
    public static String checkURL(String URL) throws IOException {
        /*
        if (!URL.contains("http"))
            return "bad url";*/
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            try {
                int responseCode = connection.getResponseCode();
                return String.valueOf(responseCode);
            } catch (java.net.UnknownHostException e) {
                return Texts.url_unreachable;
            }
        }catch(Exception e){
            return Texts.bad_Url;
        }
    }
}
