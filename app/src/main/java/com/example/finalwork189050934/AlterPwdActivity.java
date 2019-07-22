package com.example.finalwork189050934;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class AlterPwdActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SQLiteDatabase db;

    private EditText et_newPwd;
    private EditText et_confirm;
    private Button bt_submit;
    private Button bt_cancel;

    private String account;
    private String newPwd;
    private String confirm;

    private String productName;
    private float money;
    private String time;
    private String type;
    private String mark;

    private float income;
    private float expend;

    private HashMap<String,Object> income_map;
    private HashMap<String,Object> expend_map;

    private ArrayList<HashMap<String,Object>> incomeList;
    private ArrayList<HashMap<String,Object>> expendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_pwd);

        initDatabase();

        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        account = sp.getString("account","");

        et_newPwd = findViewById(R.id.et_newPwd);
        et_confirm = findViewById(R.id.et_confirm);
        bt_submit = findViewById(R.id.bt_submit);
        bt_cancel = findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPwd = et_newPwd.getText().toString();
                confirm = et_confirm.getText().toString();

                if(!newPwd.equals(confirm)){
                    Toast.makeText(AlterPwdActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                    //延时显示
                    new Handler().postDelayed(new Runnable(){
                        public void run(){
                            Toast.makeText(AlterPwdActivity.this,"请重新输入",Toast.LENGTH_LONG).show();
                        }
                    },1000);
                }else{
                    ContentValues values = new ContentValues();
                    values.put("password",newPwd);
                    db.update("user",values,"account=?",new String[]{account});

                    getData();

                    Intent intent = new Intent(AlterPwdActivity.this,MainActivity.class);
                    intent.putExtra("expendList",expendList);
                    intent.putExtra("incomeList",incomeList);
                    intent.putExtra("income","总收入:"+income);
                    intent.putExtra("expend","总支出:"+expend);
                    Toast.makeText(AlterPwdActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    startActivity(intent);

                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterPwdActivity.this,MainActivity.class);
                sp = getSharedPreferences("data", Context.MODE_PRIVATE);
                account = sp.getString("account","");
                getData();
                intent.putExtra("expendList",expendList);
                intent.putExtra("incomeList",incomeList);
                intent.putExtra("income","总收入:"+income);
                intent.putExtra("expend","总支出:"+expend);
                Toast.makeText(AlterPwdActivity.this,"取消修改密码",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

    }

    private void initDatabase(){
        MyDbHelper helper = new MyDbHelper(this,"demo.db",null,1);
        db=helper.getWritableDatabase();
    }

    private void getData(){
        expendList = new ArrayList<HashMap<String,Object>>();
        incomeList = new ArrayList<HashMap<String, Object>>();
        Cursor cursor = db.query("expend",new String[]{"productName","money","time","type","mark"},"account="+account,
                null,null,null,null);
        while(cursor.moveToNext()){
            expend_map = new HashMap<String,Object>();
            productName = cursor.getString(cursor.getColumnIndex("productName"));
            money = cursor.getFloat(cursor.getColumnIndex("money"));
            time = cursor.getString(cursor.getColumnIndex("time"));
            type = cursor.getString(cursor.getColumnIndex("type"));
            mark = cursor.getString(cursor.getColumnIndex("mark"));

            expend+=money;

            expend_map.put("productName",productName);
            expend_map.put("money","-"+money);
            expend_map.put("time",time);
            expend_map.put("type",type.equals("cash")?"现金":"信用卡");
            expend_map.put("mark",mark);

            expendList.add(expend_map);
        }

        Cursor cursor1 = db.query("income",new String[]{"productName","money","time","type","mark"},"account="+account,
                null,null,null,null);
        while(cursor1.moveToNext()){
            income_map = new HashMap<String,Object>();
            productName = cursor1.getString(cursor1.getColumnIndex("productName"));
            money = cursor1.getFloat(cursor1.getColumnIndex("money"));
            time = cursor1.getString(cursor1.getColumnIndex("time"));
            type = cursor1.getString(cursor1.getColumnIndex("type"));
            mark = cursor1.getString(cursor1.getColumnIndex("mark"));

            income+=money;

            income_map.put("productName",productName);
            income_map.put("money","+"+money);
            income_map.put("time",time);
            income_map.put("type",type.equals("cash")?"现金":"信用卡");
            income_map.put("mark",mark);

            incomeList.add(income_map);
        }
    }
}
