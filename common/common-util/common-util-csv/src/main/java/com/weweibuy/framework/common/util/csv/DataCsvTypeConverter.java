//package com.weweibuy.framework.common.util.csv;
//
//import com.weweibuy.framework.common.core.model.constant.CommonConstant;
//import com.weweibuy.framework.common.core.utils.DateTimeUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Date;
//import java.util.Optional;
//
///**
// * @author durenhao
// * @date 2021/1/15 17:53
// **/
//public class DataCsvTypeConverter implements CsvTypeConverter<Date> {
//
//    private String pattern;
//
//    @Override
//    public String writeConvert(Date date) {
//        return Optional.ofNullable(date)
//                .map(d -> DateTimeUtils.toStringDate(d, pattern))
//                .orElse(StringUtils.EMPTY);
//    }
//
//    @Override
//    public Date readConvert(String value, Class<Date> fieldType, int typeIndex) {
//        if (StringUtils.EMPTY.equals(value)) {
//            return null;
//        }
//        return DateTimeUtils.strToDate(value, pattern);
//    }
//
//    @Override
//    public int typeIndex(Class<Date> fieldType) {
//        return -1;
//    }
//
//    @Override
//    public void setPattern(String pattern) {
//        this.pattern = Optional.ofNullable(pattern)
//                .filter(StringUtils::isNotBlank)
//                .orElse(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR);
//    }
//
//}
