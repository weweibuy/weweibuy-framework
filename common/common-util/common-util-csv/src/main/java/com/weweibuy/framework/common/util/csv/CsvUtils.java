package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import de.siegmar.fastcsv.writer.CsvWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 导出CSV 数据
 *
 * @author durenhao
 * @date 2019/8/19 10:42
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvUtils {

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


    public static void read(char fieldSeparator, InputStream inputStream) throws IOException {
        CsvReader csvReader = new CsvReader();
        csvReader.setFieldSeparator(fieldSeparator);
        csvReader.setContainsHeader(false);
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            CsvContainer read = csvReader.read(inputStreamReader);
            List<CsvRow> rows = read.getRows();
            CsvRow csvRow = rows.get(0);
        }
        ;
    }


}
