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

/**
 * The adapter which manages the goal list in the GoalsActivity.
 *
 * @author Tommi Lepola
 * @version 3.0
 * @since 2019.0323
 */
public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {


    /**
     * Describes an item view and metadata about it's place in the RecyclerView.
     *
     * @author Tommi Lepola
     * @version 3.0
     * @since 2019.0323
     */
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

    /**
     * Constructor for the adapter. Initializes the goals that currently exist
     * and the viewModel which is used to manage the database.
     *
     * @param goals list of the goals that currently already exist.
     * @param viewModel the viewModel implementation which is used to manage the database.
     * @param appContext the context where the adapter is used.
     */
    public GoalsAdapter(List<Goal> goals, AppViewModel viewModel, Context appContext) {
        this.goals = goals;
        this.viewModel = viewModel;
        this.appContext = appContext;
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     *
     * @param viewGroup the ViewGroup into which the new view will be added.
     * @param viewType the type of the new view.
     * @return a new ViewHolder which holds a View of the given view type.
     */
    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        deleteAnimation = AnimationUtils.loadAnimation(context, R.anim.scale);
        View scoresView = inflater.inflate(R.layout.item_goal, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(scoresView);
        return viewHolder;
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     *
     * @param viewHolder the ViewHolder which should be updated to represent the contents of the item
     *                   at the given position in the data set.
     * @param position the position of the item in the Adapter's data set.
     */
    @Override
    public void onBindViewHolder(GoalsAdapter.ViewHolder viewHolder, int position) {
        if (goals != null) {
            final Goal goal = goals.get(position);
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

    /**
     * Sets given goals to the goals list and notifies any registered observers that
     * the data set has changed.
     *
     * @param goals the goals that exist when this function is called.
     */
    void setGoals(List<Goal> goals){
        this.goals = goals;
        notifyDataSetChanged();
    }

    /**
     * Returns the number of goals currently in the list.
     *
     * @return the number of goals currently in the list.
     */
    @Override
    public int getItemCount() {

        if (goals != null) {
            return goals.size();
        } else {
            return 0;
        }
    }
}
