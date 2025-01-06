package ru.dovakun.model.entity;

import java.util.ArrayList;
import java.util.List;

public class ProductionCenter {
    private String id;
    private String name;
    private Double performance;
    private Integer maxWorkersCount;
    private Integer buffer = 0;
    private List<Detail> details = new ArrayList<>();
    private List<ProductionCenter> children;
    private Boolean isMain = false;
    private List<Worker> workers = new ArrayList<>();
    private Boolean isFinal = false;

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }


    public ProductionCenter() {
    }


    public Integer getBuffer() {
        return buffer;
    }

    public void setBuffer(Integer buffer) {
        this.buffer = buffer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPerformance() {
        return performance;
    }

    public void setPerformance(Double performance) {
        this.performance = performance;
    }

    public Integer getMaxWorkersCount() {
        return maxWorkersCount;
    }

    public void setMaxWorkersCount(Integer maxWorkersCount) {
        this.maxWorkersCount = maxWorkersCount;
    }

    @Override
    public String toString() {
        return "ProductionCenter{" +
                "id='" + id + '\'' +
                ", performance=" + performance +
                ", maxWorkersCount=" + maxWorkersCount +
                ", buffer=" + buffer +
                ", children=" + children.size() +
                ", isMain=" + isMain +
                ", name='" + name + '\'' +
                ", details=" + details.size() +
                ", workers=" + workers.size() +
                '}';
    }

    public List<ProductionCenter> getChildren() {
        return children;
    }

    public void setChildren(List<ProductionCenter> children) {
        this.children = children;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public Boolean getFinal() {
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }
}
