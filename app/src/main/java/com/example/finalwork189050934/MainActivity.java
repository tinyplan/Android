package com.example.finalwork189050934;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ViewPager viewPager;
    private SQLiteDatabase db;

    public void gotoExpendAdd(){
        Intent intent = new Intent(MainActivity.this,ExpendAddActivity.class);
        startActivity(intent);
    }

    public void gotoIncomeAdd(){
        Intent intent = new Intent(MainActivity.this,IncomeAddActivity.class);
        startActivity(intent);
    }

    public void gotoAlterPwd(){
        Intent intent = new Intent(MainActivity.this,AlterPwdActivity.class);
        startActivity(intent);
    }

    private void initDatabase(){
        MyDbHelper helper = new MyDbHelper(this,"demo.db",null,1);
        db=helper.getWritableDatabase();
    }

    private void initFragments(){
        FirstFragment fragment1=new FirstFragment();
        SecondFragment fragment2 = new SecondFragment();
        ThirdFragment fragment3 = new ThirdFragment();
        ForthFragment fragment4 = new ForthFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_me:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragments();
        initDatabase();

        viewPager=findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


//        Bundle bundle = intent.getBundleExtra("incomeList");
//        incomeList = (ArrayList<String[]>) bundle.getSerializable("incomeList");
//        lv_income = findViewById(R.id.lv_income);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.lv_income_item.simple_list_item_1,);
//        lv_income.setAdapter(adapter1);
//        Intent intent = getIntent();
//        String msg = intent.getStringExtra("data");

//        Toast.makeText(MainActivity.this, "消息="+msg, Toast.LENGTH_LONG).show();

    }

}
