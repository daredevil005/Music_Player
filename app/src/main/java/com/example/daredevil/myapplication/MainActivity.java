package com.example.daredevil.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv =(ListView)findViewById(R.id.lv);
        final ArrayList<File> mysongs =findsongs(Environment.getExternalStorageDirectory());
        item = new String[mysongs.size()];
        for(int i=0;i<mysongs.size();i++){
            toast(mysongs.get(i).getName().toString());
            item[i]=mysongs.get(i).getName().toString().replace(".mp3"," ").replace(".wav"," ");
        }

        ArrayAdapter<String> adp =new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,item);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),player.class).putExtra("pos",position).putExtra("songslist",mysongs));
            }
        });
    }
    public ArrayList<File> findsongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files =root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findsongs(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }
    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }
}
