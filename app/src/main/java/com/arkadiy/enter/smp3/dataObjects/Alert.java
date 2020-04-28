package com.arkadiy.enter.smp3.dataObjects;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Alert {
    private int id;
    private String name;
    private String description;
    private String sendAt; //TimeDate
    private String userNameFrom;
    private int userIdFrom;
    private int userto;
    private List<Integer> toDepartments;
    private JSONObject alert;

    public Alert (JSONObject alert){

        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();


            this.id = alert.getInt("id");
            this.name = alert.getString("name");
            this.description = alert.getString("description");
            this.userIdFrom = alert.getInt("user_id_from");
            this.userNameFrom = Store.getOnlineUserNameById(alert.getInt("user_id_from"));
            this.sendAt = dateFormat.format(date);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public Alert(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSendAt() {
        return sendAt;
    }

    public void setSendAt(String sendAt) {
        this.sendAt = sendAt;
    }

    public int getUserIdFrom() {
        return userIdFrom;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }

    public void setUserIdFrom(int userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public int getUserto() {
        return userto;
    }

    public void setUserto(int userto) {
        this.userto = userto;
    }

    public List<Integer> getToDepartments() {
        return toDepartments;
    }

    public void setToDepartments(List<Integer> toDepartments) {
        this.toDepartments = toDepartments;
    }
}
