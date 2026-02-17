package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CreateCourierTest extends ScooterBaseTest {

    private CourierClient courierClient;
    private String login;
    private String password;
    private String firstName;
    private int courierId;

    @Before
    public void init() {
        courierClient = new CourierClient();
        // Генерируем уникальный логин, чтобы тесты не мешали друг другу
        login = "testcourier" + System.currentTimeMillis();
        password = "12345";
        firstName = "Иван";
        courierId = 0;
    }

    @After
    public void tearDown() {
        // Удаляем курьера после теста, если он был создан
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    // Вспомогательный метод: логинимся и сохраняем ид для последующего удаления
    private void loginAndSaveId() {
        Response loginResponse = courierClient.login(login, password);
        if (loginResponse.statusCode() == 200) {
            courierId = loginResponse.jsonPath().getInt("id");
        }
    }

    // Курьера можно создать — запрос возвращает 201 и ok: true
    @Test
    public void courierCanBeCreated() {
        Response response = courierClient.create(login, password, firstName);

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        loginAndSaveId();
    }

    // Нельзя создать двух одинаковых курьеров
    @Test
    public void cannotCreateDuplicateCourier() {
        // Создаём первого курьера
        courierClient.create(login, password, firstName);
        loginAndSaveId();

        // Пытаемся создать с тем же логинм
        Response response = courierClient.create(login, password, firstName);

        response.then()
                .statusCode(409);
    }

    // Если создать курьера с логином, который уже есть — ошибка
    @Test
    public void createCourierWithExistingLoginReturnsError() {
        courierClient.create(login, password, firstName);
        loginAndSaveId();

        Response response = courierClient.create(login, "otherpass", "Пётр");

        response.then()
                .statusCode(409)
                .body("message", notNullValue());
    }

    // Нельзя создать курьера без логина
    @Test
    public void cannotCreateCourierWithoutLogin() {
        Response response = courierClient.create("", password, firstName);

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    // Нельзя создать курьера без пароля
    @Test
    public void cannotCreateCourierWithoutPassword() {
        Response response = courierClient.create(login, "", firstName);

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }
}