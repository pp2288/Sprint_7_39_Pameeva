package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.practicum.client.OrderClient;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends ScooterBaseTest {

    @Test
    public void getOrderListReturnsOrders() {
        OrderClient orderClient = new OrderClient();
        Response response = orderClient.getOrders();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}