package com.shree.maulifoods.pojo;

public class TimeSlot {

    private String sr_No, slot_ID,slot_Time;

    public TimeSlot(String sr_No, String slot_ID, String slot_Time) {
        this.sr_No = sr_No;
        this.slot_ID = slot_ID;
        this.slot_Time = slot_Time;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
    }

    public String getSlot_ID() {
        return slot_ID;
    }

    public void setSlot_ID(String slot_ID) {
        this.slot_ID = slot_ID;
    }

    public String getSlot_Time() {
        return slot_Time;
    }

    public void setSlot_Time(String slot_Time) {
        this.slot_Time = slot_Time;
    }
}
