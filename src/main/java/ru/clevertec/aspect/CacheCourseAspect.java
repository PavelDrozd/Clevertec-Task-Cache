package ru.clevertec.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.factory.CacheFactory;
import ru.clevertec.entity.Course;

import java.util.Optional;
import java.util.UUID;

@Aspect
public class CacheCourseAspect {

    @SuppressWarnings("unchecked")
    private final Cache<UUID, Course> cache = CacheFactory.INSTANCE.getCache(Cache.class);

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
        Course course = (Course) joinPoint.proceed();
        cache.put(course.getId(), course);
        return course;
    }

    @Around(value = "createMethod()")
    public Object updateObject(ProceedingJoinPoint joinPoint) throws Throwable {
        Course course = (Course) joinPoint.proceed();
        cache.put(course.getId(), course);
        return course;
    }

    @Around(value = "deleteMethod()")
    public Object deleteObject(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID uuid = (UUID) joinPoint.getArgs()[0];
        Object proceed = joinPoint.proceed();
        cache.remove(uuid);
        return proceed;
    }
}
