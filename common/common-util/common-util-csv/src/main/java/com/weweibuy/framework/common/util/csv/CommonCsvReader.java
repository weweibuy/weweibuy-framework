package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * csv 读取类
 *
 * @author durenhao
 * @date 2023/6/16 15:52
 **/
@Data
public class CommonCsvReader<T> {

    public static final char DEFAULT_FIELD_SEPARATOR = ',';

    /**
     * 转化器
     */
    private static final Map<Class, ReflectCsvWriterConverter> CONVERTER_MAP = new ConcurrentHashMap<>();

    private CsvReader csvReader;

    private Charset charset = CommonConstant.CharsetConstant.UT8;

    private CsvReaderConverter<T> contentConverter;

    public static <T> CsvReaderBuilder<T> builder() {
        return new CsvReaderBuilder<>();
    }


    public List<T> read(Path path) throws IOException {
        CsvContainer csvContainer = csvReader.read(path, charset);
        return convertData(csvContainer);
    }

    public List<T> read(String filePath) throws IOException {
        return read(new File(filePath));
    }

    public List<T> read(File file) throws IOException {
        CsvContainer csvContainer = csvReader.read(file, charset);
        return convertData(csvContainer);
    }

    public List<T> read(InputStream inputStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(inputStream, charset);
        CsvContainer csvContainer = csvReader.read(streamReader);
        return convertData(csvContainer);
    }

    private List<T> convertData(CsvContainer csvContainer) {
        List<CsvRow> rowList = csvContainer.getRows();
        if (CollectionUtils.isEmpty(rowList)) {
            return Collections.emptyList();
        }
        List<String> header = csvContainer.getHeader();

        return rowList.stream()
                .map(row -> contentConverter.convert(header, row))
                .collect(Collectors.toList());
    }


    public static class CsvReaderBuilder<T> {

        /**
         * 字符集
         */
        private Charset charset;

        /**
         * 字段间分隔符
         */
        private Character fieldSeparator;

        /**
         * 单个字段左右的符号
         */
        private Character textDelimiter;

        /**
         * 跳过空行 默认 true
         */
        private Boolean skipEmptyRows;

        /**
         * 字段数量不一是否报错 默认:false
         */
        private Boolean errorOnDifferentFieldCount;


        /**
         * 是否读 表头
         */
        private Boolean readHeader = true;


        /**
         * 转化器
         */
        private CsvReaderConverter<T> contentConverter;


        public CsvReaderBuilder<T> charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public CsvReaderBuilder<T> gbk() {
            this.charset = CommonConstant.CharsetConstant.GBK;
            return this;
        }

        public CsvReaderBuilder<T> fieldSeparator(Character fieldSeparator) {
            this.fieldSeparator = fieldSeparator;
            return this;
        }

        public CsvReaderBuilder<T> textDelimiter(Character textDelimiter) {
            this.textDelimiter = textDelimiter;
            return this;
        }

        public CsvReaderBuilder<T> contentConverter(CsvReaderConverter<T> contentConverter) {
            this.contentConverter = contentConverter;
            return this;
        }

        public CsvReaderBuilder<T> skipEmptyRows(Boolean skipEmptyRows) {
            this.skipEmptyRows = skipEmptyRows;
            return this;
        }

        public CsvReaderBuilder<T> errorOnDifferentFieldCount(Boolean errorOnDifferentFieldCount) {
            this.errorOnDifferentFieldCount = errorOnDifferentFieldCount;
            return this;
        }

        public CsvReaderBuilder<T> contentConverter(Class<T> clazz) {
            return contentConverter(clazz, null);
        }

        public CsvReaderBuilder<T> contentConverter(Class<T> clazz, List<CsvReadListener> csvReadListenerList) {
            this.contentConverter =
                    new ReflectCsvReaderConverter(clazz, csvReadListenerList);
            return this;
        }

        public CsvReaderBuilder<T> readHeader(Boolean readHeader) {
            this.readHeader = readHeader;
            return this;
        }

        public CommonCsvReader<T> build() {
            if (contentConverter == null) {
                throw Exceptions.business("读CSV时, 必须指定contentConverter");
            }

            CsvReader csvReader = new CsvReader();

            Optional.ofNullable(fieldSeparator)
                    .ifPresent(csvReader::setFieldSeparator);

            Optional.ofNullable(readHeader)
                    .ifPresent(csvReader::setContainsHeader);

            Optional.ofNullable(textDelimiter)
                    .ifPresent(csvReader::setTextDelimiter);

            Optional.ofNullable(errorOnDifferentFieldCount)
                    .ifPresent(csvReader::setErrorOnDifferentFieldCount);

            Optional.ofNullable(skipEmptyRows)
                    .ifPresent(csvReader::setSkipEmptyRows);


            CommonCsvReader<T> commonCsvReader = new CommonCsvReader<>();
            commonCsvReader.setCsvReader(csvReader);
            commonCsvReader.setContentConverter(contentConverter);

            Optional.ofNullable(charset)
                    .ifPresent(commonCsvReader::setCharset);

            return commonCsvReader;
        }

    }


}
