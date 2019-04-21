package com.github.f4irline.taskscheduler.Goals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.f4irline.taskscheduler.AppViewModel;
import com.github.f4irline.taskscheduler.HoursDialogFragment;
import com.github.f4irline.taskscheduler.R;
import com.github.f4irline.taskscheduler.TimerTools.TimerService;

import java.math.BigDecimal;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView goalText;
        public TextView timeText;
        public TextView goalDone;
        public ImageButton removeButton;
        public ProgressBar goalProgress;
        public LinearLayout goalWrapper;

        public ViewHolder(View itemView) {
            super(itemView);
            goalText = (TextView) itemView.findViewById(R.id.goal_name);
            timeText = (TextView) itemView.findViewById(R.id.goal_time);
            goalDone = (TextView) itemView.findViewById(R.id.goal_done);
            removeButton = (ImageButton) itemView.findViewById(R.id.remove_goal);
            goalProgress = (ProgressBar) itemView.findViewById(R.id.goal_progress);
            goalWrapper = (LinearLayout) itemView.findViewById(R.id.goal_item_wrapper);
        }
    }

    private List<Goal> goals;
    private AppViewModel viewModel;
    private Context appContext;
    private Animation deleteAnimation;

    public GoalsAdapter(List<Goal> goals, AppViewModel viewModel, Context appContext) {
        this.goals = goals;
        this.viewModel = viewModel;
        this.appContext = appContext;
    }

    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        deleteAnimation = AnimationUtils.loadAnimation(context, R.anim.scale);
        View scoresView = inflater.inflate(R.layout.item_goal, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(scoresView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GoalsAdapter.ViewHolder viewHolder, int i) {
        if (goals != null) {
            final Goal goal = goals.get(i);
            viewHolder.goalText.setText(goal.goal);
            viewHolder.timeText.setText(String.valueOf(goal.goalTime)+"h");

            float roundedGoal = BigDecimal.valueOf(goal.goalDone).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            viewHolder.goalDone.setText(String.valueOf(roundedGoal)+"h");

            float ratio = (goal.goalDone / goal.goalTime) * 100;
            viewHolder.goalProgress.setProgress((int) ratio, true);
            viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if (goals.get(HoursDialogFragment.chosenItem).getGoalId() == goal.getGoalId()) {
                       Intent intent = new Intent(appContext, TimerService.class);
                       appContext.stopService(intent);
                   }
                   viewHolder.goalWrapper.startAnimation(deleteAnimation);
                   deleteAnimation.setAnimationListener(new Animation.AnimationListener() {
                       @Override
                       public void onAnimationStart(Animation animation) {

                       }

                       @Override
                       public void onAnimationEnd(Animation animation) {
                           animation.setFillAfter(true);
                           viewModel.delete(goal);
                       }

                       @Override
                       public void onAnimationRepeat(Animation animation) {

                       }
                   });
               }
            });
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
