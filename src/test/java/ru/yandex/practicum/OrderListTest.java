package ru.yandex.practicum;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends ScooterBaseTest {

    @Test
    public void getOrderListReturnsOrders() {
        given()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}