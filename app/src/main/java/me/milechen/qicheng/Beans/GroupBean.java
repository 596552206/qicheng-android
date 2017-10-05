package me.milechen.qicheng.Beans;

/**
 * Created by mile on 2017/7/30.
 */
public class GroupBean {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if(this.name == null){
            return String.valueOf(this.id);
        }else {
            return this.name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getParanumber() {
        return paranumber;
    }

    public void setParanumber(int paranumber) {
        this.paranumber = paranumber;
    }

    public String getParaone() {
        return paraone;
    }

    public void setParaone(String paraone) {
        this.paraone = paraone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public int id;
    public String name;
    public int time;
    public String password;
    public int member;
    public int paranumber;
    public String paraone;

}
