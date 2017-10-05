package me.milechen.qicheng.Beans;

import me.milechen.qicheng.Utils.TimeUtil;

/**
 * Created by mile on 2017/8/19.
 */
public class GParaBean {
    public int id;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsernick() {
        return usernick;
    }

    public void setUsernick(String usernick) {
        this.usernick = usernick;
    }

    public int getParanum() {
        return paranum;
    }

    public void setParanum(int paranum) {
        this.paranum = paranum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public String getSmartTime(){
        return TimeUtil.generateSmartTime(TimeUtil.formatTimestamp2JavaForm(this.getTime()+""));
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int userid;
    public String usernick;
    public int paranum;
    public String content;
    public int time;

}
