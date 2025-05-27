import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelColumnModifier {
    public static void removeColumn(String inputFile, String outputFile, int columnToRemove) {
        try (FileInputStream fis = new FileInputStream(inputFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Get first sheet

            // Iterate through all rows
            for (Row row : sheet) {
                // Shift cells to the left, starting from the column to remove
                for (int cn = columnToRemove; cn < row.getLastCellNum(); cn++) {
                    Cell currentCell = row.getCell(cn);
                    Cell nextCell = row.getCell(cn + 1);

                    if (currentCell == null) {
                        currentCell = row.createCell(cn);
                    }

                    if (nextCell == null) {
                        row.removeCell(currentCell);
                    } else {
                        // Copy the next cell's content and style to the current cell
                        copyCellContent(nextCell, currentCell);
                    }
                }
                // Remove the last cell in the row
                if (row.getLastCellNum() > columnToRemove) {
                    Cell lastCell = row.getCell(row.getLastCellNum() - 1);
                    if (lastCell != null) {
                        row.removeCell(lastCell);
                    }
                }
            }

            // Write the modified workbook to a new file
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                workbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyCellContent(Cell source, Cell destination) {
        // Copy cell style
        if (source.getCellStyle() != null) {
            destination.setCellStyle(source.getCellStyle());
        }

        // Copy cell content based on its type
        switch (source.getCellType()) {
            case STRING:
                destination.setCellValue(source.getStringCellValue());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(source)) {
                    destination.setCellValue(source.getDateCellValue());
                } else {
                    destination.setCellValue(source.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                destination.setCellValue(source.getBooleanCellValue());
                break;
            case FORMULA:
                destination.setCellFormula(source.getCellFormula());
                break;
            case BLANK:
                destination.setBlank();
                break;
            default:
                destination.setBlank();
        }
    }

    public static void main(String[] args) {
        // Example usage
        String inputFile = "input.xlsx";
        String outputFile = "output.xlsx";
        int columnToRemove = 1; // Column B (0-based index)
        
        removeColumn(inputFile, outputFile, columnToRemove);
    }
}