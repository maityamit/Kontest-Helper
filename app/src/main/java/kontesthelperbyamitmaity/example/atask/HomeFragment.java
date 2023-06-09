package kontesthelperbyamitmaity.example.atask;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    List<List<String>> dataList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.contest_list_recylerview);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //API Call for Contests
        retriveAllUpcomingContestsList();


        return view;

    }


    private void retriveAllUpcomingContestsList(){
        String API_URL = "https://kontests.net/api/v1/all";
        StringRequest request = new StringRequest(Request.Method.GET, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                List<String> temp = new ArrayList<>();

                                String problemName = jsonObject.getString("name");
                                String problemLink = jsonObject.getString("url");
                                String startTime = jsonObject.getString("start_time");
                                String endTime = jsonObject.getString("end_time");
                                String site = jsonObject.getString("site");

                                if(site.equals("CodeChef")||site.equals("LeetCode")||site.equals("CodeForces")){
                                    temp.add(problemName);
                                    temp.add(problemLink);
                                    temp.add(startTime);
                                    temp.add(endTime);
                                    temp.add(site);

                                    dataList.add(temp);
                                }
                            }
                            dataList.remove(0);
                            UpcomingContestCustomAdapter adapter = new UpcomingContestCustomAdapter(dataList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }


    private String[] retrieveCodeforcesRatings(String s) {

        String API_URL = "https://codeforces.com/api/user.info?handles="+s;
        String[] _ans = new String[3];
        StringRequest request = new StringRequest(Request.Method.GET, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String str = jsonObject.optString("result");
                            Toast.makeText(getContext(), String.valueOf(str), Toast.LENGTH_SHORT).show();
//                            String rating = resultObj.optString("rating");
//                            String rank = resultObj.optString("rank");

//                            _ans[0]=s; _ans[1]=rating; _ans[2]=rank;
//                            Toast.makeText(getContext(), rank, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        return _ans;

    }

    private String[] retrieveCodechefRatings(String s) {
        String[] _ans = new String[3];
        _ans[0]=s; _ans[1]=s; _ans[2]=s;
        return _ans;
    }

    private String[] retrieveLetCodeRatings(String s) {
        String[] _ans = new String[3];
        _ans[0]=s; _ans[1]=s; _ans[2]=s;
        return _ans;
    }

    private void retriveCodechefRatings(String retrievedDatum) {
        String url = "https://www.codechef.com/users/"+retrievedDatum;
//        try {
//            // Fetch the HTML document
//            Document document = Jsoup.connect(url).get();
//
//            // Select elements from the document using CSS selectors
//            Elements elements = document.getElementsByClass("rating-number");
//
//            for (Element element : elements) {
//                String rating = element.text(); // Get the text content of the element
//                cc1.setText(rating);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        HTMLScrapingTask task = new HTMLScrapingTask(new HTMLScrapingTask.HTMLScrapingListener() {
            @Override
            public void onScrapingCompleted(Elements elements) {
                // Process the extracted elements here
                for (Element element : elements) {
                    String rating = element.text();
                }
            }

            @Override
            public void onScrapingFailed(String errorMessage) {

            }
        });

// Execute the task by passing the URL
        task.execute(url);
    }


    private void retriveLeetCodeRatings(String retrievedDatum) {

    }





    private static class HTMLScrapingTask extends AsyncTask<String, Void, Elements> {


        private HTMLScrapingListener listener;

        private interface HTMLScrapingListener {
            void onScrapingCompleted(Elements elements);
            void onScrapingFailed(String errorMessage);
        }

        public HTMLScrapingTask(HTMLScrapingListener listener) {
            this.listener = listener;
        }

        @Override
        protected Elements doInBackground(String... urls) {
            String url = urls[0];
            Elements elements = null;

            try {
                // Fetch the HTML document
                Document document = Jsoup.connect(url).get();

                // Select elements with the class name "rating-number"
                elements = document.getElementsByClass("rating-number");
            } catch (IOException e) {

            }

            return elements;
        }

        @Override
        protected void onPostExecute(Elements elements) {
            if (elements != null) {
                listener.onScrapingCompleted(elements);
            } else {
                listener.onScrapingFailed("Error occurred during HTML scraping");
            }
        }
    }


}