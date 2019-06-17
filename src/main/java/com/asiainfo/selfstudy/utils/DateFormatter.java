package com.asiainfo.selfstudy.utils;

import org.springframework.format.Formatter;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.text.DateFormat;

public class DateFormatter implements Formatter<Date> {

    private String format;
    public DateFormatter(String format) {
        this.format = format;
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        //  需要进行初始化的格式化的操作的。format需要进行转化的。
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(2, 3, locale);
        return dateTimeInstance.parse(text);
    }

    @Override
    public String print(Date date, Locale locale) {
       //  根据地区获取国际化的时间的
        DateFormat dateTimeInstance = DateFormat.getDateTimeInstance(2, 3, locale);
        return dateTimeInstance.format(date);
    }
}
