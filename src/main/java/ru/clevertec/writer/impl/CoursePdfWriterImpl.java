package ru.clevertec.writer.impl;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import ru.clevertec.config.ConfigurationYamlManager;
import ru.clevertec.data.CourseDto;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.exception.OutputStreamException;
import ru.clevertec.writer.CoursePdfWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CoursePdfWriterImpl implements CoursePdfWriter {

    private final ConfigurationYamlManager yaml = ConfigurationYamlManager.INSTANCE;

    private final String BACKGROUND = yaml.getProperty("pdf.background_path");

    private final String ARIAL_FONT = yaml.getProperty("pdf.font_path");

    private final String OUTPUT = yaml.getProperty("pdf.output_path");

    private static final String LINE_BREAK = "\n";
    private static final String COURSE_NAME = "Название курса: ";
    private static final String INFO_ABOUT_COURSE = "Информация о курсе: ";
    private static final String COST = "Стоимость: ";
    private static final String BYN = " BYN";
    private static final String DISCOUNT = "Действующая скидка: ";
    private static final String START_DATE = "Дата начала: ";
    private static final String DURATION = "Длительность: ";
    private static final String DAYS = " дней";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void writePdf(List<CourseDto> courses) {
        checkOutput();

        try (PdfReader pdfReader = new PdfReader(BACKGROUND);
             PdfWriter pdfWriter = new PdfWriter(OUTPUT);
             PdfDocument pdfDocument = new PdfDocument(pdfWriter);
             PdfDocument background = new PdfDocument(pdfReader);
             Document document = new Document(pdfDocument)) {

            addBackgroundToPdfDocument(courses, pdfDocument, background);

            for (int i = 0; i < courses.size(); i++) {
                CourseDto course = courses.get(i);
                processDocument(course, document);
                if (courses.size() != i + 1) {
                    document.add(new AreaBreak());
                }
            }

        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }

    @Override
    public void writePdf(CourseDto course) {
        checkOutput();

        try (PdfReader pdfReader = new PdfReader(BACKGROUND);
             PdfWriter pdfWriter = new PdfWriter(OUTPUT);
             PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);
             Document document = new Document(pdfDocument)) {
            processDocument(course, document);

        } catch (IOException e) {
            throw new NotFoundException(e);
        }

    }

    @Override
    public void setDateTimeFormat(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    private void checkOutput() {
        Path outputPath = Path.of(OUTPUT);

        if (Files.notExists(outputPath)) {
            try {
                Files.createDirectory(outputPath.getParent());

            } catch (IOException e) {
                throw new OutputStreamException(e);
            }
        }
    }

    private void processDocument(CourseDto course, Document document) throws IOException {
        PdfFont font = PdfFontFactory.createFont(ARIAL_FONT);
        processParagraph(course, font, document);
    }

    private void addBackgroundToPdfDocument(List<CourseDto> courses, PdfDocument pdfDocument, PdfDocument background) {
        PdfMerger pdfMerger = new PdfMerger(pdfDocument);

        for (int i = 1; i < courses.size() + 1; i++) {
            pdfMerger.merge(background, 1, 1);
        }
    }

    private void processParagraph(CourseDto course, PdfFont font, Document document) {
        Paragraph courseParagraph = buildParagraph(course);

        courseParagraph.setFont(font);
        courseParagraph.setRelativePosition(25f, 150f, 25f, 25f);

        document.add(courseParagraph);
    }

    private Paragraph buildParagraph(CourseDto courseDto) {
        return new Paragraph(COURSE_NAME +
                             courseDto.name() +
                             LINE_BREAK +
                             INFO_ABOUT_COURSE +
                             courseDto.info() +
                             LINE_BREAK +
                             COST +
                             courseDto.cost() +
                             BYN +
                             LINE_BREAK +
                             DISCOUNT +
                             courseDto.discount() +
                             BYN +
                             LINE_BREAK +
                             START_DATE +
                             courseDto.start().format(formatter) +
                             LINE_BREAK +
                             DURATION +
                             courseDto.duration().toDays() +
                             DAYS +
                             LINE_BREAK);
    }


}
