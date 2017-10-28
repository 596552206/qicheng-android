package me.milechen.qicheng.Utils.Net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import me.milechen.qicheng.Beans.GParaBean;
import me.milechen.qicheng.Beans.GroupBean;
import me.milechen.qicheng.Beans.ParaBean;
import me.milechen.qicheng.Beans.SpeechBean;
import me.milechen.qicheng.Beans.TagBean;
import me.milechen.qicheng.Beans.TaleBean;
import me.milechen.qicheng.Beans.UserBean;

/**
 * Created by mile on 2017/7/9.
 */
public class ResponseUtil{


    /*
    获取故事列表返回
     */
    public static ResponseBean<List<TaleBean>> decodeResponseWithTaleBeanData(String json){
        if(ResponseCoper.isJson(json)) {
            return new Gson().fromJson(json, new TypeToken<ResponseBean<List<TaleBean>>>() {
            }.getType());
        }else {
            //new ResponseCoper().cope(json);
            return ResponseCoper.getIllegalFormResponseBean();
        }

    }

    /*
    获取标签列表返回
     */
    public static ResponseBean<List<TagBean>> decodeResponseWithTagBeanData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<List<TagBean>>>(){}.getType());
    }

    /*
    获取无数据返回
     */
    public static ResponseBean decodeResponseWithoutData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean>(){}.getType());

    }

    /*
    获取用户信息返回
     */
    public static ResponseBean<UserBean> decodeResponseWithUserData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<UserBean>>(){}.getType());

    }

    /*
    获取用户列表返回
     */
    public static ResponseBean<List<UserBean>> decodeResponseWithUserListData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<List<UserBean>>>(){}.getType());

    }

    /*
    获取故事信息返回
     */
    public static ResponseBean<TaleBean> decodeResponseWithCertainTaleData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<TaleBean>>(){}.getType());
    }

    /*
    获取字符串数据返回
     */
    public static ResponseBean<String> decodeResponseWithStringData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<String>>(){}.getType());

    }

    /*
    获取对话列表返回
     */
    public static ResponseBean<List<SpeechBean>> decodeResponseWithSpeechBeanListData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<List<SpeechBean>>>(){}.getType());
    }

    /*
    获取段落列表返回
     */
    public static ResponseBean<List<ParaBean>> decodeResponseWithParaBeanData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<List<ParaBean>>>(){}.getType());
    }

    /*
    获取小组列表返回
     */
    public static ResponseBean<List<GroupBean>> decodeResponseWithGroupBeanListData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<List<GroupBean>>>(){}.getType());
    }

    /*
    获取单个小组返回
     */
    public static ResponseBean<GroupBean> decodeResponseWithCertainGroupData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<GroupBean>>(){}.getType());
    }

    /*
    获取小组段落列表返回
     */
    public static ResponseBean<List<GParaBean>> decodeResponseWithGroupParaBeanListData(String json){
        return new Gson().fromJson(json,new TypeToken<ResponseBean<List<GParaBean>>>(){}.getType());
    }



}
class ResponseCoper{
    //*****************合法性判段******
    public static boolean isJson(String s){
        if(s.matches("[\\{|\\[].+[\\}|\\]]")){
            return true;
        }else {
            return false;
        }
    }

    public void cope(String s,Context context){
        if(s.matches("This site requires Javascript to work")){
            //copeByetHost(context);
        }
    }

    public static ResponseBean getIllegalFormResponseBean(){
        ResponseBean responseBean = new ResponseBean();
        responseBean.status = 333;
        responseBean.detail = "非法格式";
        return responseBean;
    }
}



