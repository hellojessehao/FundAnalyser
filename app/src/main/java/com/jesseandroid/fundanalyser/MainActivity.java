package com.jesseandroid.fundanalyser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.jesseandroid.fundanalyser.constants.Constant;
import com.jesseandroid.fundanalyser.network.INetResult;
import com.jesseandroid.fundanalyser.network.NetThread;

import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.tv_content)
    TextView tv_content;
    /**
     * 需要授权的权限数组
     */
    private String[] permissions = new String[]{Manifest.permission_group.STORAGE,Manifest.permission.INTERNET};
    /**
     * 权限请求码
     */
    private final int PERMISSION_REQUEST_CODE = 0x01;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermissions(permissions,PERMISSION_REQUEST_CODE);
    }

    @OnClick(R.id.tv_start) void startCatch(){
        NetThread.getInstance()
                .setUrl(Constant.MAIN_URL)
                .addResultListener(new INetResult<Document>() {
                    @Override
                    public void onSuccess(Document result) {
                        tv_start.setText("catch success");
                        tv_content.setText(result.text());
                    }

                    @Override
                    public void onFailed(String msg) {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                })
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE){
            for (String permission : permissions){
                if(ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED){
//                    ToastUtils.getInstance(this).show(R.string.permission_deny_hint, Toast.LENGTH_LONG);
//                    mHandler.sendEmptyMessageDelayed(0,3*1000);
                    return;
                }
            }
        }
    }
}