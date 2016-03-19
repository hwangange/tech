package com.example.angela.technovations2;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Angie on 3/18/2016.
 */
public class SignatureBackground extends AsyncTask <String,Void,String>{
    Context ctx;

    SignatureBackground(Context ctx) {
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String sig_url = "http://10.0.0.8/Technovations2/php/signature.php";
        String method = params[0];
        if(method.equals("signature"))
        {
            String name = params[1];
            String sig = params[2];
            try {
                URL url = new URL(sig_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("user","UTF-8") + "=" + URLEncoder.encode("name", "UTF-8") + "&" +
                        URLEncoder.encode("sig","UTF-8") + "=" + URLEncoder.encode("sig", "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Successfully saved signature";

            } catch(MalformedURLException e) {
                e.printStackTrace();
                return "MalformedURLException";

            } catch (IOException e) {
                e.printStackTrace();
                return "IOException";
            }

        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
