import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelRowRemover {
    public static void removeRowsWithPattern(String inputFile, String outputFile, int columnIndex, String pattern) {
        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Get first sheet
            
            // Find rows to remove (store indices in reverse order)
            List<Integer> rowsToRemove = new ArrayList<>();
            for (Row row : sheet) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains(pattern)) {
                        rowsToRemove.add(row.getRowNum());
                    }
                }
            }
            
            // Sort in reverse order to avoid shifting issues
            Collections.sort(rowsToRemove, Collections.reverseOrder());
            
            // Remove the rows
            for (int rowIndex : rowsToRemove) {
                removeRow(sheet, rowIndex);
            }

            // Write the modified workbook to a new file
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }

            System.out.println("Removed " + rowsToRemove.size() + " rows containing \"" + pattern + "\"");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex <= lastRowNum) {
            if (rowIndex < lastRowNum) {
                // Shift rows up
                sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
            } else {
                // If it's the last row, simply remove it
                Row removingRow = sheet.getRow(rowIndex);
                if (removingRow != null) {
                    sheet.removeRow(removingRow);
                }
            }
        }
    }

    public static void main(String[] args) {
        String inputFile = "input.xlsx";
        String outputFile = "output.xlsx";
        int columnIndex = 12; // Column M (0-based index)
        String pattern = "[Hello]";
        
        removeRowsWithPattern(inputFile, outputFile, columnIndex, pattern);
    }
}