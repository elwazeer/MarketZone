package com.marketzone.app.ui.notify;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marketzone.app.MainActivity;
import com.marketzone.app.PostLog;
import com.marketzone.app.Products.Filter;
import com.marketzone.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import static java.lang.Float.parseFloat;

public class notifyFragment extends Fragment {
    public PostLog mainActivity;

    List<notifyListItem> cartList = new ArrayList<notifyListItem>();
    private notifyViewModel nvm;
    ListView listView;

//, "getBrands/"};
    ArrayList<String> notArray = new ArrayList<String>();
    ArrayList<String> notTime = new ArrayList<String>();
    notifications_list_adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        nvm = ViewModelProviders.of(this).get(notifyViewModel.class);
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        mainActivity = (PostLog)getActivity();
        String UID = mainActivity.UID;
        final String apiURL = "http://10.40.46.65:3000/project/";
        final String[] apiCall = {"getNotifications?UID=\"" + UID + "\""};
        new apiConnect(view).execute(apiURL + apiCall[0]);//, apiURL + apiCall[1]);

        return view;
    }

    private class apiConnect extends AsyncTask<String, Void, Void> {
        HttpURLConnection http;
        JSONArray apiIn;
        JSONObject msg = new JSONObject();
        URL url;

        View view;

        public apiConnect(View view){
            this.view = view;
        }

        protected Void doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.connect();
                if (http.getResponseCode() == 200) {
                    InputStream stream = new BufferedInputStream(http.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder apiStream = new StringBuilder();

                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        apiStream.append(inputString);
                    }

                    apiIn = new JSONArray(apiStream.toString());
                    if (apiIn.length() > 0) {
                        for(int i = 0; i < apiIn.length(); i++) {
                            msg = apiIn.getJSONObject(i);
                            notArray.add(msg.getString("Msg"));
                            notTime.add(msg.getString("ts"));
                        }
                    }
                    Log.v("JSON Read2", "Succeed");
                }
                else
                    Log.v("JSON Read1", "Failed");

                http.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void arg) {
            //////////////////////////////DataBase Json parsing
            //final int resId = getResources().getIdentifier("notText", "array", getActivity().getPackageName());
            //String[] testArray = getResources().getStringArray(resId);
            String[] testArray = notArray.toArray(new String[0]);

            //final int price = getResources().getIdentifier("timeStamp", "array", getActivity().getPackageName());
            //final String[] prices = getResources().getStringArray(price);
            String[] prices = notTime.toArray(new String[0]);


            /////////////////////////////
            for (int i = 0; i < testArray.length; i++) {
                notifyListItem item = new notifyListItem(testArray[i], prices[i]);
                cartList.add(item);
            }

            listView = (ListView) view.findViewById(R.id.first);
            adapter = new notifications_list_adapter(getActivity(), R.layout.notify_list_item, cartList);
            listView.setAdapter(adapter);
        }
    }
}