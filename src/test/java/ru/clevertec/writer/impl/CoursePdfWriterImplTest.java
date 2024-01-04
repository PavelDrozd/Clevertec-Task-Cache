package ru.clevertec.writer.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.config.AppConfiguration;
import ru.clevertec.data.CourseDto;
import ru.clevertec.data.CourseDtoTestBuilder;
import ru.clevertec.writer.CoursePdfWriter;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(AppConfiguration.class)
class CoursePdfWriterImplTest {

    @InjectMocks
    @Autowired
    CoursePdfWriter coursePdfWriter;

    @Value("${pdf.path.output}")
    private String PDF_OUTPUT;

    @Test
    void writePdfShouldContainsExpectedString() throws IOException {
        // given
        String expected = """
                Название курса: Java developer
                Информация о курсе: Long time courses for Java developer.
                Стоимость: 2000 BYN
                Действующая скидка: 100 BYN
                Дата начала: 15-10-2023
                Длительность: 180 дней""";
        CourseDto courseDto = CourseDtoTestBuilder.builder().build().buildCourseDto();


        // when
        coursePdfWriter.writePdf(courseDto);

        String actual;
        try (PdfReader pdfReader = new PdfReader(PDF_OUTPUT);
             PdfDocument pdfDocument = new PdfDocument(pdfReader)) {
            actual = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1));
        }

        // then
        assertThat(actual)
                .containsIgnoringWhitespaces(expected);

    }
}