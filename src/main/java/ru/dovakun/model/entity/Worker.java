package ru.dovakun.model.entity;

import java.util.Optional;

public class Worker {
    public Boolean isBusy;

    private int workTime;

    public ProductionCenter workCenter;

    public Boolean getBusy() {
        return isBusy;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public ProductionCenter getWorkCenter() {
        return workCenter;
    }

    public void setWorkCenter(ProductionCenter workCenter) {
        this.workCenter = workCenter;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "isBusy=" + isBusy +
                '}';
    }
}
