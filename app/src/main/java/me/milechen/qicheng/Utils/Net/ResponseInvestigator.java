package me.milechen.qicheng.Utils.Net;

/**
 * Created by mile on 2017/7/12.
 */
public abstract class ResponseInvestigator<T>{

    public abstract void onOK(int status,String detail,T data);

    public abstract void onErr(int status,String detail);


}