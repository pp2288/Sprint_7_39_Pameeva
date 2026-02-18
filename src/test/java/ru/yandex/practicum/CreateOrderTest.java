package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.client.OrderClient;
import ru.yandex.practicum.model.Order;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends ScooterBaseTest {

    private final String[] color;
    private final String description;

    public CreateOrderTest(String[] color, String description) {
        this.color = color;
        this.description = description;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {new String[]{"BLACK"}, "только BLACK"},
                {new String[]{"GREY"}, "только GREY"},
                {new String[]{"BLACK", "GREY"}, "оба цвета"},
                {new String[]{}, "без цвета"},
        };
    }

    @Test
    public void createOrderReturnsTrack() {
        Order order = new Order(
                "Анна",
                "Иванова",
                "Москва, ул. Тестовая, 1",
                4,
                "+7 800 555 55 55",
                5,
                "2026-06-06",
                "Тестовый заказ",
                color
        );

        OrderClient orderClient = new OrderClient();
        Response response = orderClient.create(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}