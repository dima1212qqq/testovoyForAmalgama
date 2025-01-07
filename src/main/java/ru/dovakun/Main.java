package ru.dovakun;


import ru.dovakun.model.entity.Connection;
import ru.dovakun.model.entity.ProductionCenter;
import ru.dovakun.model.entity.Scenario;
import ru.dovakun.service.DetailProcessing;
import ru.dovakun.service.XmlReader;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        DetailProcessing detailProcessing = new DetailProcessing();
        detailProcessing.startProcessing("Путь до XML","Путь до CSV");
    }
}
