package com.ximisoft.sudoku;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abdel on 10/12/17.
 */

public class LogInActivity extends AppCompatActivity implements Constants{
    private String name, login, pwd;
    boolean connected = false;
    ProgressDialog progress;
    final String TAG = LogInActivity.class.getName();
    EditText loginTxt, pwdTxt;
    Button connexion;
    SharedPreferences prefs;
    TextView newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* les préférences on regarde si l'utilisateur est déjà loggé */
        String myprefs="sudoku";
        prefs = getSharedPreferences(myprefs, Activity.MODE_PRIVATE);
        newUser = findViewById(R.id.newUserButton);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(SINGUP_URL));
                startActivity(browser);
            }
        });

        connected = prefs.getBoolean("connected", false);

        /* si oui on passe directement à l'activity principale */
        if(connected)
        {

            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        /* sinon on continue */

        loginTxt = findViewById(R.id.loginEditText);
        pwdTxt = findViewById(R.id.pwdEditText);
        connexion = findViewById(R.id.connexionButton);
        connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect(loginTxt.getText().toString(),pwdTxt.getText().toString());
            }
        });


    }
    @Override
    public void onResume()
    {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ACTION_LOGIN));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /* fonction pour se connecter */
    private void connect(String login, String pwd)
    {
        // the request
        try
        {
            Map<String,String> map  = new HashMap<>();
            /* on ajoute le login et le mot de passe à notre requete*/
            map.put("login",login);
            map.put("passe",pwd);
            HttpPost httpPost = new HttpPost(new URI(CONNEXION_URL));
            /* notre paramètre son de type JSON */
            JSONObject holder = new JSONObject(map);

            //passes the results to a string builder/entity
            StringEntity se = new StringEntity(holder.toString());
            httpPost.setEntity(se);
            /* on spécifie que c'est du JSON que l'on envoie */
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            RestApi restApi = new RestApi(getApplicationContext(), ACTION_LOGIN);
            restApi.execute(httpPost);
            progress = ProgressDialog.show(this, "Connexion ...", "Un moment...", true);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    /* le BroadcastReceiver attent le retour de RestAPI qui l'envoi en boradcast avec le code login */
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // clear the progress indicator
            if (progress != null)
            {
                progress.dismiss();
            }
            String response = intent.getStringExtra(RestApi.HTTP_RESPONSE);
            JSONObject resp = null;
            try {
                resp = new JSONObject(response);

            if(resp.has("resp") && resp.get("resp").toString().equalsIgnoreCase("0")){
                Toast.makeText(context,"Utilisateur non existant",Toast.LENGTH_LONG).show();
            }
            else if(resp.has("Erreur")){
                Toast.makeText(context,"Mot de passe erroné",Toast.LENGTH_LONG).show();
            }
            else{
                SharedPreferences.Editor editor=prefs.edit();
                editor.putBoolean("connected", true);
                editor.putString("nom", resp.getString("nom"));
                editor.putString("prenom", resp.getString("prenom"));

                Intent i = new Intent(context,MainActivity.class);
                editor.apply();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);

            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
