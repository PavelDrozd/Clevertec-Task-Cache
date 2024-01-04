package ru.clevertec.controller;

import lombok.extern.slf4j.Slf4j;
import ru.clevertec.controller.command.CourseCommandResolver;
import ru.clevertec.exception.NotFoundException;
import ru.clevertec.exception.OutputStreamException;
import ru.clevertec.listener.WebContextListener;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@WebServlet("/courses")
public class CourseRestController extends HttpServlet {

    private CourseCommandResolver commandResolver;

    @Override
    public void init() throws ServletException {
        commandResolver = WebContextListener.getContext().getBean(CourseCommandResolver.class);
        log.info("SERVLET INIT");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CourseRestController doGet method");
        try {
            String jsonObject;
            if (req.getParameter("id") == null) {
                jsonObject = commandResolver.getAll(req);
            } else {
                jsonObject = commandResolver.get(req);
            }

            setResponseOptions(req, resp);
            writeResponse(resp, jsonObject);

        } catch (NotFoundException e) {
            log.error(e.getMessage());
            resp.setStatus(404);
            writeResponse(resp, "404 Not found");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CourseRestController doPost method");
        try {
            String jsonObject = commandResolver.create(req);

            setResponseOptions(req, resp);
            writeResponse(resp, jsonObject);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CourseRestController doPut method");
        try {
            String jsonObject = commandResolver.update(req);

            setResponseOptions(req, resp);
            writeResponse(resp, jsonObject);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("CourseRestController doDelete method");
        try {
            commandResolver.delete(req);

            setResponseOptions(req, resp);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("SERVLET DESTROY");
        super.destroy();
    }

    private void setResponseOptions(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus((int) req.getAttribute("status"));
    }

    private void writeResponse(HttpServletResponse resp, String jsonObject) {
        PrintWriter out;
        try {
            out = resp.getWriter();
        } catch (IOException e) {
            throw new OutputStreamException(e);
        }
        out.print(jsonObject);
        out.flush();
    }
}
