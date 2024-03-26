
# Задание #2:
___
Папка */doc* содержит скрипт в jMeter, который использовался для замеров производительности и отчет как результат ресёрча.

Был обновленный файл postman collection (добавлен метод /search)
otus-social-network.postman_collection.json

docker-compose:
содержит контейнер с postgres

## запуск приложения
___
* Открыть терминал и запустить postgres docker container:
```bash
docker-container up -d
```

* запустить spring boot class runner
  (схемы ddl и dml ициализируются liquibase скриптами при старте приложения)

## для ручного запуска explain в docker container
___
* переходим в postgres container
```bash
docker exec -it otus-social-network-dialogue-db /bin/sh
```
* подключаемся к базе данных otus из docker-compose файла
```bash
psql -d otus-social-network-dialogue-db -U postgres -W
```
* вводим пароль в подключении к базе данных
```bash
postgres
```
* проверяем наличие индексов в таблице:
```bash
SELECT indexname, indexdef FROM pg_indexes WHERE tablename = 'person';
```
> (если индексы не нужны, можно закоментировать создание индекса в скрипте liquibase, находящемся в/resources.
Можно было бы обьявить context ликвибейз и запускать с параметром контекста, чтобы регулировать скрипт на создание индекса черех параметры)

* выполнение SQL запроса с планом
```bash
\timing
explain SELECT * FROM person WHERE firstname like 'Толкачев%' AND lastname like 'Алек%' ORDER BY id ASC; 
```
> `\timing` включает отображение времени выполнения запросов. В SQL `;` в конце запроса обязательна.


___
`author` **Dzmitry Kapachou**


