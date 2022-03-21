package com.shree.maulifoods.pojo;

public class DispFrequency {

    private String sr_No, freq_ID,freq_Name,freq_Days;

    public DispFrequency(String sr_No, String freq_ID, String freq_Name, String freq_Days) {
        this.sr_No = sr_No;
        this.freq_ID = freq_ID;
        this.freq_Name = freq_Name;
        this.freq_Days = freq_Days;
    }

    public String getSr_No() {
        return sr_No;
    }

    public void setSr_No(String sr_No) {
        this.sr_No = sr_No;
    }

    public String getFreq_ID() {
        return freq_ID;
    }

    public void setFreq_ID(String freq_ID) {
        this.freq_ID = freq_ID;
    }

    public String getFreq_Name() {
        return freq_Name;
    }

    public void setFreq_Name(String freq_Name) {
        this.freq_Name = freq_Name;
    }

    public String getFreq_Days() {
        return freq_Days;
    }

    public void setFreq_Days(String freq_Days) {
        this.freq_Days = freq_Days;
    }
}
