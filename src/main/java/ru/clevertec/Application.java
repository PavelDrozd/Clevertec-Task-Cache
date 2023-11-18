package ru.clevertec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import ru.clevertec.data.CourseDto;
import ru.clevertec.mapper.factory.MapperFactory;
import ru.clevertec.service.CourseService;
import ru.clevertec.service.factory.ServiceFactory;
import ru.clevertec.validator.ObjectValidator;
import ru.clevertec.validator.impl.CourseDtoValidator;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Application {

    private static final CourseService service = ServiceFactory.INSTANCE.getService(CourseService.class);
    private static final ObjectValidator<CourseDto> validator = CourseDtoValidator.getInstance();
    private static final ObjectMapper jsonMapper = MapperFactory.INSTANCE.getMapper(ObjectMapper.class);
    private static final XmlMapper xmlMapper = MapperFactory.INSTANCE.getMapper(XmlMapper.class);

    public static void main(String[] args) {
        String xml = "<CourseDto><name>Scala developer</name><info>An easy way to become a Scala developer.</info><cost>500</cost><discount>0</discount><start>2023-12-13</start><duration>PT720H</duration></CourseDto>";
        String json = "{\"name\":\"Groovy developer\",\"info\":\"Advanced course for Scala developer.\",\"cost\":3000,\"discount\":400,\"start\":\"2023-11-14\",\"duration\":\"PT2880H\"}";

        CourseDto scalaDev = processXMLAndValidate(xml);
        System.out.println("Scala developer: " + scalaDev);

        CourseDto groovyDev = processJsonAndValidate(json);
        System.out.println("Groovy developer: " + groovyDev);

        UUID scalaDevUuid = service.create(scalaDev);
        System.out.println("Generated UUID of Scala developer: " + scalaDevUuid);

        UUID groovyDevUuid = service.create(groovyDev);
        System.out.println("Generated UUID of Groovy developer: " + groovyDevUuid);

        List<CourseDto> all = service.getAll();
        System.out.println("All courses: " + all);

        CourseDto newScalaDevCourse = new CourseDto("Pro Scala developer", "Pro level of Scala developer.",
                BigDecimal.valueOf(1200), BigDecimal.valueOf(100),
                LocalDate.of(2024, 1, 10), Duration.ofDays(60));

        service.update(scalaDevUuid, newScalaDevCourse);
        System.out.println("Updated Scala developer: " + service.getById(scalaDevUuid));

        service.delete(groovyDevUuid);

        System.out.println("All courses after delete Groovy developer course: " + service.getAll());
    }

    private static CourseDto processXMLAndValidate(String xml) {
        try {
            CourseDto courseDto = xmlMapper.readValue(xml, CourseDto.class);
            validator.validate(courseDto);
            return courseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static CourseDto processJsonAndValidate(String json) {
        try {
            CourseDto courseDto = jsonMapper.readValue(json, CourseDto.class);
            validator.validate(courseDto);
            return courseDto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
