package ru.yandex.practicum.client;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import ru.yandex.practicum.model.Courier;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public Response login(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login");
    }

    @Step("Удаление курьера")
    public Response delete(int courierId) {
        return given()
                .delete("/api/v1/courier/" + courierId);
    }
}