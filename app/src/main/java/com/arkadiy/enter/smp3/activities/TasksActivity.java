package com.arkadiy.enter.smp3.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arkadiy.enter.smp3.dialogs.DialogCloseTask;
import com.arkadiy.enter.smp3.dialogs.DialogDescriptionTask;
import com.arkadiy.enter.smp3.R;
import com.arkadiy.enter.smp3.config.AppConfig;
import com.arkadiy.enter.smp3.dataObjects.Task;
import com.arkadiy.enter.smp3.dataObjects.User;
import com.arkadiy.enter.smp3.services.DataServices;
import com.arkadiy.enter.smp3.utils.Constants;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity implements DialogCloseTask.DialogCloseTaskListener {


    private SwipeMenuListView listViewTask;
    private ArrayList<Task> itemsList;
    private static ArrayList<String> taskNames;
    private ArrayAdapter<Task> adapter;
    private TextView amountOfTasksTextView;
    private Button creatTask;
    private static final String TAG = "TasksActivity";
    private RequestQueue requestQueue;



    private void fillNames(ArrayList<Task> itemsList) {
        for (int i = 0; i < itemsList.size(); i++) {
            taskNames.add(itemsList.get(i).getNameTask());

        }
        this.itemsList = itemsList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        taskNames=new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        creatTask = (Button)findViewById(R.id.creatNewTask_button);
        listViewTask = (SwipeMenuListView) findViewById(R.id.listViewTask);
        amountOfTasksTextView = (TextView)findViewById(R.id.amountOfTasks_TextView);
        itemsList = new ArrayList<>();
        //User.init(TasksActivity.this);

        User.getTasksFromServer(TasksActivity.this,handler->{
            if(handler.getData()!=null){
                fillNames(User.myTasks);
                afterGotTasks();
            }
            return true;
        });


    }
    @Override
    public void applyText(String reason ,String tag,int position) {

        JSONObject taskClose = new JSONObject();
        String url = AppConfig.CLOSE_TASK;
        try {
            //User.closeTask(position);
            taskClose.put("task_id",itemsList.get(position).getIdTask());
            taskClose.put("close_description",itemsList.get(position).getDescription());

            DataServices.sendData(url,taskClose,requestQueue,TasksActivity.this,Constants.METHOD_POST,null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        itemsList.remove(position);
        taskNames.remove(position);
        listViewTask.setAdapter(adapter);
        amountOfTasksTextView.setText("Amount of open tasks: "+Integer.toString(listViewTask.getCount()));
        Toast.makeText(getApplicationContext(),reason,Toast.LENGTH_SHORT).show();
    }


    private void afterGotTasks(){

        adapter = new ArrayAdapter(TasksActivity.this,android.R.layout.simple_list_item_1,taskNames);
        //adapter = new ArrayAdapter(TasksActivity.this,android.R.layout.simple_list_item_1);
        listViewTask.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        amountOfTasksTextView.setText("Amount of open tasks: "+Integer.toString(listViewTask.getCount()));


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xff)));
                // set item width
                openItem.setWidth(210);
                // set item title
                openItem.setTitle("Description");
                openItem.setIcon(R.drawable.ic_description);
                // set item title fontsize
                openItem.setTitleSize(15);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x35, 0x25)));
                // set item width
                deleteItem.setWidth(210);
                deleteItem.setTitle("Close");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(15);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_finish);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listViewTask.setMenuCreator(creator);





        creatTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(TasksActivity.this, AddNewTaskActivity.class);
                startActivity(intent);
            }
        });


        listViewTask.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Log.d(TAG,"onMenuItemClickL click item " + index);
                        DialogDescriptionTask dialogDescriptionTask = new DialogDescriptionTask(itemsList.get(position).getDescription(),itemsList.get(position).getTimeDateStart(),itemsList.get(position).getTimeDateEnd());
                        dialogDescriptionTask.showNow(getSupportFragmentManager(),String.valueOf(itemsList.get(position).getNameTask()));
                        break;
                    case 1:
                        // delete
                        DialogCloseTask dialogCloseTask = new DialogCloseTask(position);
                        dialogCloseTask.show(getSupportFragmentManager(),String.valueOf(itemsList.get(position).getNameTask()));
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }
}
