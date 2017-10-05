package me.milechen.qicheng.Beans;

import me.milechen.qicheng.Utils.TimeUtil;

/**
 * Created by mile on 2017/7/21.
 */
public class ParaBean {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getTaleid() {
        return taleid;
    }

    public void setTaleid(int taleid) {
        this.taleid = taleid;
    }

    public int getParanum() {
        return paranum;
    }

    public void setParanum(int paranum) {
        this.paranum = paranum;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public String getUsernick() {return usernick;}

    public void setUsernick(String usernick) {this.usernick = usernick;}

    public String getSmartTime(){
        return TimeUtil.generateSmartTime(TimeUtil.formatTimestamp2JavaForm(this.getTime()+""));
    }

    public int id;
    public int userid;
    public int taleid;
    public int paranum;
    public int time;
    public String content;
    public int zan;
    public String usernick;
}
