package me.milechen.qicheng.Beans;

/**
 * Created by mile on 2017/10/3.
 */
public class QRCodeGroupData {
    public int id =-1;

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int password =-1;
    public String generateGroupDataString(){
        if(id != -1 && password != -1){
            return id+"x"+password;
        }else {
            return null;
        }
    }
}
