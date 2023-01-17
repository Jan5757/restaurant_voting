# Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

## The task is:

#### Build a voting system for deciding where to have lunch.

1. 2 types of users: admin and regular users
2. Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
3. Menu changes each day (admins do the updates)
4. Users can vote for a restaurant they want to have lunch at today
5. Only one vote counted per user
6. If user votes again the same day:
7. If it is before 11:00 we assume that he changed his mind.
   - If it is after 11:00 then it is too late, vote can't be changed
   - Each restaurant provides a new menu each day.

### Swagger link: http://localhost:8080/swagger-ui/index.html

## Test credentials:
- user@yandex.ru / password
- admin@gmail.com / admin
- guest@gmail.com / guest