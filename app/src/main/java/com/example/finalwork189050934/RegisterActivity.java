package com.example.finalwork189050934;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_account;
    private EditText et_password;
    private String account;
    private String password;

    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatabase();

        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);

        Button bt_register = (Button) findViewById(R.id.bt_register);
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = et_account.getText().toString();
                password = et_password.getText().toString();

                if(account.equals("")){
                    Toast.makeText(RegisterActivity.this,"注册用户名为空",Toast.LENGTH_LONG).show();
                }else if (password.equals("")){
                    Toast.makeText(RegisterActivity.this,"登录密码为空",Toast.LENGTH_LONG).show();
                }else {
                    int flag = 0;
                    Cursor cursor = db.query("user",null,null,null,null,null,null);
                    while(cursor.moveToNext()) {
                        String ac = cursor.getString(cursor.getColumnIndex("account"));
                        if(account.equals(ac)) {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag==0) {
                        ContentValues values = new ContentValues();
                        values.put("account", account);
                        values.put("password", password);
                        db.insert("user", null, values);

                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this, "用户名已被注册", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    private void initDatabase(){
        MyDbHelper helper = new MyDbHelper(this,"demo.db",null,1);
        db=helper.getWritableDatabase();
    }
}
