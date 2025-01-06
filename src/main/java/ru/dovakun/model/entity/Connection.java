package ru.dovakun.model.entity;

public class Connection {
    private ProductionCenter sourceCenter;

    public ProductionCenter getSourceCenter() {
        return sourceCenter;
    }

    public void setSourceCenter(ProductionCenter sourceCenter) {
        this.sourceCenter = sourceCenter;
    }

    public ProductionCenter getDestCenter() {
        return destCenter;
    }

    public void setDestCenter(ProductionCenter destCenter) {
        this.destCenter = destCenter;
    }

    private ProductionCenter destCenter;

    @Override
    public String toString() {
        return "Connection{" +
                "sourceCenter=" + sourceCenter +
                ", destCenter=" + destCenter +
                '}';
    }
}
