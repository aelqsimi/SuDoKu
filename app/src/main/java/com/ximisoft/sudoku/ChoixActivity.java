package com.ximisoft.sudoku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChoixActivity extends AppCompatActivity {
    ListView chooseListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix);
        final List<String> chooseArray =  new ArrayList<String>();
        chooseArray.add("");
        chooseArray.add("1");
        chooseArray.add("2");
        chooseArray.add("3");
        chooseArray.add("4");
        chooseArray.add("5");
        chooseArray.add("6");
        chooseArray.add("7");
        chooseArray.add("8");
        chooseArray.add("9");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, chooseArray);

        chooseListView = findViewById(R.id.chooseListView);
        chooseListView.setAdapter(adapter);

        chooseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("v",String.valueOf(chooseArray.get(i)));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}
