package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import de.siegmar.fastcsv.writer.CsvWriter;
import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * csv 写出类
 *
 * @author durenhao
 * @date 2023/6/16 15:52
 **/
@Data
public class CommonCsvWriter {

    public static final char DEFAULT_FIELD_SEPARATOR = ',';

    /**
     * 转化器
     */
    private static final Map<Class, ReflectCsvWriterConverter> CONVERTER_MAP = new ConcurrentHashMap<>();

    private CsvWriter csvWriter;

    private Charset charset = CommonConstant.CharsetConstant.UT8;

    private Collection<String[]> data;


    public static <T> CsvWriterBuilder<T> builder() {
        return new CsvWriterBuilder<>();
    }


    public void write(Path path) throws IOException {
        csvWriter.write(path, charset, data);
    }


    public void write(String filePath) throws IOException {
        File file = new File(filePath);
        csvWriter.write(file, charset, data);
    }


    public void write(File file) throws IOException {
        checkAndCreateFileDir(file);
        csvWriter.write(file, charset, data);
    }

    public void write(OutputStream outputStream) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
        csvWriter.write(outputStreamWriter, data);
    }

    public void checkAndCreateFileDir(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }
    }


    public static class CsvWriterBuilder<T> {

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
         * 是否写 表头
         */
        private Boolean writerHeader = true;

        /**
         * 换行符
         */
        private char[] lineDelimiter;

        /**
         * 表头
         */
        private String[] header;

        /**
         * 内容
         */
        private List<T> body;

        /**
         * 转化器
         */
        private CsvWriterConverter<T> contentConverter;


        public CsvWriterBuilder<T> charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public CsvWriterBuilder<T> gbk() {
            this.charset = CommonConstant.CharsetConstant.GBK;
            return this;
        }

        public CsvWriterBuilder<T> fieldSeparator(Character fieldSeparator) {
            this.fieldSeparator = fieldSeparator;
            return this;
        }

        public CsvWriterBuilder<T> textDelimiter(Character textDelimiter) {
            this.textDelimiter = textDelimiter;
            return this;
        }

        public CsvWriterBuilder<T> alwaysDelimitText(Boolean alwaysDelimitText) {
            this.alwaysDelimitText = alwaysDelimitText;
            return this;
        }

        public CsvWriterBuilder<T> lineDelimiter(char[] lineDelimiter) {
            this.lineDelimiter = lineDelimiter;
            return this;
        }

        public CsvWriterBuilder<T> header(String[] header) {
            this.header = header;
            return this;
        }

        public CsvWriterBuilder<T> body(List<T> body) {
            this.body = body;
            return this;
        }

        public CsvWriterBuilder<T> contentConverter(CsvWriterConverter<T> contentConverter) {
            this.contentConverter = contentConverter;
            return this;
        }

        public CsvWriterBuilder<T> contentConverter(Class<T> clazz) {
            this.contentConverter = CONVERTER_MAP.computeIfAbsent(clazz, ReflectCsvWriterConverter::new);
            return this;
        }

        public CsvWriterBuilder<T> writerHeader(Boolean writerHeader) {
            this.writerHeader = writerHeader;
            return this;
        }

        public CommonCsvWriter build() {
            if (contentConverter == null) {
                throw Exceptions.business("写CSV时, 必须指定contentConverter");
            }

            CsvWriter csvWriter = new CsvWriter();
            Optional.ofNullable(fieldSeparator)
                    .ifPresent(csvWriter::setFieldSeparator);
            Optional.ofNullable(textDelimiter)
                    .ifPresent(csvWriter::setTextDelimiter);
            Optional.ofNullable(alwaysDelimitText)
                    .ifPresent(csvWriter::setAlwaysDelimitText);
            Optional.ofNullable(lineDelimiter)
                    .ifPresent(csvWriter::setLineDelimiter);
            CommonCsvWriter commonCsvWriter = new CommonCsvWriter();
            commonCsvWriter.setCsvWriter(csvWriter);
            Optional.ofNullable(charset)
                    .ifPresent(commonCsvWriter::setCharset);
            if (Boolean.TRUE.equals(writerHeader) && ArrayUtils.isEmpty(header) && contentConverter instanceof ReflectCsvWriterConverter) {
                header = ((ReflectCsvWriterConverter) contentConverter).getHeader();
            }

            Collection<String[]> data = null;
            if (Boolean.TRUE.equals(writerHeader) && ArrayUtils.isNotEmpty(header)) {
                data = contentConverter.convert(header, body);
            } else {
                data = contentConverter.convert(null, body);
            }

            commonCsvWriter.setData(data);
            return commonCsvWriter;
        }

    }


}
