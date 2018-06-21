package jianqiang.com.activityhook1.ams_hook;

import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.jianqiang.mypluginlibrary.RefInvoke;

import jianqiang.com.activityhook1.UPFApplication;

class MockClass2 implements Handler.Callback {

    Handler mBase;

    public MockClass2(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {

        Log.d("baobao4321", String.valueOf(msg.what));
        switch (msg.what) {

            // ActivityThread里面 "CREATE_SERVICE" 这个字段的值是114
            // 本来使用反射的方式获取最好, 这里为了简便直接使用硬编码
            case 114:
                handleCreateService(msg);
                break;
        }

        mBase.handleMessage(msg);
        return true;
    }

    private void handleCreateService(Message msg) {
        // 这里简单起见,直接取出插件Servie

        Object obj = msg.obj;
        ServiceInfo serviceInfo = (ServiceInfo) RefInvoke.getFieldObject(obj, "info");

        String realServiceName = null;

        for (String key : UPFApplication.pluginServices.keySet()) {
            String value = UPFApplication.pluginServices.get(key);
            if(value.equals(serviceInfo.name)) {
                realServiceName = key;
                break;
            }
        }

        serviceInfo.name = realServiceName;
    }
}

