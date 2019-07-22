package com.example.finalwork189050934;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.makeText;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SQLiteDatabase db;

    private HashMap<String,Object> income_map;
    private HashMap<String,Object> expend_map;
    private ArrayList<HashMap<String,Object>> incomeList;
    private ArrayList<HashMap<String,Object>> expendList;

    private EditText et_account;
    private EditText et_password;
    private float income;
    private float expend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDatabase();

        sp = getSharedPreferences("data",Context.MODE_PRIVATE);
        editor = sp.edit();

        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);

        Button bt_login = findViewById(R.id.bt_logout);
        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                String password = et_password.getText().toString();
                Intent intent=null;
                Cursor cursor = db.query("user",null,null,null,null,null,null);
                while(cursor.moveToNext()) {
                    String ac = cursor.getString(cursor.getColumnIndex("account"));
                    String pa = cursor.getString(cursor.getColumnIndex("password"));
                    if (account.equals(ac) && password.equals(pa)) {
                        editor.putString("account",account);
                        editor.commit();

                        intent = new Intent(LoginActivity.this, MainActivity.class);

                        getData();

                        intent.putExtra("incomeList",incomeList);
                        intent.putExtra("expendList",expendList);
                        intent.putExtra("income","总收入:"+income);
                        intent.putExtra("expend","总支出:"+expend);
                        Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        break;
                    }else{
                        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        bt_login.setOnClickListener(loginListener);

        Button bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initDatabase(){
        MyDbHelper helper = new MyDbHelper(this,"demo.db",null,1);
        db=helper.getWritableDatabase();
    }

    private void getData(){
        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        String account = sp.getString("account","");

        Cursor cursor1 = db.query("income",new String[]{"productName","money","time","type","mark"},"account='"+account+"'",
                null,null,null,null);
        Cursor cursor2 = db.query("expend",new String[]{"productName","money","time","type","mark"},"account='"+account+"'",
                null,null,null,null);
        incomeList = new ArrayList<HashMap<String, Object>>();
        expendList = new ArrayList<HashMap<String, Object>>();
        while(cursor1.moveToNext()) {
            income_map = new HashMap<String, Object>();
            String productName1 = cursor1.getString(cursor1.getColumnIndex("productName"));
            Float money1 = cursor1.getFloat(cursor1.getColumnIndex("money"));
            String time1 = cursor1.getString(cursor1.getColumnIndex("time"));
            String type1 = cursor1.getString(cursor1.getColumnIndex("type"));
            String mark1 = cursor1.getString(cursor1.getColumnIndex("mark"));
            income+=money1;
            income_map.put("productName", productName1);
            income_map.put("money", "+"+money1);
            income_map.put("time", time1);
            income_map.put("type", type1.equals("cash")?"现金":"信用卡");
            income_map.put("mark", mark1);
            incomeList.add(income_map);
        }
        while (cursor2.moveToNext()){
            expend_map = new HashMap<String,Object>();
            String productName2 = cursor2.getString(cursor2.getColumnIndex("productName"));
            Float money2 = cursor2.getFloat(cursor2.getColumnIndex("money"));
            String time2 = cursor2.getString(cursor2.getColumnIndex("time"));
            String type2 = cursor2.getString(cursor2.getColumnIndex("type"));
            String mark2 = cursor2.getString(cursor2.getColumnIndex("mark"));
            expend+=money2;
            expend_map.put("productName",productName2);
            expend_map.put("money","-"+money2);
            expend_map.put("time",time2);
            expend_map.put("type",type2.equals("cash")?"现金":"信用卡");
            expend_map.put("mark",mark2);
            expendList.add(expend_map);
        }
    }
}
