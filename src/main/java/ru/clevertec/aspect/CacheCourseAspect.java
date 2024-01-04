package ru.clevertec.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.clevertec.cache.Cache;
import ru.clevertec.entity.Course;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CacheCourseAspect {

    private final Cache<UUID, Course> cache;

    @Pointcut("@annotation(ru.clevertec.aspect.Get)")
    public void getMethod() {
    }

    @Pointcut("@annotation(ru.clevertec.aspect.Create)")
    public void createMethod() {
    }

    @Pointcut("@annotation(ru.clevertec.aspect.Update)")
    public void updateMethod() {
    }

    @Pointcut("@annotation(ru.clevertec.aspect.Delete)")
    public void deleteMethod() {
    }

    @Around(value = "getMethod()")
    @SuppressWarnings("unchecked")
    public Object getObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("CacheCourseAspect get method");
        UUID uuid = (UUID) joinPoint.getArgs()[0];
        Optional<Course> courseInCache = cache.get(uuid);
        if (courseInCache.isEmpty()) {
            Optional<Course> course = (Optional<Course>) joinPoint.proceed();
            Course courseToCache = course.orElseThrow();
            cache.put(courseToCache.getId(), courseToCache);
            return course;
        }
        return courseInCache;
    }

    @Around(value = "createMethod()")
    public Object createObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("CacheCourseAspect create method");
        Course course = (Course) joinPoint.proceed();
        cache.put(course.getId(), course);
        return course;
    }

    @Around(value = "updateMethod()")
    public Object updateObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("CacheCourseAspect update method");
        Course course = (Course) joinPoint.proceed();
        cache.put(course.getId(), course);
        return course;
    }

    @Around(value = "deleteMethod()")
    public Object deleteObject(ProceedingJoinPoint joinPoint) throws Throwable {
        log.debug("CacheCourseAspect delete method");
        UUID uuid = (UUID) joinPoint.getArgs()[0];
        Object proceed = joinPoint.proceed();
        cache.remove(uuid);
        return proceed;
    }
}
