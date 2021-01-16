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
        return read(csvBeanConverter, DEFAULT_FIELD_SEPARATOR, inputStream, true, charset);
    }

    public static <T> List<T> read(Class<T> clazz, InputStream inputStream, Charset charset) throws IOException {
        return read(clazz, DEFAULT_FIELD_SEPARATOR, inputStream, true, charset);
    }


    public static <T> List<T> read(CsvBeanConverter<T> csvBeanConverter, InputStream inputStream) throws IOException {
        return read(csvBeanConverter, DEFAULT_FIELD_SEPARATOR, inputStream, true, CommonConstant.CharsetConstant.UT8);
    }

    public static <T> List<T> read(CsvBeanConverter<T> csvBeanConverter, char fieldSeparator, InputStream inputStream, boolean containsHeader, Charset charset) throws IOException {
        CsvReader csvReader = new CsvReader();
        csvReader.setFieldSeparator(fieldSeparator);
        csvReader.setContainsHeader(containsHeader);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        CsvContainer csvContainer = csvReader.read(inputStreamReader);
        List<String> header = csvContainer.getHeader();
        Map<String, Integer> headIndexMap = new HashMap<>();
        for (int i = 0; i < header.size(); i++) {
            headIndexMap.put(header.get(i), i);
        }

        List<CsvRow> rowList = csvContainer.getRows();
        if (CollectionUtils.isEmpty(rowList)) {
            return Collections.emptyList();
        }
        return rowList.stream()
                .map(row -> csvBeanConverter.convert(headIndexMap, row))
                .collect(Collectors.toList());
    }


    public static <T> List<T> read(Class<T> clazz, char fieldSeparator, InputStream inputStream, boolean containsHeader, Charset charset) throws IOException {
        CsvReader csvReader = new CsvReader();
        csvReader.setFieldSeparator(fieldSeparator);
        csvReader.setContainsHeader(containsHeader);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
        CsvContainer csvContainer = csvReader.read(inputStreamReader);
        List<String> header = csvContainer.getHeader();
        Map<String, Integer> headIndexMap = new HashMap<>();
        for (int i = 0; i < header.size(); i++) {
            headIndexMap.put(header.get(i), i);
        }
        ReflectCsvBeanConverter<T> beanConverter = new ReflectCsvBeanConverter<>(clazz, headIndexMap);
        List<CsvRow> rowList = csvContainer.getRows();
        if (CollectionUtils.isEmpty(rowList)) {
            return Collections.emptyList();
        }
        return rowList.stream()
                .map(row -> beanConverter.convert(headIndexMap, row))
                .collect(Collectors.toList());
    }

}
