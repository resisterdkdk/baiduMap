package com.example.administrator.baidumap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/16 0016.
 */

public class friends extends Activity {
    private ImageButton bt_back;
    private ImageButton bt_add1;
    private ImageButton bt_ftoe;
    private Context context;
    public static int position_1;
    private ListView lv;
    static int Key[] = new int[65535];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);
        bt_back = (ImageButton) findViewById(R.id.button_RAZER);
        bt_add1 = (ImageButton) findViewById(R.id.button_add);
        bt_ftoe =(ImageButton)findViewById(R.id.imageButton_ftoe);

        //this.context = this;
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"hahah",Toast.LENGTH_SHORT).show();
                //finish();
                Intent intent = new Intent(friends.this,MainActivity.class);
                startActivity(intent);
            }
        });
        bt_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDia();
            }
        });
        bt_ftoe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(friends.this,enemys.class);
                startActivity(intent);
                return true;
            }
        });
        bt_ftoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friends.this,enemys.class);
                startActivity(intent);
            }
        });
    }
    private void showCustomDia()
    {
        final MainActivity ac = new MainActivity();
        AlertDialog.Builder customDia=new AlertDialog.Builder(friends.this);
        final View viewDia=LayoutInflater.from(friends.this).inflate(R.layout.dialog2, null);
        customDia.setTitle("请输入姓名和号码");
        customDia.setView(viewDia);
        customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                EditText editText_name=(EditText) viewDia.findViewById(R.id.editText_name);
                EditText editText=(EditText) viewDia.findViewById(R.id.editText_num);

                Toast.makeText(friends.this, "您输入的号码是："+editText.getText(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = ac.pref.edit();
                String key = String.valueOf(ac.friend_num)+"_num";
                editor.putString(key,editText.getText().toString());
                key = String.valueOf(ac.friend_num)+"_id";
                editor.putString(key,String.valueOf(ac.friend_num));
                key = String.valueOf(ac.friend_num)+"_name";

                editor.putString(key,editText_name.getText().toString());
                ac.friend_num++;
                editor.commit();
                onResume();
            }
        });
        customDia.setNegativeButton("取消",null);
        customDia.create().show();
    }
    /*
        private void showDialog(){
            final MainActivity ac = new MainActivity();
            final EditText editText_name = new EditText(friends.this);
            final EditText editText = new EditText(friends.this);
            editText_name.setText("姓名");
            editText.setText("电话号码");
            new AlertDialog.Builder(friends.this).setTitle("请输入电话号码").setView(editText_name).setView(editText).setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Toast.makeText(friends.this, "您输入的号码是："+editText.getText(), Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = ac.pref.edit();
                            String key = String.valueOf(ac.friend_num)+"_num";
                            editor.putString(key,editText.getText().toString());
                            key = String.valueOf(ac.friend_num)+"_id";
                            editor.putString(key,String.valueOf(ac.friend_num));
                            key = String.valueOf(ac.friend_num)+"_name";
                            String frin = "Friend "+String.valueOf(ac.friend_num+1);
                            editor.putString(key,editText_name.toString());
                            ac.friend_num++;
                            editor.commit();
                            onResume();
                        }
                    }).setNegativeButton("取消", null).show();
        }*/

    @Override
    protected void onResume() {
        super.onResume();
        final MainActivity ac = new MainActivity();
        lv = (ListView) findViewById(R.id.lv);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
        /*在数组中存放数据*/
        int num=0;
        for(int i=0;i<ac.friend_num;i++)
        {
            String key1 = String.valueOf(i)+"_num";
            String key2 = String.valueOf(i)+"_name";
            String key3 = String.valueOf(i)+"_id";
            HashMap<String, Object> map = new HashMap<String, Object>();
            if(ac.pref.getString(key1,null)==null) continue;
                map.put("textView_name", ac.pref.getString(key2,null));
                map.put("textView_num", ac.pref.getString(key1,null));
                map.put("textView_id",ac.pref.getString(key3,null));
                Key[num++] = i;
                listItem.add(map);
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.friend_item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"textView_name","textView_id","textView_num"},
                new int[]{R.id.textView_name,R.id.textView_id,R.id.textView_num}
        );

        lv.setAdapter(mSimpleAdapter);//为ListView绑定适配器

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //setTitle("你点击了第"+position+"行");//设置标题栏显示点击的行
                position_1 = Key[position];
                Intent intent =new Intent(friends.this,friendInfo.class);
                startActivityForResult(intent,position);
            }
        });
    }
    public String getNum(int id)
    {
        final MainActivity ac = new MainActivity();
        String key = String.valueOf(id)+"_num";
        return ac.pref.getString(key,null);
    }
    public String getName(int id)
    {
        final MainActivity ac = new MainActivity();
        String key = String.valueOf(id)+"_name";
        return ac.pref.getString(key,null);
    }
    public float getJing(int id)
    {
        final MainActivity ac = new MainActivity();
        String key = String.valueOf(id)+"_jing";
        return ac.pref.getFloat(key,0);
    }
    public float getWei(int id)
    {
        final MainActivity ac = new MainActivity();
        String key = String.valueOf(id)+"_wei";
        return ac.pref.getFloat(key,0);
    }
}
