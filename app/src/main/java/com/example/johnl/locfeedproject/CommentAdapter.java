package com.example.johnl.locfeedproject;

/**
 * Created by johnl on 4/5/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<CommentModel> {

    private ArrayList<CommentModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView comment;
        TextView user;
        TextView userRep;
        TextView userID;
    }

    //constructor
    public CommentAdapter(ArrayList<CommentModel> data, Context context) {
        super(context, R.layout.row_comment, data);
        this.dataSet = data;
        this.mContext=context;
    }

    private int lastPosition = -1;

    //returns the view used as a row in the ListView at a specfic position.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CommentModel commentModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_comment, parent, false);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.user = (TextView) convertView.findViewById(R.id.user);
            viewHolder.userRep = (TextView) convertView.findViewById(R.id.userRep);
            viewHolder.userID = (TextView) convertView.findViewById(R.id.userID);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.comment.setText(commentModel.getComment());
        viewHolder.user.setText(commentModel.getUser());
        viewHolder.userRep.setText("  " + commentModel.getUserRep());
        viewHolder.userID.setText(commentModel.getUserID());
        viewHolder.comment.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}