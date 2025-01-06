package ru.dovakun.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.dovakun.model.entity.Connection;
import ru.dovakun.model.entity.Detail;
import ru.dovakun.model.entity.ProductionCenter;
import ru.dovakun.model.entity.Scenario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlReader {
    public String filePath = "C:\\Users\\pante\\Downloads\\untitled4\\pc\\src\\main\\resources\\тестовый сценарий №1 3 сотрудников.xlsx";

    public List<ProductionCenter> parseXmlPC() throws IOException {
        List<ProductionCenter> productionCenters = new ArrayList<ProductionCenter>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)){
            Sheet sheet = workbook.getSheet("ProductionCenter");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                try {
                    ProductionCenter productionCenter = new ProductionCenter();
                    productionCenter.setId(row.getCell(0).getStringCellValue());
                    productionCenter.setName(row.getCell(1).getStringCellValue());
                    productionCenter.setPerformance(row.getCell(2).getNumericCellValue());
                    productionCenter.setMaxWorkersCount((int) row.getCell(3).getNumericCellValue());
                    productionCenters.add(productionCenter);
                }catch (Exception e){
                    continue;
                }
            }
        }
//        for (ProductionCenter productionCenter : productionCenters) {
//            System.out.println(productionCenter);
//        }
    return productionCenters;
    }
    public List<Connection> parseXmlConnection(List<ProductionCenter> productionCenterList) throws IOException {
        List<Connection> connectionList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)){
            Sheet sheet = workbook.getSheet("Connection");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                try {
                    Connection connection = new Connection();
                    String source = row.getCell(0).getStringCellValue();
                    String target = row.getCell(1).getStringCellValue();
                    ProductionCenter sourceCenter = productionCenterList.stream()
                            .filter(center -> center.getName().equals(source) )
                            .findFirst()
                            .orElse(null);
                    ProductionCenter targetCenter = productionCenterList.stream()
                            .filter(center -> center.getName().equals(target))
                            .findFirst()
                            .orElse(null);
                    if (sourceCenter == null || targetCenter == null) continue;
                    connection.setSourceCenter(sourceCenter);
                    connection.setDestCenter(targetCenter);
                    connectionList.add(connection);
                }catch (Exception e){
                    continue;
                }
            }
        }
//        for (Connection connection : connectionList) {
//            System.out.println(connection);
//        }
        return connectionList;
    }
    public Scenario parseXmlScenario() throws IOException {
        Scenario scenario = new Scenario();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)){
            Sheet sheet = workbook.getSheet("Scenario");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                try {
                    scenario.setWorkersCount((int) row.getCell(0).getNumericCellValue());
                    scenario.setDetailsCount((int) row.getCell(1).getNumericCellValue());
//                    System.out.println(scenario);
                    return scenario;
                }catch (Exception e){
                    continue;
                }
            }
        }
        return scenario;
    }

}
