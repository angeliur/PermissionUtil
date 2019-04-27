package permission.com.angeliur.permissionutil;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CALL_PHONE_REQUEST_CODE = 0x0001;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCallPhone();
            }
        });
    }

    private void requestCallPhone() {
        PermissionHelper.requestPermission(this,CALL_PHONE_REQUEST_CODE,new String[]{Manifest.permission.CALL_PHONE});
    }

    @PermissionSucceed(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:10086");
        intent.setData(uri);
        startActivity(intent);
    }

    @PermissionFaild(requestCode = CALL_PHONE_REQUEST_CODE)
    private void callPhoneFail() {
        //授权失败提示用户
        Toast.makeText(this,getString(R.string.permission_denied),Toast.LENGTH_SHORT).show();
    }

    //获取用户的反馈 是否授予了权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.requestPermissionsResult(this,requestCode,permissions);
    }
}
