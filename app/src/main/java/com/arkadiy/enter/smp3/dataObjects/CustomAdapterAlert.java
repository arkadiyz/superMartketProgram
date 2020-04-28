package com.arkadiy.enter.smp3.dataObjects;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arkadiy.enter.smp3.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapterAlert extends ArrayAdapter<Alert> {


    private List<Alert>alerts;
    private Activity context;
    private boolean hideTextView = true;


    public CustomAdapterAlert(Activity context, List<Alert>alert ,boolean hideTextView) {
        super(context, R.layout.customlayout,alert);
        this.context = context;
        this.alerts = alert;
        this.hideTextView = hideTextView;

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.customlayout,null,true);
            viewHolder =new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }


        viewHolder.userNameTextView.setText(alerts.get(position).getName());
        if (!hideTextView) {
            viewHolder.sendByTextView.setText("Sent from: "+alerts.get(position).getUserNameFrom());
            viewHolder.whenToSendTextView.setText("Sent at: "+alerts.get(position).getSendAt());
        }

        viewHolder.imageView.setVisibility(View.VISIBLE);

//        viewHolder.idTextView.setText(alerts.get(position).getDescription());



        return r;
    }

    class ViewHolder{

        TextView userNameTextView;
        TextView sendByTextView;
        TextView whenToSendTextView;
        ImageView imageView;

        ViewHolder(View v ){
            userNameTextView = (TextView) v.findViewById(R.id.alertName_textView);
            imageView=(ImageView)v.findViewById(R.id.imageCustom);
            if (!hideTextView){
                sendByTextView = (TextView) v.findViewById(R.id.send_by_textView);
                whenToSendTextView = (TextView) v.findViewById(R.id.when_to_send_textView);
            }


        }

    }

}
