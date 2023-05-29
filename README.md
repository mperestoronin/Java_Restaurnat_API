# Java_Restaurnat_API

Ссылки на Postman:
<br> https://grey-resonance-934930.postman.co/workspace/New-Team-Workspace~3f29f347-8cb5-4e81-992a-96e2fb69d346/collection/27666004-97206b72-50ca-4612-aeba-94212a11b83c?action=share&creator=27666004
<br> https://grey-resonance-934930.postman.co/workspace/New-Team-Workspace~3f29f347-8cb5-4e81-992a-96e2fb69d346/collection/27666004-cf7b133d-9284-4721-b844-71ab54bfc524?action=share&creator=27666004
<br>Также прилагаю JSON файл (см в репозитории)

### Особенности реализации:
Работа сделана на Java 20 с использованием фреймворка spring.
В качестве базы данных был выбран PostgreSQL. Необходимую информацию о бд можно найти в файле application.properties.
Для удобства тестировния, база данных очищается при каждом запуске программы.

<br> Тестирование было выполнено и спользованием postman. 
<br> Для тестирования сначала необходимо зарегестрировать пользователя, затем войти в аккаунт зарегестированного пользователя, взять оттуда токен, и использовать его для получения данных о пользователе. Токен действует 30 минут.
<br> К проекту приложена postman коллекция, но на всякий случай распишу тестирование более подробно на примере микросервиса для авторизациии:
<br>POST `http://localhost:8080/api/user/register` в body выбираем raw, формат json, передаем данные о пользователе в JSON формате:
```json
{
    "username": "Georg2",
    "email": "georg2@gmail.com",
    "passwordHash": "test",
    "role": "admin"
}
```
После чего используем:
<br> GET `http://localhost:8080/api/user/login` в params задаем 2 параметра: (key: username; value: Georg2) и (key: password; value: test)
<br> Должен вернутся токен, его нужно скопировать.
<br> Далее GET `http://localhost:8080/api/user/me` в authorization выбираем bearer token и в поле токен вставляем токен, полученный при логине.
В ответ на это получаем информацию о пользователе.
