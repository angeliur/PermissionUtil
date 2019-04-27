package permission.com.angeliur.permissionutil;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Administrator
 * @project PermissionUtil
 * @class name：permission.com.angeliur.permissionutil
 * @describe ${describe}
 * @time 2019/4/23 0023 22:05
 */


public class PermissionUtils {

    //这个类里面的所有方法都是静态方法，所以不能让别人去new对象
    private PermissionUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //判断当前版本号是否是6.0及以上版本
    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //执行失败的方法
    public static void executeFaildMethod(Object object, int requestCode) {
        Method[] methods = object.getClass().getDeclaredMethods();

        // 遍历找我们打了标记的方法
        for (Method method : methods) {
            // 获取该方法上面有没有打这个失败的标记
            PermissionFaild faild = method.getAnnotation(PermissionFaild.class);
            if (faild != null){
                // 代表该方法打了标记
                // 并且我们的请求码必须和requestCode 一样
                int methodCode = faild.requestCode();
                if (methodCode == requestCode){
                    // 这个就是我们要找的成功方法
                    // 反射执行该方法
                    executeMethod(object,method);
                }
            }
        }
    }

    //执行成功的方法
    public static void executeSucceedMethod(Object object, int mRequestCode) {
        // 获取class中所有的方法
        Method[] methods = object.getClass().getDeclaredMethods();

        // 遍历找我们打了标记的方法
        for (Method method : methods) {
            // 获取该方法上面有没有打这个成功的标记
            PermissionSucceed succeed = method.getAnnotation(PermissionSucceed.class);
            if (succeed != null){
                // 代表该方法打了标记
                // 并且我们的请求码必须和requestCode 一样
                int methodCode = succeed.requestCode();
                if (methodCode == mRequestCode){
                    // 这个就是我们要找的成功方法
                    // 反射执行该方法
                    executeMethod(object,method);
                }
            }
        }
    }

    //反射执行
    private static void executeMethod(Object object, Method method) {
        // 反射执行方法  第一个是传该方法是属于哪个类   第二个参数是反射方法的参数
        try {
            method.setAccessible(true);// 允许执行私有方法
            method.invoke(object,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //获取没有授予的权限
    public static ArrayList<String> getDeniedPermissions(Object object, String[] requestPermission) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (String permission : requestPermission) {
            if (ActivityCompat.checkSelfPermission(getActivity(object), permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    public static Activity getActivity(Object object) {
        if (object instanceof Activity){
            return (Activity) object;
        }

        if (object instanceof Fragment){
            return ((Fragment)object).getActivity();
        }

        return null;
    }


}
