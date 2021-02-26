package com.jesseandroid.fundanalyser;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.widget.TextView;

import com.jesseandroid.fundanalyser.network.NetThread;
import com.jesseandroid.fundanalyser.utils.CatchUtils;
import com.jesseandroid.fundanalyser.utils.LogUtils;

import org.jsoup.nodes.Document;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.tv_content)
    TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_start) void startCatch(){
        tv_start.setText("Start catching...");
        Document document = CatchUtils.getInstance().catchDocument("https://fund.eastmoney.com/data/");
        if(document != null){
            tv_content.setText(document.text());
        }else{
            LogUtils.e("get document null!");
        }
    }

}