package com.ximisoft.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/*
 Activity pour choisir le type de grille "choix" = -1 => au hasard, -2 grille du jour ou un nombre de 1 à 7
 */

public class GameTypeActivity extends AppCompatActivity {
    TextView chooseTextView;
    Button randomButton;
    Button dailyButton;
    Spinner grilleNumberpinner;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);
        chooseTextView = findViewById(R.id.chooseTextView);
        randomButton = findViewById(R.id.randomButton);
        dailyButton = findViewById(R.id.dailyButton);
        grilleNumberpinner = findViewById(R.id.grilleNumberspinner);

        ArrayList<String> number = new ArrayList<>();
        number.add("");
        for (int i = 1; i < 8; i++) {
            number.add(i+"");
        }

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, number);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        grilleNumberpinner.setAdapter(adapter);

        grilleNumberpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0) {
                    intent = new Intent(GameTypeActivity.this, JeuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("choix", Integer.valueOf(adapter.getItem(i)));
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GameTypeActivity.this,JeuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("choix",-2);
                startActivity(intent);
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(GameTypeActivity.this,JeuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("choix",-1);
                startActivity(intent);
            }
        });


    }
}
