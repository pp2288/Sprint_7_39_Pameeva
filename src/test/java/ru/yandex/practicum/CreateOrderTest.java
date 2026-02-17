package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends ScooterBaseTest {

    private final String[] color;
    private final String description;

    public CreateOrderTest(String[] color, String description) {
        this.color = color;
        this.description = description;
    }

    // Набор тестовых данных: разные комбинации цветов
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
        // JSON для массива цветов
        StringBuilder colorJson = new StringBuilder("[");
        for (int i = 0; i < color.length; i++) {
            colorJson.append("\"").append(color[i]).append("\"");
            if (i < color.length - 1) colorJson.append(", ");
        }
        colorJson.append("]");

        String body = String.format(
                "{\"firstName\": \"Анна\", " +
                        "\"lastName\": \"Иванова\", " +
                        "\"address\": \"Москва, ул. Тестовая, 1\", " +
                        "\"metroStation\": 4, " +
                        "\"phone\": \"+7 800 555 55 55\", " +
                        "\"rentTime\": 5, " +
                        "\"deliveryDate\": \"2026-06-06\", " +
                        "\"comment\": \"Тестовый заказ\", " +
                        "\"color\": %s}", colorJson
        );

        Response response = io.restassured.RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .post("/api/v1/orders");

        response.then()
                .statusCode(201)
                .body("track", notNullValue());
    }
}