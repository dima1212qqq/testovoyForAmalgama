package ru.dovakun.model.entity;

public class Detail {
    private Double timeProcess;
    private Double percent = 0.0;

    @Override
    public String toString() {
        return "Detail{" +
                "timeProcess=" + timeProcess +
                ", percent=" + percent +
                '}';
    }

    public Detail(Double timeProcess) {
        this.timeProcess = timeProcess;
    }

    public Double getTimeProcess() {
        return timeProcess;
    }

    public void setTimeProcess(Double timeProcess) {
        this.timeProcess = timeProcess;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
