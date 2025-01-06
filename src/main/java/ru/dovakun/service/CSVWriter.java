package ru.dovakun.service;

import ru.dovakun.model.entity.ProductionCenter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    String fileName = "C:\\Users\\pante\\Downloads\\untitled4\\pc\\output.csv";

    public void writeToCSV(ProductionCenter pc, double time) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(time + ", " + pc.getName() + ", " + pc.getWorkers().size() + ", " + pc.getDetails().size());
            writer.newLine();
        }
    }

    public void writeHeader() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            writer.write("Time, ProductionCenter, WorkersCount, BufferCount");
            writer.newLine();
        }

    }
}
