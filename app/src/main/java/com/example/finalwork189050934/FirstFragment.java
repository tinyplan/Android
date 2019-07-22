package com.example.finalwork189050934;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    private SQLiteDatabase db;
    private SharedPreferences sp;

    private EditText et_year;
    private EditText et_month;
    private EditText et_day;
    private RadioGroup rg_type;
    private Button bt_search;
    private ListView lv_search;

    private String year;
    private String month;
    private String day;
    private String searchTime;

    private String type = "income";
    private String account;

    private HashMap<String,Object> searchMap;
    private ArrayList<HashMap<String,Object>> searchList;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the lv_income_item for this fragment
        View view =  inflater.inflate(R.layout.fragment_first, container, false);
        sp = view.getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        initDatabase();
        searchList = new ArrayList<HashMap<String, Object>>();
        View header = inflater.inflate(R.layout.lv_income_header,null);
        et_year = view.findViewById(R.id.et_year);
        et_month = view.findViewById(R.id.et_month);
        et_day = view.findViewById(R.id.et_day);

        rg_type = view.findViewById(R.id.rg_type);

        bt_search = view.findViewById(R.id.bt_search);

        lv_search= view.findViewById(R.id.lv_search);
        lv_search.addHeaderView(header);

        account = sp.getString("account","");
        //类型
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_income:
                        type = "income";
                        Toast.makeText(getContext(),"已选择收入",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_expend:
                        type = "expend";
                        Toast.makeText(getContext(),"已选择支出",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String icon = "+";
                year = et_year.getText().toString();
                month = et_month.getText().toString();
                day = et_day.getText().toString();
                if(month.isEmpty()){
                    searchTime = year + "-";
                }else if(day.isEmpty()) {
                    searchTime = year + "-" + month + "-";
                }else{
                    searchTime = year + "-" + month + "-" + day;
                }
                Toast.makeText(getContext(),searchTime,Toast.LENGTH_SHORT).show();
                searchList.clear();//清空列表，防止数据重复出现的BUG
                if(type.equals("expend")){
                    icon = "-";
                }
                Cursor cursor = db.query(type,new String[]{"productName","money","time","type","mark"},"account='"+account+"'",null,null,null,null);
                while(cursor.moveToNext()){
                    searchMap = new HashMap<String, Object>();
                    String productName = cursor.getString(cursor.getColumnIndex("productName"));
                    Float money = cursor.getFloat(cursor.getColumnIndex("money"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String type1 = cursor.getString(cursor.getColumnIndex("type"));
                    String mark = cursor.getString(cursor.getColumnIndex("mark"));
                    if(time.contains(searchTime)){
                        searchMap.put("productName",productName);
                        searchMap.put("money",icon+money);
                        searchMap.put("time",time);
                        searchMap.put("type",type1.equals("cash")?"现金":"信用卡");
                        searchMap.put("mark",mark);

                        searchList.add(searchMap);
                    }
                }
                SimpleAdapter adapter = new SimpleAdapter(getContext(),searchList,R.layout.lv_income_item,
                        new String[]{"productName","money","type","mark","time"},
                        new int[]{R.id.tv_productName,R.id.tv_money,R.id.tv_type,R.id.tv_mark,R.id.tv_time});
                lv_search.setAdapter(adapter);
            }
        });

        return view;
    }

    private void initDatabase(){
        MyDbHelper helper = new MyDbHelper(getContext(),"demo.db",null,1);
        db=helper.getWritableDatabase();
    }
}
