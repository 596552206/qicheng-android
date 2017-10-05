package me.milechen.qicheng.Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.milechen.qicheng.Utils.TimeUtil;

/**
 * Created by mile on 2017/7/8.
 */
public class TaleBean implements Serializable
{
    public int id;
    public int sponsorid;
    public String sponsornick;
    public int time;
    public int paranumber;
    public String paraone;
    public int zan;
    public List<TagBean> tagset;
    public String sponsoravatar;

    public String getSponsoravatar() {
        return sponsoravatar;
    }

    public void setSponsoravatar(String sponsoravatar) {
        this.sponsoravatar = sponsoravatar;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSponsorId() {
        return sponsorid;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorid = sponsorId;
    }

    public String getSponsorNick() {
        return sponsornick;
    }

    public void setSponsorNick(String spnsorNick) {
        this.sponsornick = spnsorNick;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getParaNumber() {
        return paranumber;
    }

    public void setParaNumber(int paraNumber) {
        this.paranumber = paraNumber;
    }

    public String getParaOne() {
        return paraone;
    }

    public void setParaOne(String paraOne) {
        this.paraone = paraOne;
    }

    public List<TagBean> getTagSet() {
        return tagset;
    }

    public void setTagSet(List<TagBean> tagSet) {
        this.tagset = tagSet;
    }

    public String getTagString(){
        ArrayList<String> list = new ArrayList<String>();
        for (TagBean tag:tagset) {
            list.add(tag.getName());
        }
        return list.toString().replaceAll("\\[|\\]","");
    }

    public String getSmartTime(){
        return TimeUtil.generateSmartTime(TimeUtil.formatTimestamp2JavaForm(this.getTime()+""));
    }
}
