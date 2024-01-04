package ru.clevertec.controller.command.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import ru.clevertec.controller.command.CourseCommandResolver;
import ru.clevertec.data.CourseDto;
import ru.clevertec.data.Paging;
import ru.clevertec.exception.InputStreamException;
import ru.clevertec.exception.ValidationException;
import ru.clevertec.pagination.Pagination;
import ru.clevertec.reader.DataInputStreamReader;
import ru.clevertec.service.CourseService;
import ru.clevertec.validator.impl.CourseDtoValidator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CourseCommandResolverImpl implements CourseCommandResolver {

    private final CourseService courseService;

    private final ObjectMapper objectMapper;

    private final CourseDtoValidator validator;

    private final DataInputStreamReader dataInputStreamReader;

    private final Pagination pagination;

    @Override
    public String get(HttpServletRequest req) {
        log.debug("Course get command");

        UUID id = getUuidFromRequest(req);

        CourseDto courseDto = courseService.getById(id);

        req.setAttribute("course", courseDto);
        req.setAttribute("status", 200);

        return writeJson(courseDto);
    }

    @Override
    public String getAll(HttpServletRequest req) {
        log.debug("Course get all command");
        Paging paging = getPaging(req);

        List<CourseDto> courses = courseService.getAll(paging.limit(), paging.offset());

        req.setAttribute("courses", courses);
        req.setAttribute("page", paging.page());
        req.setAttribute("total", paging.totalPages());
        req.setAttribute("status", 200);

        return writeJson(courses);
    }

    @Override
    public String create(HttpServletRequest req) {
        log.debug("Course create command");

        String courseJson = readJsonFromRequest(req);
        CourseDto courseDto = readJson(courseJson);

        validator.validate(courseDto);

        UUID createdId = courseService.create(courseDto);

        req.setAttribute("id", createdId);
        req.setAttribute("status", 201);

        return writeJson(createdId);
    }

    @Override
    public String update(HttpServletRequest req) {
        log.debug("Course update command");

        UUID id = getUuidFromRequest(req);
        String courseJson = readJsonFromRequest(req);
        CourseDto courseDto = readJson(courseJson);

        validator.validate(courseDto);

        CourseDto updated = courseService.update(id, courseDto);
        req.setAttribute("id", updated);
        req.setAttribute("status", 200);

        return writeJson(updated);
    }

    @Override
    public void delete(HttpServletRequest req) {
        log.debug("Course delete command");

        UUID id = getUuidFromRequest(req);
        courseService.delete(id);

        req.setAttribute("status", 204);
    }

    private static UUID getUuidFromRequest(HttpServletRequest req) {
        return UUID.fromString(req.getParameter("id"));
    }

    private String readJsonFromRequest(HttpServletRequest req) {
        String json;
        try {
            json = dataInputStreamReader.getString(req.getInputStream());
        } catch (IOException e) {
            throw new InputStreamException(e);
        }
        return json;
    }

    private CourseDto readJson(String courseJson) {
        try {
            return objectMapper.readValue(courseJson, CourseDto.class);
        } catch (JsonProcessingException e) {
            throw new ValidationException(e);
        }
    }

    private String writeJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ValidationException(e);
        }
    }

    private Paging getPaging(HttpServletRequest req) {
        long limit = getLongFromString(req.getParameter("pagesize"));
        long offset = getLongFromString(req.getParameter("offset"));
        long page = getLongFromString(req.getParameter("page"));
        long totalEntities = courseService.count();

        return pagination.getPaging(limit, offset, page, totalEntities);
    }

    private long getLongFromString(String value) {
        if (value == null) {
            return 0;
        } else {
            return Long.parseLong(value);
        }
    }
}
