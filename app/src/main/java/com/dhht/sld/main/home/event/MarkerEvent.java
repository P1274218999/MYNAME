package com.dhht.sld.main.home.event;

public class MarkerEvent {

    private static MarkerEvent event;
    private int find_order_id;
    private int user_help_order_id;

    private MarkerEvent(){}

    public static MarkerEvent getInstance() {
        event = MarkerEventHolder.INSTANCE;
        return event;
    }

    private static class MarkerEventHolder{
        private static MarkerEvent INSTANCE = new MarkerEvent();
    }

    public MarkerEvent setFind_order_id(int find_order_id) {
        this.find_order_id = find_order_id;
        return this;
    }

    public MarkerEvent setUser_help_order_id(int user_help_order_id) {
        this.user_help_order_id = user_help_order_id;
        return this;
    }

    public int getFind_order_id() {
        return find_order_id;
    }

    public int getUser_help_order_id() {
        return user_help_order_id;
    }
}
