package ru.yandex.practicum.client;

import io.restassured.response.Response;
import io.qameta.allure.Step;
import ru.yandex.practicum.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание заказа")
    public Response create(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .body(order)
                .post("/api/v1/orders");
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .get("/api/v1/orders");
    }
}