import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelRowFilterByPattern {
    public static void removeRowsWithPattern(String inputFile, String outputFile, int columnIndex, String pattern) {
        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sourceSheet = workbook.getSheetAt(0);
            String sheetName = sourceSheet.getSheetName();
            
            // Create a new sheet
            Sheet newSheet = workbook.createSheet(sheetName + "_temp");
            
            // Keep track of the new row index
            int newRowIndex = 0;
            
            // Copy column widths
            for (int i = 0; i < sourceSheet.getPhysicalNumberOfRows(); i++) {
                Row row = sourceSheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        newSheet.setColumnWidth(j, sourceSheet.getColumnWidth(j));
                    }
                    break; // Once we've copied the widths, we can stop
                }
            }

            // Iterate through all rows in the original sheet
            for (Row row : sourceSheet) {
                boolean shouldCopyRow = true;
                Cell cell = row.getCell(columnIndex);
                
                // Check if this row should be skipped
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains(pattern)) {
                        shouldCopyRow = false;
                    }
                }
                
                // Copy row if it shouldn't be skipped
                if (shouldCopyRow) {
                    Row newRow = newSheet.createRow(newRowIndex++);
                    copyRow(row, newRow, workbook);
                }
            }

            // Remove the original sheet and rename the new one
            workbook.removeSheetAt(workbook.getSheetIndex(sourceSheet));
            workbook.setSheetName(workbook.getSheetIndex(newSheet), sheetName);

            // Write the modified workbook to a new file
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyRow(Row sourceRow, Row destinationRow, Workbook workbook) {
        // Copy row height and properties
        destinationRow.setHeight(sourceRow.getHeight());
        
        // Copy all cells
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell oldCell = sourceRow.getCell(i);
            Cell newCell = destinationRow.createCell(i);
            
            if (oldCell != null) {
                // Copy cell style
                CellStyle newCellStyle = workbook.createCellStyle();
                newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                newCell.setCellStyle(newCellStyle);

                // Copy cell content
                switch (oldCell.getCellType()) {
                    case STRING:
                        newCell.setCellValue(oldCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(oldCell)) {
                            newCell.setCellValue(oldCell.getDateCellValue());
                        } else {
                            newCell.setCellValue(oldCell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        newCell.setCellValue(oldCell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        newCell.setCellFormula(oldCell.getCellFormula());
                        break;
                    case ERROR:
                        newCell.setCellErrorValue(oldCell.getErrorCellValue());
                        break;
                    case BLANK:
                        break;
                    default:
                        break;
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