package ru.dovakun.service;

import ru.dovakun.model.entity.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DetailProcessing {
    public static Double time = 0.0;
    private final List<Worker> workers = new ArrayList<>();
    CSVWriter writer = new CSVWriter();

    public void startProcessing(String filePathXML, String filePathCSV) throws IOException {
        XmlReader xmlReader = new XmlReader(filePathXML);
        writer.writeHeader(filePathCSV);
        List<ProductionCenter> productionCenters = xmlReader.parseXmlPC();
        List<Connection> connections = xmlReader.parseXmlConnection(productionCenters);
        Scenario scenario = xmlReader.parseXmlScenario();

        findChildrenForPC(connections, productionCenters);
        findMainPC(connections, productionCenters, scenario);
        findFinalPC(productionCenters);

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
                writer.writeToCSV(productionCenter, time, filePathCSV);
            }
            System.out.println(time);
            int countDetail = 0;
            for (ProductionCenter productionCenter : productionCenters) {
                countDetail += productionCenter.getDetails().size();
            }
            if (countDetail == 0) {
                start = false;
            }
        }


    }

    private void detailProcess(List<ProductionCenter> productionCenters) {
        time++;
        for (ProductionCenter productionCenter : productionCenters) {
            productionCenter.setBuffer(productionCenter.getDetails().size());
            List<Detail> details = productionCenter.getDetails();
            int countWorker = productionCenter.getWorkers().size();
            for (int i = 0; i < countWorker && i < details.size(); i++) {
                Optional<Detail> detail = Optional.ofNullable(productionCenter.getDetails().get(i));
                if (detail.isPresent()) {
                    double percent = detail.get().getPercent();
                    percent = percent + 1 / productionCenter.getPerformance();
                    detail.get().setPercent(percent);
                    if (percent >= 1) {
                        findWork(workers, productionCenters);
                        Optional<Detail> detail1 = Optional.empty();
                        if (i + countWorker < productionCenter.getDetails().size()) {
                            detail1 = Optional.ofNullable(productionCenter.getDetails().get(i + countWorker));
                        }
                        if (detail1.isPresent()) {
                            detail.get().setPercent(0.0);
                            detail1.get().setPercent(percent-1);
                            if (!productionCenter.getFinal()) {
                                ProductionCenter productionCenterChildren = findPCWithMinEff(productionCenter.getChildren());
                                productionCenterChildren.getDetails().add(detail.get());
                            }
                            productionCenter.getDetails().remove(detail.get());
                            countWorker--;
                            i--;
                            productionCenter.setBuffer(productionCenter.getDetails().size());
                        }else {
                            detail.get().setPercent(0.0);
                            if (!productionCenter.getFinal()) {
                                ProductionCenter productionCenterChildren = findPCWithMinEff(productionCenter.getChildren());
                                productionCenterChildren.getDetails().add(detail.get());
                            }
                            productionCenter.getDetails().remove(detail.get());
                            productionCenter.setBuffer(productionCenter.getDetails().size());
                        }
                    }
                }
            }
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


public void findFinalPC(List<ProductionCenter> productionCenters) {
    for (ProductionCenter productionCenter : productionCenters) {
        if (productionCenter.getChildren().isEmpty()) {
            productionCenter.setFinal(true);
        }
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
