package me.milechen.qicheng.Utils.Net;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpHeaders;

/**
 * Created by mile on 2017/10/22.
 */
public class ByetCookieUtil {
    public void copeByetHost(WebView webView){
        class MyWebviewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                Log.i("ii","cookie string:"+cookieStr);
                String ch = getCookie(cookieStr);
                Log.i("ii", "Byet Cookies = " + ch);
                Log.i("ii", "Trying to set common header for Byet Cookies");
                setCh(ch);
                super.onPageFinished(view, url);
            }
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(Urls.ROOT);
        webView.setWebViewClient(new MyWebviewClient());
    }
    public String getCookie(String s){
        int b = s.indexOf("=");
        int e = s.indexOf(";");
        if(e>=0)return s.substring(b+1,e);
        return s.substring(b+1);
    }
    public void setCh(String s){
        HttpHeaders headers = new HttpHeaders();//设置公共header所有的 header 都 不支持 中文
        headers.put("Cookie", "__test="+s+"; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
        OkHttpUtils.getInstance().addCommonHeaders(headers);
    }
}
