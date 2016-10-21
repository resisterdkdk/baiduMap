package com.example.administrator.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class friendInfo extends Activity {
    private TextView textView_name;
    private TextView textView_num;
    private TextView textView_jw;
    private ImageButton bt_radar;
    private ImageButton bt_list;
    private ImageButton bt_delete;
    private ImageButton bt_edit;
    private ImageButton bt_ftoe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_info);
        final friends fri =new friends();
        textView_name = (TextView)findViewById(R.id.textView_name1);
        textView_num = (TextView)findViewById(R.id.textView_num1);
        textView_jw = (TextView)findViewById(R.id.textView_jw) ;
        textView_name.setText(fri.getName(fri.position_1));
        textView_num.setText(fri.getNum(fri.position_1));
        textView_jw.setText(String.valueOf(fri.getWei(fri.position_1))+"N/"+String.valueOf(fri.getJing(fri.position_1))+"E");
        bt_radar = (ImageButton) findViewById(R.id.Button_radar);
        bt_radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friendInfo.this,MainActivity.class);
                startActivity(intent);
            }
        });
        bt_list = (ImageButton) findViewById(R.id.button_list);
        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_delete = (ImageButton) findViewById(R.id.button_delete);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(friendInfo.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("删除该条信息！")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                final MainActivity ac = new MainActivity();
                                SharedPreferences.Editor editor = ac.pref.edit();
                                String key = String.valueOf(fri.position_1)+"_num";
                                editor.remove(key);
                                key = String.valueOf(fri.position_1)+"_name";
                                editor.remove(key);
                                key = String.valueOf(fri.position_1)+"_jing";
                                editor.remove(key);
                                key = String.valueOf(fri.position_1)+"_wei";
                                editor.remove(key);
                                editor.commit();
                                finish();
                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                        Log.i("alertdialog"," 数据保存！");
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        });
        bt_edit = (ImageButton) findViewById(R.id.button_edit);
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDia();
            }
        });
        bt_ftoe =(ImageButton)findViewById(R.id.imageButton19) ;
        bt_ftoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friendInfo.this,enemys.class);
                startActivity(intent);
            }
        });
    }
    private void showCustomDia()
    {
        final MainActivity ac = new MainActivity();
        final friends fri =new friends();
        AlertDialog.Builder customDia=new AlertDialog.Builder(friendInfo.this);
        final View viewDia= LayoutInflater.from(friendInfo.this).inflate(R.layout.dialog2, null);
        customDia.setTitle("请重新输入姓名和号码");
        customDia.setView(viewDia);
        customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText editText_name=(EditText) viewDia.findViewById(R.id.editText_name);
                EditText editText=(EditText) viewDia.findViewById(R.id.editText_num);

                Toast.makeText(friendInfo.this, "您输入的号码是："+editText.getText(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = ac.pref.edit();
                String key = String.valueOf(fri.position_1)+"_num";
                editor.putString(key,editText.getText().toString());
                key = String.valueOf(fri.position_1)+"_id";
                editor.putString(key,String.valueOf(fri.position_1));
                key = String.valueOf(fri.position_1)+"_name";
                editor.putString(key,editText_name.getText().toString());
                editor.commit();
                finish();
            }
        });
        customDia.setNegativeButton("取消",null);
        customDia.create().show();
    }
}
