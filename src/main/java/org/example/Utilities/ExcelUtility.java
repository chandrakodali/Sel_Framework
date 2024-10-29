package org.example.Utilities;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtility {
    public static XSSFWorkbook excelBook;
    public static XSSFSheet excelSheet;
    public static XSSFRow row;
    public static XSSFCell cell;

    public ExcelUtility(String path, String sheetName) throws Exception {
        try {
            FileInputStream excelFile = new FileInputStream(path);
            excelBook = new XSSFWorkbook(excelFile);
            excelSheet = excelBook.getSheet(sheetName);
        } catch (Exception e) {
            throw (e);
        }
    }

    public int getRowCount() {
        int rowCount = 0;
        try {
            rowCount = excelSheet.getPhysicalNumberOfRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    public int getColCount() {
        int colCount = 0;
        try {
            colCount = excelSheet.getRow(0).getPhysicalNumberOfCells();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return colCount;
    }

    public String getCellDataString(int rowNum, int colNum) {
        String cellData = "";
        try {
            cell = excelSheet.getRow(rowNum).getCell(colNum);
            cellData = cell.getStringCellValue();
        } catch (Exception e) {
            throw e;
        }
        return cellData;
    }

    public int getCellDataNumeric(int rowNum, int colNum) {
        try {
            cell = excelSheet.getRow(rowNum).getCell(colNum);
            int cellData = (int) cell.getNumericCellValue();
            return cellData;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getCellData(int rowNum, int colNum) {
        String cellData = null; // Initialize cellData to null
        try {
            cell = excelSheet.getRow(rowNum).getCell(colNum, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            if (cell == null || cell.getCellType() == CellType.BLANK) {
                return "";
            } else if (cell.getCellType() == CellType.NUMERIC) {
                DataFormatter dataFormatter = new DataFormatter();
                cellData = dataFormatter.formatCellValue(cell);
                return cellData;
            } else if (cell.getCellType() == CellType.STRING) {
                cellData = cell.getStringCellValue();
                return cellData;
            } else {
                return ""; // Return empty string for other cell types
            }
        } catch (Exception e) {
            throw e; // Rethrow the exception
        }
    }


    // Method to set the value in a cell in an excel
    public void setCellData(String result, int rowNum, int colNum) throws Exception {
        try {
            excelSheet.createRow(rowNum).createCell(colNum).setCellValue(result);
            FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "/Excel/Output Data.xlsx");
            excelBook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            throw e; // Rethrow the exception
        }
    }




}
