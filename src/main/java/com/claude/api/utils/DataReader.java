package com.claude.api.utils;

import com.claude.api.exceptions.FrameworkException;
import com.fasterxml.jackson.databind.JavaType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data-driven test data loader supporting JSON, CSV, and Excel fixtures from
 * the classpath. Centralizing format-specific parsing here keeps test
 * classes free of I/O and library-specific concerns.
 */
public final class DataReader {

    private DataReader() {
    }

    /**
     * Reads a JSON array from the classpath into a list of the given type.
     *
     * @param classpathLocation the classpath-relative resource path
     * @param elementType       the element type to deserialize each entry into
     * @param <T>                the element type parameter
     * @return the deserialized list
     */
    public static <T> List<T> readJson(final String classpathLocation, final Class<T> elementType) {
        try (InputStream stream = openStream(classpathLocation)) {
            final JavaType listType = JsonUtils.mapper().getTypeFactory()
                    .constructCollectionType(List.class, elementType);
            return JsonUtils.mapper().readValue(stream, listType);
        } catch (IOException ex) {
            throw new FrameworkException("Failed to read JSON test data: " + classpathLocation, ex);
        }
    }

    /**
     * Reads a CSV file from the classpath into a list of header-keyed row
     * maps.
     *
     * @param classpathLocation the classpath-relative resource path
     * @return one map per row, keyed by column header
     */
    public static List<Map<String, String>> readCsv(final String classpathLocation) {
        try (InputStream stream = openStream(classpathLocation);
             Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            final List<Map<String, String>> rows = new ArrayList<>();
            for (CSVRecord record : parser) {
                rows.add(new LinkedHashMap<>(record.toMap()));
            }
            return rows;
        } catch (IOException ex) {
            throw new FrameworkException("Failed to read CSV test data: " + classpathLocation, ex);
        }
    }

    /**
     * Reads the first sheet of an Excel workbook from the classpath into a
     * list of header-keyed row maps. The first row is treated as the header.
     *
     * @param classpathLocation the classpath-relative resource path
     * @return one map per data row, keyed by column header
     */
    public static List<Map<String, String>> readExcel(final String classpathLocation) {
        try (InputStream stream = openStream(classpathLocation);
             Workbook workbook = WorkbookFactory.create(stream)) {

            final Sheet sheet = workbook.getSheetAt(0);
            final Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            final List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            final List<Map<String, String>> rows = new ArrayList<>();
            for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                final Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                final Map<String, String> rowData = new LinkedHashMap<>();
                for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                    final Cell cell = row.getCell(colIndex);
                    rowData.put(headers.get(colIndex), cell == null ? "" : cellAsString(cell));
            }
            rows.add(rowData);
            }
            return rows;
        } catch (IOException ex) {
            throw new FrameworkException("Failed to read Excel test data: " + classpathLocation, ex);
        }
    }

    private static String cellAsString(final Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private static InputStream openStream(final String classpathLocation) {
        final InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(classpathLocation);
        if (stream == null) {
            throw new FrameworkException("Test data file not found on classpath: " + classpathLocation);
        }
        return stream;
    }
}
