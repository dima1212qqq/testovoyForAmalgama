package ru.dovakun.service;

import ru.dovakun.model.entity.ProductionCenter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {

    public void writeToCSV(ProductionCenter pc, double time, String filePathCSV) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathCSV, true))) {
            writer.write(time + ", " + pc.getName() + ", " + pc.getWorkers().size() + ", " + pc.getDetails().size());
            writer.newLine();
        }
    }

    public void writeHeader(String filePathCSV) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathCSV, false))) {
            writer.write("Time, ProductionCenter, WorkersCount, BufferCount");
            writer.newLine();
        }

    }
}
