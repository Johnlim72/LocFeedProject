package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class QuestionAdapter extends ArrayAdapter<QuestionModel> implements View.OnClickListener{

    private ArrayList<QuestionModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView question;
        TextView user;
        TextView userRep;
    }

    public QuestionAdapter(ArrayList<QuestionModel> data, Context context) {
        super(context, R.layout.row_question, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        QuestionModel questionModel =(QuestionModel)object;

        switch (v.getId())
        {
            case R.id.question:
                Snackbar.make(v, "question:" + questionModel.getQuestion(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        QuestionModel questionModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_question, parent, false);
            viewHolder.question = (TextView) convertView.findViewById(R.id.question);
            viewHolder.user = (TextView) convertView.findViewById(R.id.user);
            viewHolder.userRep = (TextView) convertView.findViewById(R.id.userRep);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.question.setText(questionModel.getQuestion());
        viewHolder.user.setText(questionModel.getUser());
        viewHolder.userRep.setText("  " + questionModel.getUserRep());
        viewHolder.question.setOnClickListener(this);
        viewHolder.question.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}