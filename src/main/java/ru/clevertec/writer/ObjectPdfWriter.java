package ru.clevertec.writer;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface ObjectPdfWriter<T> {

    void writePdf(T t);

    void writePdf(List<T> list);

    void setDateTimeFormat(DateTimeFormatter dateFormat);
}
