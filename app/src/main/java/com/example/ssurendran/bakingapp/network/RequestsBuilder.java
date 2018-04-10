package com.example.ssurendran.bakingapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.ssurendran.bakingapp.models.RecipeModel;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ssurendran on 4/10/18.
 */

public class RequestsBuilder {

    Context context;

    public RequestsBuilder(Context context) {
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public List<RecipeModel> makeRecipeListRequest() throws IOException, JSONException {
        String base_url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        try {
            return makeNetworkRequestAndParseResponse(new URL(base_url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<RecipeModel> makeNetworkRequestAndParseResponse(final URL url) throws IOException, JSONException {
        String response = makeNetworkCall(url);
        return new ResponseParser().parseRecipeList(response);
    }

    private String makeNetworkCall(URL url) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                result = readResponseString(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private String readResponseString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        String responseString = "";
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            responseString = readResponse(reader);

        } finally {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            inputStream.close();
        }
        return responseString;
    }

    private String readResponse(BufferedReader reader) throws IOException {
        if (reader == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
