package ru.yandex.practicum;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {

    // Создаем курьера
    public Response create(String login, String password, String firstName) {
        String body = String.format(
                "{\"login\": \"%s\", \"password\": \"%s\", \"firstName\": \"%s\"}",
                login, password, firstName
        );

        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .post("/api/v1/courier");
    }

    // Логин курьера (возвращает Response, из которого можно достать ид)
    public Response login(String login, String password) {
        String body = String.format(
                "{\"login\": \"%s\", \"password\": \"%s\"}",
                login, password
        );

        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .post("/api/v1/courier/login");
    }

    // Удаление курьера по ид
    public Response delete(int courierId) {
        return given()
                .delete("/api/v1/courier/" + courierId);
    }
}