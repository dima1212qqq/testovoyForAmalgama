package ru.dovakun.model.entity;

public class Scenario {
    private Integer workersCount;
    private Integer detailsCount;

    public Scenario() {
    }

    public Scenario(Integer workersCount, Integer detailsCount) {
        this.workersCount = workersCount;
        this.detailsCount = detailsCount;
    }

    public Integer getWorkersCount() {
        return workersCount;
    }

    public void setWorkersCount(Integer workersCount) {
        this.workersCount = workersCount;
    }

    public Integer getDetailsCount() {
        return detailsCount;
    }

    public void setDetailsCount(Integer detailsCount) {
        this.detailsCount = detailsCount;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "workersCount=" + workersCount +
                ", detailsCount=" + detailsCount +
                '}';
    }
}
