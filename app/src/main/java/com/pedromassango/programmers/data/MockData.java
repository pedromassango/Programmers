package com.pedromassango.programmers.data;

import com.pedromassango.programmers.models.Notification;

import java.util.ArrayList;

/**
 * Created by Pedro Massango on 2/17/18.
 */

public class MockData {

    public static ArrayList<Notification> notifications;
    {
        notifications = new ArrayList<Notification>();
        notifications.add(new Notification("1", "21323", "Pedro Massango",
                        "1253", "12","Sample tile",System.currentTimeMillis()));
        notifications.add(new Notification("13", "213", "Pedro Massango",
                        "12345", "14","Sample title",System.currentTimeMillis()));
        notifications.add(new Notification("15", "213", "Jose Eduardo",
                        "1223", "16","Sample tittle",System.currentTimeMillis()));
        notifications.add(new Notification("17", "213", "Pedro Massango",
                        "1233", "18","Sample itle",System.currentTimeMillis()));
    }
}
