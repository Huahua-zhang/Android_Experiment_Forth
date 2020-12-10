package com.example.exp4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeActivity extends AppCompatActivity {
    ListView lv_like;
    ImageButton iv_back2;
    MyDBHelper dbHelper;
    private ArrayList<Integer> _ids = new ArrayList<>();
    private ArrayList<Integer> songIds = new ArrayList<>();
    private ArrayList<String> songNames = new ArrayList<>();
    private ArrayList<String> artists = new ArrayList<>();
    private String Url, As;
    private String json_url_list, json_as_list;
    //private String J_url_list,J_as_list;
    private int _id, songId;
    private String songName, artist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        iv_back2 = (ImageButton) findViewById(R.id.ib_back2);
        iv_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeActivity.this.finish();
            }
        });
        dbHelper = new MyDBHelper(this, "LIKE.db", null, 1);

        InitData();

        Log.d("DB", _ids.toString());
        Log.d("DB", songIds.toString());
        Log.d("DB", songNames.toString());
        Log.d("DB", artists.toString());
        Log.d("DBS",songNames.size()+"");
        lv_like = (ListView) findViewById(R.id.lv_like);
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                        MainActivity.this, android.R.layout.simple_list_item_2, song_list);
        List<Map<String, String>> listItems = new ArrayList<>();
        for (int i = 0; i < songNames.size(); i++) {
            //实例化Map对象
            Map<String, String> map = new HashMap<>();
            map.put("songName", songNames.get(i));
            map.put("artist", artists.get(i));
            //将map对象添加到List集合
            listItems.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(LikeActivity.this, listItems,
                android.R.layout.simple_list_item_2, new String[]{"songName", "artist"},
                new int[]{android.R.id.text1, android.R.id.text2});
        lv_like.setAdapter(adapter);

        //点击item事件
        lv_like.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int songId = songIds.get(position);
                songName = songNames.get(position);//得到song
                artist = artists.get(position);

                Intent intent = new Intent(LikeActivity.this, PlayActivity.class);
                intent.putExtra("thisSongName", songName);
                intent.putExtra("thisArtist", artist);
                intent.putExtra("songId", songId);
                intent.putExtra("position", position);
                intent.putExtra("as_list", songNames);
                intent.putExtra("id_list", songIds);

                startActivity(intent);

            }
        });
    }

    public void InitData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("MyLike", null, null, null,
                null, null, null);
//        .clear();
        _ids.clear();
        songIds.clear();
        songNames.clear();
        artists.clear();

        while (cursor.moveToNext()) {

            _id = cursor.getInt(cursor.getColumnIndex("_id"));
            songId = cursor.getInt(cursor.getColumnIndex("song_id"));
            songName = cursor.getString(cursor.getColumnIndex("song_name"));
            artist = cursor.getString(cursor.getColumnIndex("artist"));

            _ids.add(_id);
            songIds.add(songId);
            songNames.add(songName);
            artists.add(artist);
        }
        Log.d("DB", _ids.toString());
        Log.d("DB", songIds.toString());
        Log.d("DB", songNames.toString());
        Log.d("DB", artists.toString());

        cursor.close();
    }

}
