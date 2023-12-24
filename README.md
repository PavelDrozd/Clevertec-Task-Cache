### Инструкция по запуску проекта:

### Запуск через Tomcat:

С помощью IDEA:

- Создать базу данных в PostgreSQL с названием "task_servlet"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- запустить приложение с помощью Apache Tomcat с параметром Context path: /clevertec

С помощью сервера Apache Tomcat:

- создать базу данных в PostgreSQL с названием "task_servlet"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- в корневой папке приложения прописать в консоли: gradle build
- зайти в папку build/libs/ взять файл clevertec.war и переместить в папку webapps от корневой папки расположения Apache
  Tomcat

### CRUD операции в Application классе:

### Пример объекта в формате JSON:

{
"name": "Name",
"info": "Info",
"cost": 777.0,
"discount": 7.0,
"start": "2023-12-31",
"duration": "PT1440H"
}

##### Пример Create:

- Отправляем POST запрос с телом CourseDto объекта в формате JSON по ссылке: http://localhost:8080/clevertec/courses
- Получаем результат сгенерированный UUID, например: 09f9b1d6-a68c-4a9d-bb35-180edec5d3d5

##### Пример GetAll:

- Отправляем GET запрос по ссылке: http://localhost:8080/clevertec/courses
- Также можно указать дополнительные параметры pagesize, offset, page
  Например: http://localhost:8080/clevertec/courses?pagesize=2
- Получаем результатом ответ от сервера список курсов из количества элементов не больше указанного (в application.yml
  файле в параметре pagination.limit) в формате JSON.

##### Пример Get by ID:

- Отправляем GET запрос по ссылке с параметром ID.
  Например: http://localhost:8080/clevertec/courses?id=36da71f5-173f-431a-9844-0a33eed3b7c5
- Получаем результатом объект из базы данных в формате JSON.

##### Пример Update:

- Отправляем PUT запрос с изменённым CourseDto в теле запроса и отдельным параметром передаём ID объекта для изменения.
 Например: http://localhost:8080/clevertec/courses?id=0faac2b1-0394-4708-b53f-5cd3c48cbe7e
- Результатом придёт ответ с изменённым объектом в формате JSON.

##### Пример DeleteById:

- Отправляем DELETE запрос по ссылке с параметром ID.
  Например: http://localhost:8080/clevertec/courses?id=36da71f5-173f-431a-9844-0a33eed3b7c5
- После отправки данного запроса ответом от сервера будет результирующий статус.

##### Печать файлов в PDF формате:

- Производится автоматически после отправки GET запроса определённого объекта.
- Печать файла с определённым именем производится в папку установленную в конфигурационном файле application.yml
- Также файлы для шрифта и подложки можно добавить самостоятельно и изменить их расположение или название в
  application.yml