package com.wiseasy.webviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wiseasy.webviewdemo.commands.CommandsConstants;

public class CashPayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_pay);

        String amount = getIntent().getStringExtra("amount");
        TextView tv_amount = findViewById(R.id.tv_amount);
        tv_amount.setText(String.format("金额：%s元", amount));

        findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CommandsConstants.Action_Pay);
                intent.putExtra("result", "支付成功");
                LocalBroadcastManager.getInstance(CashPayActivity.this).sendBroadcast(intent);

                finish();
            }
        });
    }
}