package org.example.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelDataProvider {

        public static Object[][] testData(String excelPath, String sheetName) {
            ExcelUtility excel = null;
            try {
                excel = new ExcelUtility(excelPath, sheetName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Get row and column count
            int rowCount = excel.getRowCount();
            int colCount = excel.getColCount();

            // Initialize data array, subtracting 1 from row count to exclude header
            Object[][] data = new Object[rowCount - 1][colCount];

            // Loop through rows and columns to populate the data array
            for (int i = 1; i < rowCount; i++) { // Starting from 1 to skip the header row
                for (int j = 0; j < colCount; j++) {
                    String cellData = excel.getCellData(i, j); // Get cell data for each row/column
                    data[i - 1][j] = cellData; // Store data, adjusting row index
                }
            }

            return data; // Return the populated data array
        }


    public static Map<Integer, Map<String, String>> excelDataMap(String excelPath, String sheetName) {
        Map<Integer, Map<String, String>> excelData = new HashMap<>();
        ExcelUtility excel = null;
        List<String> headerList = new ArrayList<>();
        Map<String, String> rowMap = null;

        try {
            excel = new ExcelUtility(excelPath, sheetName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int rowCount = excel.getRowCount();
        int colCount = excel.getColCount();

        for (int i = 0; i < rowCount; i++) {
            rowMap = new HashMap<>();

            for (int j = 0; j < colCount; j++) {
                String cellData = excel.getCellData(i, j);
                // System.out.print(cellData + " | ");

                if (i == 0) {
                    headerList.add(cellData.trim());
                } else {
                    rowMap.put(headerList.get(j), cellData == null ? "" : cellData.trim());
                }
            }

            if (i != 0) {
                excelData.put(i, rowMap);
            }
        }
        return excelData;
    }


}
