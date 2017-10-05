package me.milechen.qicheng.Utils.Net;


/**
 * Created by mile on 2017/7/9.
 */
public class ResponseBean<T> {
    public int status;
    public String detail;
    public T data;

    public void investigate(ResponseInvestigator<T> responseInvestigator){
        if(status >= 300 && status<= 400){
            responseInvestigator.onErr(status,detail);
        }else {
            responseInvestigator.onOK(status,detail,data);
        }
    }


}
