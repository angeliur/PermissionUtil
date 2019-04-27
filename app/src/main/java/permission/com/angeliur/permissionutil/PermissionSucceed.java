package permission.com.angeliur.permissionutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Administrator
 * @project PermissionUtil
 * @class name：permission.com.angeliur.permissionutil
 * @describe ${describe}
 * @time 2019/4/23 0023 22:21
 */

@Target({ElementType.METHOD})   //Target代表放在什么位置   ElementType.METHOD 放在方法上  ElementType.FIELD 放在属性上    ElementType.TYPE 放在类上
@Retention(RetentionPolicy.RUNTIME)  //Retention代表编译时检测还是运行时检测 RetentionPolicy.SOURCE   编译时      RetentionPolicy.RUNTIME  运行时
public @interface PermissionSucceed {
    int requestCode();
}
