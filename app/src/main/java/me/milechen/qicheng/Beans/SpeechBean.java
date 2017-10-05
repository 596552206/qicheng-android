package me.milechen.qicheng.Beans;

/**
 * Created by mile on 2017/8/6.
 */
public class SpeechBean {
    public int userid;
    public String nick;
    public String content;
    public int time;
    public int atpara;
    public int atuser;

    public String getAtusernick() {
        return atusernick;
    }

    public void setAtusernick(String atusernick) {
        this.atusernick = atusernick;
    }

    public String atusernick;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public void setTime(int time) {
        this.time = time;
    }

    public int getAtpara() {
        return atpara;
    }

    public void setAtpara(int atpara) {
        this.atpara = atpara;
    }

    public int getAtuser() {
        return atuser;
    }

    public void setAtuser(int atuser) {
        this.atuser = atuser;
    }
}
