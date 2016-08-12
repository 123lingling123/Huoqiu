package com.lsl.huoqiu.net.systerm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.lsl.huoqiu.net.listener.NetWorkListener;

/**
 * 网络连接状态信息
 * uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"
 * Created by Forrest on 16/7/13.
 */
public class NetWorkAdapter implements NetWorkListener {
    private NetworkInfo mNetWorkInfo;

    /**
     * 是否连接网络
     * @return
     */
    public boolean isConnectedNetwork() {
        if(mNetWorkInfo != null){
            return mNetWorkInfo.isConnected();
        }else{
            return false;
        }

    }
    /**
     * 初始化，得到网络连接状态
     * @param context
     */
    public NetWorkAdapter(final Context context){
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetWorkInfo=manager.getActiveNetworkInfo();
    }

    /**
     *  判断连接的网络是否是WIFI
     * @return
     */
    public boolean isConnectedWiFi(){
        return isConnectedNetwork()&&mNetWorkInfo.getType()==ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断连接的网络是否是移动网络
     * @return
     */
    public boolean isConnectedMobileNet(){
        return isConnectedNetwork()&&mNetWorkInfo.getType()==ConnectivityManager.TYPE_MOBILE;
    }

    @Override
    public void onWifiConnected(NetworkInfo networkInfo) {
        this.mNetWorkInfo=networkInfo;
    }

    @Override
    public void onMobileConnected(NetworkInfo networkInfo) {
        this.mNetWorkInfo=networkInfo;
    }

    @Override
    public void onDisconnected(NetworkInfo networkInfo) {
        this.mNetWorkInfo=networkInfo;
    }

    /**
     * 检查连接的网络网速的快慢
     * @return 如果网络连接块就返回true，网络连接慢就返回false
     */
    public boolean isConnectionFast(){
        //precheck is connected
        if(mNetWorkInfo==null || !mNetWorkInfo.isConnected()) return false;
        int type=mNetWorkInfo.getType();
        int subType=mNetWorkInfo.getSubtype();
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
           /* // NOT AVAILABLE YET IN API LEVEL 7
            case Connectivity.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case Connectivity.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case Connectivity.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case Connectivity.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case Connectivity.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            // Unknown
*/            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return false;
                default:
                    return false;
            }
        }else{
            return false;
        }
    }
}
