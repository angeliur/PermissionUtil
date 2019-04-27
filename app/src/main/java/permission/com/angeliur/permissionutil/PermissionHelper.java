package permission.com.angeliur.permissionutil;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * @author Administrator
 * @project PermissionUtil
 * @class name：permission.com.angeliur.permissionutil
 * @describe ${describe}
 * @time 2019/4/23 0023 21:27
 */


public class PermissionHelper {
    // 传的参数
    private Object mObject;  // Object: Fragment or Activity
    private int mRequestCode;  //int 请求码
    private String[] mRequestPermission; //需要请求的权限  string[]

    public PermissionHelper(Object mObject) {
        this.mObject = mObject;
    }

    public static void requestPermission(Activity activity,int requestCode,String[] permission){
       PermissionHelper.with(activity).requestCode(requestCode).requestPermission(permission).request();
    }



    public static void requestPermission(Fragment fragment, int requestCode, String[] permission){
        PermissionHelper.with(fragment).requestCode(requestCode).requestPermission(permission).request();
    }


    //添加请求权限
    private PermissionHelper requestPermission(String[] permission) {
        this.mRequestPermission = permission;
        return this;
    }

    //添加请求码
    private PermissionHelper requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    //链式的方式传Activity
    public static PermissionHelper with(Activity activity) {
        return new PermissionHelper(activity);
    }

    //链式的方式传Fragment
    public static PermissionHelper with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    //真正判断和发起请求权限的方法
    private void request() {
        //判断当前的版本是不是6.0 及以上
        if (!PermissionUtils.isOverMarshmallow()){
            //如果不是6.0以上  那么直接执行方法   反射获取执行方法
            // 执行什么方法并不确定 那么我们只能采用注解的方式给方法打一个标记，
            // 然后通过反射去执行。  注解 + 反射  执行Activity里面的callPhone
            PermissionUtils.executeSucceedMethod(mObject,mRequestCode);
            return;
        }

        //如果是6.0以上  那么首先需要判断权限是否授予
        //需要申请的权限中 获取没有授予过得权限
        ArrayList<String> deniedPermissions = PermissionUtils.getDeniedPermissions(mObject, mRequestPermission);
        if (deniedPermissions.size() == 0){
            //所有权限都授予了
            PermissionUtils.executeSucceedMethod(mObject,mRequestCode);
        }else {
            //没有授权则申请权限
            ActivityCompat.requestPermissions(PermissionUtils.getActivity(mObject),deniedPermissions.toArray(new String[deniedPermissions.size()]),mRequestCode);
        }
    }

    //处理申请权限的回调
    public static void requestPermissionsResult(Object object,int requestCode,String[] permissions){
        //获取没有授予的权限
        ArrayList<String> deniedPermissions = PermissionUtils.getDeniedPermissions(object, permissions);
        if (deniedPermissions.size() == 0){
            //所有权限都授予了
            PermissionUtils.executeSucceedMethod(object,requestCode);
        }else {
            //申请的权限中 有用户拒绝的
            PermissionUtils.executeFaildMethod(object,requestCode);
        }
    }
}
