package kontesthelperbyamitmaity.example.atask;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String API_URL = "https://kontests.net/api/v1/all";
    List<List<String>> dataList = new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.contest_list_recylerview);

        FetchDataTask fetchDataTask = new FetchDataTask();
        fetchDataTask.execute(API_URL);

        return view;

    }
    private class FetchDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String apiUrl = urls[0];
            String result = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(apiUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();
                }
            } catch (IOException e) {
                Log.e("FetchDataTask", "Error fetching data from API", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    StringBuilder dataBuilder = new StringBuilder();


                    for (int i = 0; i < jsonArray.length(); i++) {

                        List<String> temp = new ArrayList<>();

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                    NowfetchtheUpcomingData();

                } catch (JSONException e) {
                    Log.e("FetchDataTask", "Error parsing JSON response", e);
                }
            } else {

            }
        }
    }
    private void NowfetchtheUpcomingData() {

        UpcomingContestCustomAdapter adapter = new UpcomingContestCustomAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }
}