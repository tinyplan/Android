package com.example.finalwork189050934;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IncomeAddActivity extends AppCompatActivity {
    private EditText et_productName;
    private EditText et_money;
    private EditText et_time;
    private RadioGroup rg_type;
    private EditText et_mark;
    private String account;

    private Button bt_submit;
    private Button bt_cancel;

    private String productName;
    private float money;
    private String time;
    private String type = "cash";
    private String mark;

    private float income = 0;
    private float expend = 0;

    private SQLiteDatabase db;
    private SharedPreferences sp;

    private ArrayList<HashMap<String,Object>> incomeList;
    private HashMap<String,Object> income_map;

    private ArrayList<HashMap<String,Object>> expendList;
    private HashMap<String,Object> expend_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_add);

        initDatabase();

        et_productName = findViewById(R.id.et_productName);
        et_money = findViewById(R.id.et_money);
        et_time = findViewById(R.id.et_time);
        et_mark = findViewById(R.id.et_mark);

        bt_submit = findViewById(R.id.bt_submit);
        bt_cancel = findViewById(R.id.bt_cancel);
        rg_type = findViewById(R.id.rg_type);

        //提交
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getSharedPreferences("data", Context.MODE_PRIVATE);

                account = sp.getString("account","");
                productName = et_productName.getText().toString();
                money = Float.parseFloat(et_money.getText().toString());
                time = et_time.getText().toString();
                mark = et_mark.getText().toString();

                ContentValues values = new ContentValues();
                values.put("account",account);
                values.put("productName",productName);
                values.put("money",money);
                values.put("time",time);
                values.put("type",type);
                values.put("mark",mark);

                db.insert("income",null,values);

                getData();

                Intent intent = new Intent(IncomeAddActivity.this,MainActivity.class);
                intent.putExtra("incomeList",incomeList);
                intent.putExtra("expendList",expendList);
                intent.putExtra("income","总收入:"+income);
                intent.putExtra("expend","总支出:"+expend);
                Toast.makeText(IncomeAddActivity.this,"收入添加成功",Toast.LENGTH_LONG).show();
                startActivity(intent);


//                Toast.makeText(IncomeAddActivity.this,"account="+account,Toast.LENGTH_LONG).show();
            }
        });

        //取消
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncomeAddActivity.this,MainActivity.class);
                sp = getSharedPreferences("data", Context.MODE_PRIVATE);
                account = sp.getString("account","");
                getData();
                intent.putExtra("expendList",expendList);
                intent.putExtra("incomeList",incomeList);
                intent.putExtra("income","总收入:"+income);
                intent.putExtra("expend","总支出:"+expend);
                Toast.makeText(IncomeAddActivity.this,"取消收入添加",Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        //单选框
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_card:
                        type = "card";
                        Toast.makeText(IncomeAddActivity.this,"已选择信用卡",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_cash:
                        type = "cash";
                        Toast.makeText(IncomeAddActivity.this,"已选择现金",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // 时间选择
        et_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });

        et_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

    }

    private void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(IncomeAddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                et_time.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void initDatabase(){
        MyDbHelper helper = new MyDbHelper(this,"demo.db",null,1);
        db=helper.getWritableDatabase();
    }

    private void getData(){
        incomeList = new ArrayList<HashMap<String,Object>>();
        expendList = new ArrayList<HashMap<String, Object>>();

        Cursor cursor = db.query("income",new String[]{"productName","money","time","type","mark"},"account='"+account+"'",
                null,null,null,null);
        while(cursor.moveToNext()){
            income_map = new HashMap<String,Object>();

            productName = cursor.getString(cursor.getColumnIndex("productName"));
            money = cursor.getFloat(cursor.getColumnIndex("money"));
            time = cursor.getString(cursor.getColumnIndex("time"));
            type = cursor.getString(cursor.getColumnIndex("type"));
            mark = cursor.getString(cursor.getColumnIndex("mark"));

            income+=money;

            income_map.put("productName",productName);
            income_map.put("money","+"+money);
            income_map.put("time",time);
            income_map.put("type",type.equals("cash")?"现金":"信用卡");
            income_map.put("mark",mark);

            incomeList.add(income_map);
        }

        Cursor cursor1 = db.query("expend",new String[]{"productName","money","time","type","mark"},"account='"+account+"'",
                null,null,null,null);
        while(cursor1.moveToNext()){
            expend_map = new HashMap<String,Object>();
            productName = cursor1.getString(cursor1.getColumnIndex("productName"));
            money = cursor1.getFloat(cursor1.getColumnIndex("money"));
            time = cursor1.getString(cursor1.getColumnIndex("time"));
            type = cursor1.getString(cursor1.getColumnIndex("type"));
            mark = cursor1.getString(cursor1.getColumnIndex("mark"));

            expend+=money;

            expend_map.put("productName",productName);
            expend_map.put("money","-"+money);
            expend_map.put("time",time);
            expend_map.put("type",type.equals("cash")?"现金":"信用卡");
            expend_map.put("mark",mark);

            expendList.add(expend_map);
        }
    }
}
