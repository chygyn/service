package checkurl.service.entity;

import java.util.List;

public class Config {
    private List <String> URL;
    private int timer;
    private long mode;
    public Config (){}
    public Config (List<String> URL, int timer, int mode ){
        this.URL = URL;
        this.timer = timer;
        this.mode=mode;
    }

    public void setMode(long mode) {
        this.mode = mode;
    }

    public long getMode() {
        return mode;
    }

    public List<String> getURL() {
        return URL;
    }

    public int getTimer() {
        return timer;
    }

    public void setURL(List<String> URL) {
        this.URL = URL;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public String toString (){
        StringBuilder result = new StringBuilder();
        result.append("Current config:");
        result.append(" URL:");
        this.URL.forEach(u->result.append(u+", "));
        result.append(" Timer "+this.timer+" Mode: "+this.mode);
        return result.toString();
    }
}
