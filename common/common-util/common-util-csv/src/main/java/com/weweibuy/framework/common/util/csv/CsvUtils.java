package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.writer.CsvWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * CSV 导入 导出工具
 *
 * @author durenhao
 * @date 2019/8/19 10:42
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvUtils {

    public static final char DEFAULT_FIELD_SEPARATOR = ',';

    private static final Map<Class, ReflectCsvContentConverter> CONVERTER_MAP = new ConcurrentHashMap<>();


    public static <T> void export(String[] header, List<T> body, CsvContentConverter<T> converter, OutputStream outputStream, Charset charset) throws IOException {
        Collection<String[]> collection = converter.convert(header, body);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);

        CsvWriter csvWriter = new CsvWriter();
        csvWriter.write(outputStreamWriter, collection);
    }

    public static <T> void export(String[] header, List<T> body, CsvContentConverter<T> converter, OutputStream outputStream) throws IOException {
        export(header, body, converter, outputStream, CommonConstant.CharsetConstant.UT8);
    }

    public static <T> void export(List<T> body, Class<T> clazz, OutputStream outputStream) throws IOException {
        ReflectCsvContentConverter<T> converter = csvContentConverter(clazz);
        export(null, body, converter, outputStream, CommonConstant.CharsetConstant.UT8);
    }

    public static <T> void export(List<T> body, Class<T> clazz, OutputStream outputStream, Charset charset) throws IOException {
        ReflectCsvContentConverter<T> converter = csvContentConverter(clazz);
        export(null, body, converter, outputStream, charset);
    }

    public static <T> void export(String[] header, List<T> body, CsvContentConverter<T> converter, File file, Charset charset) throws IOException {
        Collection<String[]> collection = converter.convert(header, body);
        try (OutputStream out = new FileOutputStream(file)) {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, charset);
            CsvWriter csvWriter = new CsvWriter();
            csvWriter.write(outputStreamWriter, collection);
        }
    }

    public static <T> void export(String[] header, List<T> body, CsvContentConverter<T> converter, File file) throws IOException {
        export(header, body, converter, file, CommonConstant.CharsetConstant.UT8);
    }

    public static <T> void export(List<T> body, Class<T> clazz, File file) throws IOException {
        ReflectCsvContentConverter<T> converter = csvContentConverter(clazz);
        export(null, body, converter, file, CommonConstant.CharsetConstant.UT8);
    }

    public static <T> void export(List<T> body, Class<T> clazz, File file, Charset charset) throws IOException {
        ReflectCsvContentConverter<T> converter = csvContentConverter(clazz);
        export(null, body, converter, file, charset);
    }


    public static <T> ReflectCsvContentConverter<T> csvContentConverter(Class<T> clazz) {
        return CONVERTER_MAP.computeIfAbsent(clazz, key -> new ReflectCsvContentConverter(key));
    }


    public static <T> List<T> read(CsvBeanConverter<T> csvBeanConverter, InputStream inputStream, Charset charset) throws IOException {
        return read(csvBeanConverter, inputStream, charset, DEFAULT_FIELD_SEPARATOR, true);
    }


    public static <T> List<T> read(CsvBeanConverter<T> csvBeanConverter, CsvReader csvReader, InputStreamReader inputStreamReader) throws IOException {
        CsvContainer csvContainer = csvReader.read(inputStreamReader);
        List<String> header = csvContainer.getHeader();

        List<CsvRow> rowList = csvContainer.getRows();
        if (CollectionUtils.isEmpty(rowList)) {
            return Collections.emptyList();
        }

        Map<String, Integer> headIndexMap = headIndexMap(header);

        return rowList.stream()
                .map(row -> csvBeanConverter.convert(headIndexMap, row))
                .collect(Collectors.toList());
    }


    public static <T> List<T> read(CsvBeanConverter<T> csvBeanConverter, InputStream inputStream, Charset charset, char fieldSeparator, boolean containsHeader) throws IOException {
        CsvReader csvReader = csvReader(fieldSeparator, containsHeader, null, null, null);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        return read(csvBeanConverter, csvReader, inputStreamReader);
    }

    public static <T> List<T> read(CsvBeanConverter<T> csvBeanConverter, InputStream inputStream) throws IOException {
        CsvReader csvReader = csvReader(null, true, null, null, null);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, CommonConstant.CharsetConstant.UT8);
        return read(csvBeanConverter, csvReader, inputStreamReader);
    }


    public static <T> List<T> read(Class<T> clazz, InputStream inputStream) throws IOException {
        return read(clazz, inputStream, CommonConstant.CharsetConstant.UT8, DEFAULT_FIELD_SEPARATOR, true);
    }

    public static <T> List<T> read(Class<T> clazz, InputStream inputStream, Boolean containsHeader) throws IOException {
        return read(clazz, inputStream, CommonConstant.CharsetConstant.UT8, DEFAULT_FIELD_SEPARATOR, containsHeader);
    }

    public static <T> List<T> read(Class<T> clazz, InputStream inputStream, Charset charset, Boolean containsHeader) throws IOException {
        return read(clazz, inputStream, charset, DEFAULT_FIELD_SEPARATOR, containsHeader);
    }

    public static <T> List<T> read(Class<T> clazz, InputStream inputStream, Charset charset) throws IOException {
        return read(clazz, inputStream, charset, DEFAULT_FIELD_SEPARATOR, true);
    }


    public static <T> List<T> read(Class<T> clazz, CsvReader csvReader, InputStreamReader inputStreamReader, CsvReadListener<T> csvReadListener) throws IOException {
        CsvContainer csvContainer = csvReader.read(inputStreamReader);
        List<String> header = csvContainer.getHeader();
        List<CsvRow> rowList = csvContainer.getRows();
        if (CollectionUtils.isEmpty(rowList)) {
            return Collections.emptyList();
        }

        Map<String, Integer> headIndexMap = headIndexMap(header);

        ReflectCsvBeanConverter<T> beanConverter = new ReflectCsvBeanConverter<>(clazz, csvReadListener, headIndexMap);

        return rowList.stream()
                .map(row -> beanConverter.convert(headIndexMap, row))
                .collect(Collectors.toList());
    }


    public static <T> List<T> read(Class<T> clazz, InputStream inputStream, Charset charset, Character fieldSeparator, Boolean containsHeader) throws IOException {
        CsvReader csvReader = csvReader(fieldSeparator, containsHeader, null, null, null);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        return read(clazz, csvReader, inputStreamReader, null);
    }


    private static Map<String, Integer> headIndexMap(List<String> header) {
        Map<String, Integer> headIndexMap = new HashMap<>();
        if (CollectionUtils.isEmpty(header)) {
            return headIndexMap;
        }
        for (int i = 0; i < header.size(); i++) {
            headIndexMap.put(header.get(i), i);
        }
        return headIndexMap;
    }


    public static CsvReader csvReader(Character fieldSeparator, Boolean containsHeader, Character textDelimiter, Boolean errorOnDifferentFieldCount, Boolean skipEmptyRows) {
        CsvReader csvReader = new CsvReader();
        Optional.ofNullable(fieldSeparator)
                .ifPresent(csvReader::setFieldSeparator);

        Optional.ofNullable(containsHeader)
                .ifPresent(csvReader::setContainsHeader);

        Optional.ofNullable(textDelimiter)
                .ifPresent(csvReader::setTextDelimiter);

        Optional.ofNullable(errorOnDifferentFieldCount)
                .ifPresent(csvReader::setErrorOnDifferentFieldCount);

        Optional.ofNullable(skipEmptyRows)
                .ifPresent(csvReader::setSkipEmptyRows);

        return csvReader;
    }


}
