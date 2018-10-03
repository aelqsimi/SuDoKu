package com.ximisoft.sudoku;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by abdel on 09/12/17.
 * classe qui permet déffectuer les requete REST vers le serveur
 */


public class RestApi extends AsyncTask<Object, Void, String>
{
    private static final String TAG = RestApi.class.getName();
    public static final String HTTP_RESPONSE = "httpResponse";

    private Context context;
    private HttpClient client;
    private String action;

    RestApi(Context context, String action)
    {
        this.context = context;
        this.action = action;
        client = new DefaultHttpClient();
    }

    @Override
    protected String doInBackground(Object... params)
    {
        try
        {
            HttpUriRequest request = (HttpUriRequest)params[0];
            HttpResponse serverResponse = client.execute(request);
            BasicResponseHandler handler = new BasicResponseHandler();
            return handler.handleResponse(serverResponse);
        }
        catch (Exception e)
        {
            // TODO handle this properly
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        Log.i(TAG, "RESULT = " + result);
        Intent intent = new Intent(action);
        intent.putExtra(HTTP_RESPONSE, result);
        context.sendBroadcast(intent);
    }

}
