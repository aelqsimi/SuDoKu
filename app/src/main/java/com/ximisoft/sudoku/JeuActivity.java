package com.ximisoft.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class JeuActivity extends AppCompatActivity implements Constants{
    final String TAG = JeuActivity.class.getName();
    ProgressDialog progress;
    private Boolean exit = false;
    Grille grille;
    SeekBar seekBar;
    Button verifyButton;
    Button retryButton;
    int x,y;
    /* 10 minutes par grille */
    public int seconds = 60;
    public int minutes = 9;
    Timer timer;
    TextView timerValue;
    TextView timerText;
    SharedPreferences prefs;
    String baseGrid;
    DBAdapter dbAdapter;
    Intent activityIntent;
    MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        activityIntent = getIntent();
        grille = findViewById(R.id.grille);
        grille.setVisibility(View.INVISIBLE);
        seekBar = findViewById(R.id.seekBar);
        verifyButton = findViewById(R.id.verifyButton);
        retryButton = findViewById(R.id.retryButton);
        retryButton.setVisibility(View.INVISIBLE);
        timerValue = findViewById(R.id.timeValueTextView);
        timerText = findViewById(R.id.timeTextView);

         dbAdapter = new DBAdapter(this);


        String myprefs="sudoku";
        prefs = getSharedPreferences(myprefs, Activity.MODE_PRIVATE);

        getGrilles();

        /* le OnTouchListener de la grille */
        grille.setOnTouchListener(onTouch);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* un petit son sympa si on pert ou on gagne */
                mMediaPlayer = new MediaPlayer();
                if(grille.verify()){
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.won);
                }
                else
                {
                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lose);
                }
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.start();

                    seekBar.setEnabled(false);
                    retryButton.setVisibility(View.VISIBLE);
                    grille.setOnTouchListener(null);
                    stopTimer();
            }
        });
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setEnabled(true);
                seekBar.setProgress(0);
                retryButton.setVisibility(View.INVISIBLE);
                grille.redraw(0);
                grille.setOnTouchListener(onTouch);
                startTimer();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                grille.redraw(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /* notre BroadcastReceiver rempli la grille par rapport au choix effectué dans l'activité précédente (grille de jour, au hasard ou par id) */
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // clear the progress indicator
            if (progress != null) {
                progress.dismiss();
                String response = intent.getStringExtra(RestApi.HTTP_RESPONSE);
                JSONArray jsonArray;
                JSONObject jsonObject;
                try {
                    jsonArray = new JSONArray(response);
                    /* au hasard */
                    if(activityIntent.getIntExtra("choix",-1) == -1) {
                        int rnd = new Random().nextInt(jsonArray.length());
                        jsonObject = jsonArray.getJSONObject(rnd);
                        if (jsonObject.has("grille")) {
                            grille.set(jsonObject.getString("grille"));
                            grille.redraw(0);
                            grille.setVisibility(View.VISIBLE);
                            baseGrid = jsonObject.getString("grille");
                            startTimer();
                        }
                    }
                    /* Grille du jour par rapport au numero du jour courant */
                    else if(activityIntent.getIntExtra("choix",-1) == -2) {
                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);
                        jsonObject = jsonArray.getJSONObject(day);
                        if (jsonObject.has("grille")) {
                            grille.set(jsonObject.getString("grille"));
                            grille.redraw(0);
                            grille.setVisibility(View.VISIBLE);
                            baseGrid = jsonObject.getString("grille");
                            startTimer();
                        }
                    }
                    else{
                        /* sinon bah... on choisi dans le JSONArray */
                        jsonObject = jsonArray.getJSONObject(activityIntent.getIntExtra("choix",-1)-1);
                        if (jsonObject.has("grille")) {
                            grille.set(jsonObject.getString("grille"));
                            grille.redraw(0);
                            grille.setVisibility(View.VISIBLE);
                            baseGrid = jsonObject.getString("grille");
                            startTimer();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /* on demande à l'utilisateur s'il veut quitter vraiment sans sauvegarder et tt ça */
    @Override
    public void onBackPressed() {
        stopTimer();
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    /* il part sans sauvegarder, c'est son problème */
                    case DialogInterface.BUTTON_POSITIVE: {
                        stopTimer();
                        finish();
                    }
                        break;
                    /* notre message lui a fait réfléchir et il décide de rester finalement */
                    case DialogInterface.BUTTON_NEGATIVE:
                        startTimer();
                        break;
                    /* il prend ses précautions et sauvegarde avec de quitter, tant mieux pour lui */
                    case DialogInterface.BUTTON_NEUTRAL:
                    {
                        saveParty();
                        stopTimer();
                        finish();
                    }
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(JeuActivity.this);
        builder.setMessage("Etes-vous sur de vouloir quitter? Pensez à sauvegarder la partie").setPositiveButton("Quitter", dialogClickListener)
                .setNegativeButton("Annuler", dialogClickListener).setNeutralButton("Sauvegarder et quitter",dialogClickListener).setCancelable(false).show();

    }

    /* la focntion qui gère notre timer (utilisation du scheduler de la classe Timer */
    public void startTimer(){
        timer = new Timer();
        //Set the schedule function and rate
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        String time = String.valueOf(minutes)+":"+String.valueOf(seconds);
                        timerValue.setText(time);
                        seconds -= 1;

                        if(seconds == 0)
                        {
                            if(minutes == 0){
                                mMediaPlayer = new MediaPlayer();
                                if(grille.verify()){
                                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.won);
                                }
                                else
                                {
                                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lose);
                                }
                                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mMediaPlayer.setLooping(false);
                                mMediaPlayer.start();

                                grille.setOnTouchListener(null);
                                verifyButton.setEnabled(false);
                                seekBar.setEnabled(false);
                                stopTimer();
                            }
                            time = String.valueOf(minutes)+":"+String.valueOf(seconds);
                            timerValue.setText(time);

                            seconds=60;
                            minutes=minutes-1;

                        }

                    }

                });
                /* toute les secodes on fait notre traitement */
            }

        }, 0, 1000);
    }

    public void stopTimer(){
        if(timer != null)
            timer.cancel();
    }

    /* la fonction qui appell le service REST pour l'obtention de la liste des grilles */
    private void getGrilles()
    {
        // the request
        try
        {
            progress = ProgressDialog.show(this, "Patientez ...", "Obtention de la grille...", true);
            Map<String,String> map  = new HashMap<>();
            HttpPost httpPost = new HttpPost(new URI(GRILLE_URL));

            //passes the results to a string builder/entity
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            RestApi restApi = new RestApi(JeuActivity.this, ACTION_GRILLE);
            restApi.execute(httpPost);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_GRILLE));
    }
    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /* le retour de l'activité choix et remplissage de la grille suivant le choix du joueur */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String v=data.getStringExtra("v");
                if(v.equalsIgnoreCase("")){
                    grille.set(x,y,0);
                }
                else
                    grille.set(x,y,Integer.valueOf(v));
                grille.redraw(seekBar.getProgress());
            }
        }
    }

    private View.OnTouchListener onTouch = new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                if (action==MotionEvent.ACTION_DOWN)
                {
                    x = grille.getXFromMatrix((int)event.getX());
                    y = grille.getYFromMatrix((int)event.getY());
                    if(grille.isNotFix(x,y)){
                        Intent i = new Intent(getApplicationContext(), ChoixActivity.class);
                        startActivityForResult(i, 1);
                    }
                }
                else if(action==MotionEvent.ACTION_UP)
                    view.performClick();
                // TODO Auto-generated method stub
                return false;
            }
    };
    /* on sauvegarde */

    public void saveParty(){
        dbAdapter.update(prefs.getString("prenom",""),prefs.getString("nom",""),baseGrid, grille.get(),String.valueOf(minutes),String.valueOf(seconds));
        Toast.makeText(this,"Partie sauvegardée",Toast.LENGTH_LONG).show();
    }

    /* on restaure */
    public void restoreParty() {
        /* il peut y avoir plusieur joueurs sur le meme telephone donc on récupère toutes les lignes puis on verifie la cle (PrenomNom) */
        HashMap<String,HashMap<String,String>> map = dbAdapter.getData();
        HashMap<String,String> map2 = map.get(prefs.getString("prenom","")+prefs.getString("nom",""));
            if(map2!=null && map2.size()>0){
                String gr = map2.get("grilleDeBase");
                grille.set(gr);
                gr =  map2.get("grilleCourante");
                Toast.makeText(this,"Partie restaurée",Toast.LENGTH_LONG).show();

                for(int i = 0; i < 9; i ++){
                    for(int j = 0; j <9; j++)
                    if(grille.isNotFix(j,i))
                        grille.set(j,i,Character.getNumericValue(gr.charAt((9*i)+j)));
                }
                grille.redraw(progress.getProgress());
                stopTimer();
                minutes = Integer.valueOf(map2.get("Minutes"));
                seconds = Integer.valueOf(map2.get("Secondes"));
                startTimer();
            }

    }

    public void retry(){
        grille.set(baseGrid);
        grille.redraw(progress.getProgress());
        stopTimer();
        minutes = 9;
        seconds = 60;
        seekBar.setEnabled(true);
        retryButton.setVisibility(View.INVISIBLE);
        grille.setOnTouchListener(onTouch);
        startTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.game_menu, menu);
            return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            /* on recommence le jeu */
            case R.id.retry:
                retry();
                return true;
                /* on sauvegarde */
            case R.id.save:
                saveParty();
                return true;
                /* on restaure */
            case R.id.restore:
                restoreParty();
                return true;
                /* on se deconnecte */
            case R.id.logout:
            {
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                    /* se déconnecter */
                            case DialogInterface.BUTTON_POSITIVE: {
                                SharedPreferences.Editor editor=prefs.edit();
                                editor.putBoolean("connected", false);
                                editor.apply();
                                stopTimer();
                                startActivity(new Intent(JeuActivity.this,LogInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                break;
                            }
                    /* notre message lui a fait réfléchir et il décide de rester finalement */
                            case DialogInterface.BUTTON_NEGATIVE:
                                startTimer();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(JeuActivity.this);
                builder.setMessage("Voulez-vous vraiment vous déconnecter? :(").setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).setCancelable(false).show();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

