package com.weweibuy.framework.common.util.csv;

import com.weweibuy.framework.common.core.utils.DateTimeUtils;
import de.siegmar.fastcsv.writer.CsvWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 导出CSV 数据
 *
 * @author durenhao
 * @date 2019/8/19 10:42
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvUtils {


    public static <T> void export(String[] header, List<T> body, CsvContentConverter<T> converter, OutputStream outputStream, Charset charset) throws IOException {
        Collection<String[]> collection = converter.convert(header, body);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, charset);
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.write(outputStreamWriter, collection);
    }


    public static String convertDate(Date date) {
        if (date == null) {
            return "-";
        }
        return DateTimeUtils.toStringDate(date);
    }

    public static String convertInt(Integer integer) {
        if (integer == null) {
            return "-";
        }
        return integer + "";
    }


}
