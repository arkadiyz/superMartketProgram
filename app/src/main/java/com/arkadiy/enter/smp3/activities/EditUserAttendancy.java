package com.arkadiy.enter.smp3.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arkadiy.enter.smp3.R;
import com.arkadiy.enter.smp3.dataObjects.GetDate;
import com.arkadiy.enter.smp3.dataObjects.Manager;
import com.arkadiy.enter.smp3.services.GlobalServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class EditUserAttendancy extends AppCompatActivity {

    private EditText editFromTimeEditText;
    private EditText editToTimeEditText;
    private Button  sendFromEditTimeButton;
    private Button  sendEndToEditTimeButton;
    private int in_out;
    private long userId;
    private int startId;
    private int endId;
    private RequestQueue requestQueue;
    private String nextDate;
    private String previousDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_attendancy);
        App.setContext(EditUserAttendancy.this);
        requestQueue = Volley.newRequestQueue(App.getContext());
        userId = Long.parseLong(getIntent().getStringExtra("userId"));
        editFromTimeEditText = (EditText)findViewById(R.id.editFromTime_EditText);
        editToTimeEditText = (EditText)findViewById(R.id.editToTime_EditText);

        sendFromEditTimeButton = (Button)findViewById(R.id.sendFromEditTime_Button);
        sendEndToEditTimeButton = (Button)findViewById(R.id.sendEndToEditTime_Button);


        nextDate = getIntent().getStringExtra("next_date");
        previousDate = getIntent().getStringExtra("prev_date");


        editFromTimeEditText.setText(getIntent().getStringExtra("start"));
        editToTimeEditText.setText(getIntent().getStringExtra("finish"));
        startId = Integer.parseInt(getIntent().getStringExtra("start_id"));
        endId = Integer.parseInt(getIntent().getStringExtra("finish_id"));

        editFromTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetDate.getDate(editFromTimeEditText);

            }
        });

        editToTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDate.getDate(editToTimeEditText);
            }
        });

        sendFromEditTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date dateStart = null;
                Date previous = null;
                if (!editFromTimeEditText.getText().toString().isEmpty()){
                    dateStart = GetDate.getDateFromString(editFromTimeEditText.getText().toString());
                }


                if (previousDate !=null){
                    previous=GetDate.getDateFromString(previousDate);
                }
                if(!editToTimeEditText.getText().toString().isEmpty()  &&!editToTimeEditText.getText().toString().equals("--")){
                    Date dateEnd=GetDate.getDateFromString(editToTimeEditText.getText().toString());
                    if(dateStart.after(dateEnd)) {
                        Toast.makeText(App.getContext(),"Error. Cannot set \"start time\" after \"end time\"",Toast.LENGTH_LONG).show();

                        return;
                    }
                }
                if(previous!=null && dateStart.before(previous)){
                    Toast.makeText(App.getContext(),"Error. The time for \"Shift Start\" you are trying to enter is before the" +
                                    " previous \"Shift End\" time. Please check the shifts of this user and try again"
                            ,Toast.LENGTH_LONG).show();

                    return;
                }


                in_out = 1;
                sendToServer(in_out,editFromTimeEditText.getText().toString(),userId,startId);
                sendFromEditTimeButton.setVisibility(View.GONE);
            }
        });

        sendEndToEditTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Date dateEnd = null;
                Date dateNextDate = null;
                if (!editToTimeEditText.getText().toString().isEmpty()){
                    dateEnd=GetDate.getDateFromString(editToTimeEditText.getText().toString());
                }

                if (previousDate != null){
                    dateNextDate=GetDate.getDateFromString(nextDate);
                }
                if(!editFromTimeEditText.getText().toString().isEmpty() && !editFromTimeEditText.getText().toString().equals("--")){
                    Date dateStart=GetDate.getDateFromString(editFromTimeEditText.getText().toString());
                    if(dateEnd.before(dateStart)) {

                        new SweetAlertDialog(App.getContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Error. Cannot set \"end time\" before \"start time\"")
                                .show();

                        Toast.makeText(App.getContext(),"Error. Cannot set \"end time\" before \"start time\"",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(dateNextDate!=null && dateEnd.after(dateNextDate)){

                    new SweetAlertDialog(App.getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Error. The time for \"Shift End\" you are trying to enter is after the  \"Shift Start\" time \"" +
                                    "                            \"of next shift. Please check the shifts of this user and try again")
                            .show();

                    Toast.makeText(App.getContext(),"Error. The time for \"Shift End\" you are trying to enter is after the  \"Shift Start\" time " +
                            "of next shift. Please check the shifts of this user and try again",Toast.LENGTH_LONG).show();
                    return;
                }
                in_out = 2;
                sendToServer(in_out,editToTimeEditText.getText().toString(),userId,endId);
                sendEndToEditTimeButton.setVisibility(View.GONE);
            }
        });

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbarMine);
        setSupportActionBar(toolbar);
        GlobalServices.addListener(toolbar,this);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            // do something
////            yourView.notifyBackPressed();
//
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    private void sendToServer(int inOrOut ,String dateTime,long forUserId,int idStatus) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date_time",dateTime);
            jsonObject.put("in_out",inOrOut);
            jsonObject.put("user_id",forUserId);
            if (idStatus != -100 ){
                jsonObject.put("id",idStatus);
            }else{
                jsonObject.put("id",-1);
            }

            Manager.sendEditUserAttendancy(requestQueue,jsonObject,userAttendacyHandler->{

                return true;

            });

        } catch (JSONException e) {

            e.printStackTrace();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
}
