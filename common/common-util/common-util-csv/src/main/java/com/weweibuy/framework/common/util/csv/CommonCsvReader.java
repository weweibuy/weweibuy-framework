package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import de.siegmar.fastcsv.reader.CsvReader;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final Map<Class, ReflectCsvContentConverter> CONVERTER_MAP = new ConcurrentHashMap<>();

    private CsvReader csvReader;

    private Charset charset = CommonConstant.CharsetConstant.UT8;

    private CsvBeanConverter<T> contentConverter;

    public static <T> CsvReaderBuilder<T> builder() {
        return new CsvReaderBuilder<>();
    }


    public void read(Path path) throws IOException {
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
         * 单个字段左右的符号 是否生效
         */
        private Boolean alwaysDelimitText;

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
        private CsvBeanConverter<T> contentConverter;

        /**
         * Java类
         */
        private Class<T> clazz;

        private List<CsvReadListener<T>> readListenerList;


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

        public CsvReaderBuilder<T> alwaysDelimitText(Boolean alwaysDelimitText) {
            this.alwaysDelimitText = alwaysDelimitText;
            return this;
        }


        public CsvReaderBuilder<T> contentConverter(CsvBeanConverter<T> contentConverter) {
            this.contentConverter = contentConverter;
            return this;
        }

        public CsvReaderBuilder<T> contentConverter(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public CsvReaderBuilder<T> readHeader(Boolean readHeader) {
            this.readHeader = readHeader;
            return this;
        }

        public CommonCsvReader build() {
            if (contentConverter == null) {
                throw Exceptions.business("写CSV时, 必须指定contentConverter");
            }

            ReflectCsvBeanConverter<T> beanConverter = new ReflectCsvBeanConverter<>(clazz, csvReadListener);

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

            CommonCsvReader commonCsvReader = new CommonCsvReader();
            commonCsvReader.setCsvReader(csvReader);

            Optional.ofNullable(charset)
                    .ifPresent(commonCsvReader::setCharset);

            return commonCsvReader;
        }

    }


}
