# Задание:

1. Создать любой gradle проект
2. Проект должен быть совместим с java 17
3. Придерживаться GitFlow: master -&gt; develop -&gt; feature/fix
4. Создать реализацию кэша, используя алгоритмы LRU и LFU
5. Создать в приложении слои service и dao (service будет вызывать слой
   dao, слой dao будет временная замена database). В этих сервисах
   реализовать CRUD операции для работы с entity. Работу организовать
   через интерфейсы.
6. Результат работы dao должен синхронизироваться с кешем через proxy
   (или кастомная аннотация, или АОП/aspectj). При работе с entity
   оперируем id. Алгоритм работы с кешем:
   ● GET - ищем в кеше и если там данных нет, то достаем объект из
   dao, сохраняем в кеш и возвращаем
   ● POST - сохраняем в dao и потом сохраняем в кеше
   ● DELETE - удаляем из dao и потом удаляем из кеша
   ● PUT - обновление/вставка в dao и потом обновление/вставка в
   кеше

7. Алгоритм и максимальный размер коллекции должны читаться из
   файла resources/application.yml
8. Создать entity, в нем должно быть поле id и еще минимум 4 поля
9. Service работает с dto
10. Объекты (dto), которые принимает service, должны валидироваться. В
    т.ч. добавить regex валидацию
11. Кеши должны быть покрыты unit tests
12. Должен содержать javadoc и описанный README.md
13. Использовать lombok
14. *Реализовать метод для получения информации в формате xml
15. Заполнить и отправить форму

# Литература:

### Cache:

- Least Frequently Used (LFU) Cache Implementation - Dinesh on Java
  https://www.dineshonjava.com/least-frequently-used-lfu-cache-implementation/
- Алгоритмы кэширования | Bimlibik
  https://bimlibik.github.io/posts/cache-algorithms/
- Creating a simple and generic cache manager in Java | by Marcello Passos | Medium
  https://medium.com/@marcellogpassos/creating-a-simple-and-generic-cache-manager-in-java-e62e4204a10e

### Proxy:

- Java Dynamic proxy mechanism and how Spring is using it | by Spac Valentin | Medium
  https://medium.com/@spac.valentin/java-dynamic-proxy-mechanism-and-how-spring-is-using-it-93756fc707d5
- Dynamic Proxies in Java | Baeldung
  https://www.baeldung.com/java-dynamic-proxies
- Proxy (Java SE 17 &amp; JDK 17) (oracle.com)
  https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/reflect/Proxy.html
- Заместитель на Java (refactoring.guru)
  https://refactoring.guru/ru/design-patterns/proxy/java/example