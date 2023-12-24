package ru.clevertec.controller.command;

import javax.servlet.http.HttpServletRequest;

public interface CommandResolver {

    String get(HttpServletRequest req);

    String getAll(HttpServletRequest req);

    String create(HttpServletRequest req);

    String update(HttpServletRequest req);

    void delete(HttpServletRequest req);
}
