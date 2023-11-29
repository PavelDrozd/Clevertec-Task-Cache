### Инструкция по запуску проекта:

### Альтернативный запуск через Tomcat(Если нет докера):

- Создать базу данных в PostgreSQL с названием "task_cache"
  (все конфигурационные параметры можно изменить в файле application.yml - src/main/resources/application.yml)
- запустить SQL скрипт файл с schema.sql в папке src/main/resources
- Запустить класс Application и использовать его как замену контроллеру.

### CRUD операции в Application классе:
https://github.com/PavelDrozd/Clevertec-Task-Cache/blob/5d48ac3c07e939fff24364e0d639de650ce48129/src/main/java/ru/clevertec/Application.java

##### Пример обработки XML:
- Отправляем XML в метод processXMLAndValidate: <CourseDto><name>Scala developer</name><info>An easy way to become a Scala developer.</info><cost>500</cost><discount>0</discount><start>2023-12-13</start><duration>PT720H</duration></CourseDto>
- Получаем результат: Scala developer: CourseDto[name=Scala developer, info=An easy way to become a Scala developer., cost=500, discount=0, start=2023-12-13, duration=PT720H]

##### Пример обработки JSON:
- Отправляем JSON в метод processJSONAndValidate: {"name":"Groovy developer","info":"Advanced course for Scala developer.","cost":3000,"discount":400,"start":"2023-11-14","duration":"PT2880H"}
- Получаем результат: Scala developer: CourseDto[name=Groovy developer, info=Advanced course for Scala developer., cost=3000, discount=400, start=2023-11-14, duration=PT2880H]

##### Пример Create:

- Отправляем CourseDto объекты в метод create в service;
- Получаем результат сгенерированный UUID, например: 09f9b1d6-a68c-4a9d-bb35-180edec5d3d5

##### Пример GetAll:

- Вызываем метод getAll в service;
- Получаем результат: [CourseDto[name=Pro Scala developer, info=Pro level of Scala developer., cost=1200.0, discount=100.0, start=2024-01-10, duration=PT1440H], 
CourseDto[name=Groovy developer, info=Advanced course for Scala developer., cost=3000.0, discount=400.0, start=2023-11-14, duration=PT2880H]]

##### Пример Get by ID:

- Вызываем метод getById в service и передаём в него UUID объекта. например: 345dc84e-00e0-4710-a6e1-0477ba003b4d
- Получаем результатом объект из базы данных, например: CourseDto[name=Groovy developer, info=Advanced course for Scala developer., cost=3000, discount=400, start=2023-11-14, duration=PT2880H]

##### Пример Update:

- Отправляем новый изменённый CourseDto в метод update в service: CourseDto[name=Scala developer, info=An easy way to become a Scala developer., cost=500.0, discount=0.0, start=2023-12-13, duration=PT720H]
- Результат можно будет увидеть вызвав метод getById в service в который передать UUID этого объекта;

##### Пример DeleteById:

- Вызываем метод deleteById и передаём туда UUID объекта для удаления, например: 345dc84e-00e0-4710-a6e1-0477ba003b4d
- Результат можно увидеть вызвав метод getAll в service где будет отсутствовать удалённый объект.

##### Печать файлов в PDF формате:
- Может быть осуществлена вызовом метода writePdf который принимает в качестве параметра объект CourseDto. 
- Печать файла с определённым именем производится в папку установленную в конфигурационном файле application.yml
- Также файлы для шрифта и подложки можно добавить самостоятельно и изменить их расположение или название в application.yml