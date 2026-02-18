package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.Courier;

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
        login = "testcourier" + System.currentTimeMillis();
        password = "12345";
        firstName = "Иван";
        courierId = 0;
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    private void loginAndSaveId() {
        Response loginResponse = courierClient.login(new Courier(login, password));
        if (loginResponse.statusCode() == 200) {
            courierId = loginResponse.jsonPath().getInt("id");
        }
    }

    // Курьера можно создать — запрос возвращает 201 и ok: true
    @Test
    public void courierCanBeCreated() {
        Response response = courierClient.create(new Courier(login, password, firstName));

        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));

        loginAndSaveId();
    }

    // Нельзя создать двух одинаковых курьеров
    @Test
    public void cannotCreateDuplicateCourier() {
        courierClient.create(new Courier(login, password, firstName));
        loginAndSaveId();

        Response response = courierClient.create(new Courier(login, password, firstName));

        response.then()
                .statusCode(409);
    }

    // Если создать курьера с логином, который уже есть — ошибка
    @Test
    public void createCourierWithExistingLoginReturnsError() {
        courierClient.create(new Courier(login, password, firstName));
        loginAndSaveId();

        Response response = courierClient.create(new Courier(login, "otherpass", "Пётр"));

        response.then()
                .statusCode(409)
                .body("message", notNullValue());
    }

    // Нельзя создать курьера без логина
    @Test
    public void cannotCreateCourierWithoutLogin() {
        Response response = courierClient.create(new Courier("", password, firstName));

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    // Нельзя создать курьера без пароля
    @Test
    public void cannotCreateCourierWithoutPassword() {
        Response response = courierClient.create(new Courier(login, "", firstName));

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }
}