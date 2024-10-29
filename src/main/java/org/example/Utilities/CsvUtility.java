package org.example.Utilities;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtility {

    private static final ReadProperty readProperty = ReadProperty.getInstance();
    public static final String attachment = readProperty.readProperties("attachmentPath");

    public static List<String> getCsvData(String fileName) {
        // Path to your CSV file
        String csvFilePath = System.getProperty("user.dir") + attachment + "/" + fileName;
        List<String> headerList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] header = reader.readNext(); // Read the header row (column names)
            if (header != null) {
                // Print column names
                for (String columnName : header) {
                    System.out.println(columnName); // Printing column names
                }

                // Read data rows
                String[] rowData;
                while ((rowData = reader.readNext()) != null) {
                    // Print data in each row
                    for (String data : rowData) {
                        headerList.add(data);
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return headerList;
    }


    public static void setCSVData(String fileName, int columnNumber, String columnValue) {
        String csvFilePath = System.getProperty("user.dir") + attachment + "/" + fileName;
        try {
            // Read existing data from the CSV file
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            // Modify specific column values
            for (int i = 1; i < lines.size(); i++) {
                String[] columns = lines.get(i).split(",");
                columns[columnNumber] = columnValue;
                lines.set(i, String.join(",", columns));
            }

            // Write modified data back to the CSV file
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath));
            for (String modifiedLine : lines) {
                writer.write(modifiedLine);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
