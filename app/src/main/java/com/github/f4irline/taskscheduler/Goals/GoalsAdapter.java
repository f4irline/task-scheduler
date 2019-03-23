package com.github.f4irline.taskscheduler.Goals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView goalText;
        public TextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            goalText = (TextView) itemView.findViewById(R.id.goal_name);
            timeText = (TextView) itemView.findViewById(R.id.goal_time);
        }
    }

    private List<Goal> goals;

    public GoalsAdapter(List<Goal> goals) {
        this.goals = goals;
    }

    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View scoresView = inflater.inflate(R.layout.item_goal, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(scoresView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GoalsAdapter.ViewHolder viewHolder, int i) {
        if (goals != null) {
            Goal goal = goals.get(i);
            viewHolder.goalText.setText(goal.goal);
            viewHolder.timeText.setText(String.valueOf(goal.time));
        } else {
            viewHolder.goalText.setText("No goals yet");
            viewHolder.timeText.setText("");
        }
    }

    void setGoals(List<Goal> words){
        goals = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (goals != null) {
            return goals.size();
        } else {
            return 0;
        }
    }
}
