package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

    private CheckBox mCheckBox;
    private EditText username_edit,password_edit;
    private String theusername="",thepassword="";
    private SharedPreferences sp;

    public void init(){
        sp = this.getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE);
        mCheckBox = (CheckBox)findViewById(R.id.checkBox);
        username_edit =(EditText)findViewById(R.id.the_username);
        password_edit = (EditText)findViewById(R.id.the_password);

        //判断是否选中状态，是则填写账号密码
        if (sp.getBoolean("ischeck",false)){
            mCheckBox.setChecked(true);
            username_edit.setText(sp.getString("username_",""));
            password_edit.setText(sp.getString("password_",""));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void login(View view) {
        try {
            theusername = username_edit.getText().toString();
            thepassword = password_edit.getText().toString();
            SharedPreferences.Editor editor = sp.edit();
            if (theusername.equals("admin") && thepassword.equals("123456")) {
                show(this, "成功验证！");
                if (mCheckBox.isChecked()) {
                    editor.putString("username_", theusername);
                    editor.putString("password_", thepassword);
                    editor.putBoolean("ischeck", true);
                    editor.commit();
                } else if (!mCheckBox.isChecked()) {
                    editor.putString("username_", "");
                    editor.putString("password_", "");
                    editor.putBoolean("ischeck", false);
                    editor.commit();
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                show(this, "验证失败！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
