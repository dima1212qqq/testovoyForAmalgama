package ru.dovakun.service;

import ru.dovakun.model.entity.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DetailProcessing {
    public static Double time = 0.0;
    private static Boolean start = true;
    private final List<Worker> workers = new ArrayList<>();

    public void startProcessing() throws IOException {
        XmlReader xmlReader = new XmlReader();

        List<ProductionCenter> productionCenters = xmlReader.parseXmlPC();
        List<Connection> connections = xmlReader.parseXmlConnection(productionCenters);
        Scenario scenario = xmlReader.parseXmlScenario();

        findChildrenForPC(connections, productionCenters);
        findMainPC(connections, productionCenters, scenario);


        for (int i = 0; i < scenario.getWorkersCount(); i++) {
            Worker worker = new Worker();
            worker.isBusy = false;
            workers.add(worker);
        }
        boolean start = true;
        while (start) {
            findWork(workers, productionCenters);
            detailProcess(productionCenters);
            for (ProductionCenter productionCenter : productionCenters) {
                System.out.println(productionCenter);
            }
//            System.out.println(time + "\n");
        }


    }

    private void detailProcess(List<ProductionCenter> productionCenters) {
        time++;
        try {
            for (ProductionCenter productionCenter : productionCenters) {

                int countWorker = productionCenter.getWorkers().size();
                if (!productionCenter.getDetails().isEmpty())
                    for (int i = 0; i < countWorker; i++) {
                        Detail detail = productionCenter.getDetails().get(i);
                        double percent = detail.getPercent();
                        percent = percent + 1 / productionCenter.getPerformance();
                        detail.setPercent(percent);
                        if (percent >= 1) {
                            double newPercent = 1 - percent;
                            try {
                                Detail newDetail = productionCenter.getDetails().get(countWorker + i);
                                if (newDetail != null) {
                                    detail.setPercent(0.0);
                                    newDetail.setPercent(newPercent);
                                    ProductionCenter productionCenterChildren = findPCWithMinEff(productionCenter.getChildren());
                                    productionCenterChildren.getDetails().add(detail);
                                    productionCenter.getDetails().remove(detail);
                                    productionCenter.setBuffer(productionCenter.getBuffer() - 1);
                                    findWork(workers, productionCenters);
                                }
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("На " + productionCenter.getName() + " закончились детали!");
                                if (!productionCenter.getChildren().isEmpty()) {
                                    ProductionCenter productionCenterChildren = findPCWithMinEff(productionCenter.getChildren());
                                    productionCenterChildren.getDetails().add(detail);
                                    productionCenter.getDetails().remove(detail);
                                    productionCenter.setBuffer(productionCenter.getBuffer() - 1);
                                    productionCenterChildren.setBuffer(productionCenterChildren.getBuffer() + 1);
                                    findWork(workers, productionCenters);
                                } else {
                                    productionCenter.setBuffer(productionCenter.getBuffer() - 1);
                                    productionCenter.getDetails().remove(detail);
                                }

                            }

                        }
                    }
            }
        } catch (IndexOutOfBoundsException ignored) {

        }
    }

    private ProductionCenter findPCWithMinEff(List<ProductionCenter> productionCenters) {
        HashMap<ProductionCenter, Double> productionCenterMap = new HashMap<>();
        for (ProductionCenter productionCenter : productionCenters) {
            Double performance = productionCenter.getPerformance();
            Double buffer = productionCenter.getBuffer().doubleValue();
            Double result = performance * buffer;
            productionCenterMap.put(productionCenter, result);
        }
        Map.Entry<ProductionCenter, Double> minEntry = productionCenterMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .orElse(null);

        return minEntry.getKey();
    }


private void findWork(List<Worker> workers, List<ProductionCenter> productionCenters) {
    HashMap<ProductionCenter, Double> productionCenterMap = new HashMap<>();
    List<ProductionCenter> productionCenterList = new ArrayList<>(productionCenters);
    List<Worker> workerList = new ArrayList<>(workers);

    for (ProductionCenter productionCenter : productionCenters) {
        Double performance = productionCenter.getPerformance();
        Double buffer = productionCenter.getBuffer().doubleValue();
        Double result = performance * buffer;
        productionCenterMap.put(productionCenter, result);
    }

    Map.Entry<ProductionCenter, Double> maxEntry = productionCenterMap.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);
    ProductionCenter productionCenter = maxEntry.getKey();
    List<Worker> newWorkerList = new ArrayList<>(workerList);
    for (Worker worker : newWorkerList) {
        if (productionCenter.getMaxWorkersCount() > productionCenter.getWorkers().size()) {
            productionCenter.getWorkers().add(worker);
            worker.setBusy(true);
            if (worker.getWorkCenter() != null) {
                worker.getWorkCenter().getWorkers().remove(worker);
            }
            worker.setWorkCenter(productionCenter);
            workerList.remove(worker);
        }
    }

    productionCenterList.remove(productionCenter);
    workerList.removeIf(worker -> productionCenter.getWorkers().contains(worker));
    if (!workerList.isEmpty()) {
        findWork(workerList, productionCenterList);
    }
}

public void findChildrenForPC(List<Connection> connections, List<ProductionCenter> productionCenters) {
    for (ProductionCenter productionCenter : productionCenters) {
        List<ProductionCenter> children = new ArrayList<>();
        for (Connection connection : connections) {
            if (connection.getSourceCenter().equals(productionCenter)) {
                children.add(connection.getDestCenter());
            }
        }
        productionCenter.setChildren(children);
    }
}

public void findMainPC(List<Connection> connections, List<ProductionCenter> productionCenters, Scenario scenario) {
    Set<ProductionCenter> set = new HashSet<>();
    List<Detail> details = new ArrayList<>();
    for (Connection connection : connections) {
        set.add(connection.getDestCenter());
    }
    for (ProductionCenter productionCenter : productionCenters) {
        if (!set.contains(productionCenter)) {
            productionCenter.setMain(true);
            productionCenter.setBuffer(scenario.getDetailsCount());
            for (int i = 0; i < productionCenter.getBuffer(); i++) {
                details.add(new Detail(productionCenter.getPerformance()));
            }
            productionCenter.setDetails(details);
        }
        System.out.println(productionCenter);
    }
}
}
