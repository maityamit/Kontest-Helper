package kontesthelperbyamitmaity.example.atask;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UpcomingContestCustomAdapter extends RecyclerView.Adapter<UpcomingContestCustomAdapter.CustomViewHolder> {
    private List<List<String>> dataList; // Replace String with your actual data type

    UpcomingContestCustomAdapter(List<List<String>> dataList) {
        this.dataList = dataList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_contests, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        List<String> data = dataList.get(position);
        holder.problem_name.setText(data.get(0));
        holder.problem_time.setText(data.get(2));

        if(data.get(4).equals("LeetCode")) holder.contest_site.setImageResource(R.drawable.leetcode);
        else if(data.get(4).equals("CodeForces")) holder.contest_site.setImageResource(R.drawable.codeforces);
        else if(data.get(4).equals("CodeChef")) holder.contest_site.setImageResource(R.drawable.codechef);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView problem_name,problem_time;
        public ImageView contest_site;

        public CustomViewHolder(View itemView) {
            super(itemView);
            problem_name = itemView.findViewById(R.id.upcoming_contest_name);
            problem_time = itemView.findViewById(R.id.upcoming_contest_date);
            contest_site = itemView.findViewById(R.id.upcoming_contest_logo);// Replace with the actual view ID from item_layout.xml
        }
    }
}
