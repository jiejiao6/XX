package Xposed;

import com.param.bean.ParamEntity;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

public class SystemPropertiesHook extends XC_MethodHook {
	ParamEntity paramEntity;
	public SystemPropertiesHook(ParamEntity paramEntity)
    {
        super();
        this.paramEntity = paramEntity;
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable
    {
        XSharedPreferences pre = new XSharedPreferences(this.getClass().getPackage().getName(), "deviceInfo");
        String methodName = param.method.getName();
        if (methodName.startsWith("get"))
        {
//            Logger.h("getDeviceInfo"+"hook systemProperties ------>"+param.args[0]+"   "+param.getResult());
            XposedHelpers.setStaticObjectField(android.os.Build.class, "MODEL", pre.getString("model", paramEntity.getBuild_model()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "MANUFACTURER", pre.getString("manufacturer", paramEntity.getBuild_manufacture()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "BRAND", pre.getString("brand", paramEntity.getBuild_brand()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "HARDWARE", pre.getString("hardware", paramEntity.getBuild_hardware()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "RADIO", pre.getString("radio", paramEntity.getBuild_radioversion()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "DEVICE", pre.getString("device", paramEntity.getBuild_device()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "PRODUCT", pre.getString("product", paramEntity.getBuild_product()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "BOARD", pre.getString("board", paramEntity.getBuild_board()));
            
            XposedHelpers.setStaticObjectField(android.os.Build.class, "DISPLAY", pre.getString("display", paramEntity.getBuild_display()));
            
            XposedHelpers.setStaticObjectField(android.os.Build.class, "HOST", pre.getString("host", paramEntity.getBuild_host()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "HARDWARE", pre.getString("hardware", paramEntity.getBuild_hardware()));
            XposedHelpers.setStaticObjectField(android.os.Build.class, "SERIAL", pre.getString("serial", paramEntity.getBuild_serial()));

        }
    }
}
