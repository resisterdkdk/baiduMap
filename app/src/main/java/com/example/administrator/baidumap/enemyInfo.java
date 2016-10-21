package com.example.administrator.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class enemyInfo extends Activity{
    private TextView textView_name;
    private TextView textView_num;
    private TextView textView_jw;
    private ImageButton bt_radar;
    private ImageButton bt_list;
    private ImageButton bt_delete;
    private ImageButton bt_edit;
    private ImageButton bt_eotf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enemy_info);
        final enemys ene =new enemys();
        textView_name = (TextView)findViewById(R.id.textView_name1_enemy);
        textView_num = (TextView)findViewById(R.id.textView_num1_enemy);
        textView_jw = (TextView)findViewById(R.id.textView_jw_enemy) ;
        textView_name.setText(ene.getName(ene.position_1));
        textView_num.setText(ene.getNum(ene.position_1));
        textView_jw.setText(String.valueOf(ene.getWei(ene.position_1))+"N/"+String.valueOf(ene.getJing(ene.position_1))+"E");
        bt_radar = (ImageButton) findViewById(R.id.Button_radar_enemy);
        bt_radar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(enemyInfo.this,MainActivity.class);
                startActivity(intent);
            }
        });
        bt_list = (ImageButton) findViewById(R.id.button_list_enemy);
        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_delete = (ImageButton) findViewById(R.id.button_delete_enemy);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(enemyInfo.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("删除该条信息！")//设置显示的内容
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                final MainActivity ac = new MainActivity();
                                SharedPreferences.Editor editor = ac.pref_enemy.edit();
                                String key = String.valueOf(ene.position_1)+"_num";
                                editor.remove(key);
                                key = String.valueOf(ene.position_1)+"_name";
                                editor.remove(key);
                                key = String.valueOf(ene.position_1)+"_jing";
                                editor.remove(key);
                                key = String.valueOf(ene.position_1)+"_wei";
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
        bt_edit = (ImageButton) findViewById(R.id.button_edit_enemy);
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDia();
            }
        });
        bt_eotf =(ImageButton)findViewById(R.id.imageButton_etof);
        bt_eotf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(enemyInfo.this,friends.class);
                startActivity(intent);
            }
        });
    }
    private void showCustomDia()
    {
        final MainActivity ac = new MainActivity();
        final enemys ene =new enemys();
        AlertDialog.Builder customDia=new AlertDialog.Builder(enemyInfo.this);
        final View viewDia= LayoutInflater.from(enemyInfo.this).inflate(R.layout.dialog2, null);
        customDia.setTitle("请重新输入姓名和号码");
        customDia.setView(viewDia);
        customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText editText_name=(EditText) viewDia.findViewById(R.id.editText_name);
                EditText editText=(EditText) viewDia.findViewById(R.id.editText_num);

                Toast.makeText(enemyInfo.this, "您输入的号码是："+editText.getText(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = ac.pref_enemy.edit();
                String key = String.valueOf(ene.position_1)+"_num";
                editor.putString(key,editText.getText().toString());
                key = String.valueOf(ene.position_1)+"_id";
                editor.putString(key,String.valueOf(ene.position_1));
                key = String.valueOf(ene.position_1)+"_name";
                editor.putString(key,editText_name.getText().toString());
                editor.commit();
                finish();
            }
        });
        customDia.setNegativeButton("取消",null);
        customDia.create().show();
    }
}
