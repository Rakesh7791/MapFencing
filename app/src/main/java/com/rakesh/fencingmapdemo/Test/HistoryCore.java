package com.rakesh.fencingmapdemo.Test;

import java.util.ArrayList;

public class HistoryCore {
    private int msec;
    private ArrayList<HistoryData> history;

    public int getMsec() {
        return msec;
    }

    public void setMsec(int msec) {
        this.msec = msec;
    }

    public ArrayList<HistoryData> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<HistoryData> history) {
        this.history = history;
    }
}
