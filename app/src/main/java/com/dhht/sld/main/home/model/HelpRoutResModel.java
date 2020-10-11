package com.dhht.sld.main.home.model;

public class HelpRoutResModel {

    public String title = "最佳路线方案";

    private float duration; // 时间

    private float distance; // 距离

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return (int)Math.ceil(duration / 60) + "分钟";
    }

    public String getDistance() {
        String string;
        if (distance >= 1000)
        {
            string = (float)(Math.round((distance / 1000)*100)/100) + "千米";
        }else{
            string = (float)(Math.round(distance*100)/100) + "米";
        }

        return string;
    }
}
