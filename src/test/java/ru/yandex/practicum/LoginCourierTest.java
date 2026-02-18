package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.client.CourierClient;
import ru.yandex.practicum.model.Courier;

import static org.hamcrest.Matchers.*;

public class LoginCourierTest extends ScooterBaseTest {

    private CourierClient courierClient;
    private String login;
    private String password;
    private int courierId;

    @Before
    public void init() {
        courierClient = new CourierClient();
        login = "testlogin" + System.currentTimeMillis();
        password = "12345";
        courierId = 0;

        courierClient.create(new Courier(login, password, "Тест"));
    }

    @After
    public void tearDown() {
        if (courierId == 0) {
            Response loginResponse = courierClient.login(new Courier(login, password));
            if (loginResponse.statusCode() == 200) {
                courierId = loginResponse.jsonPath().getInt("id");
            }
        }
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    // Курьер может авторизоваться — успешный запрос возвращает id
    @Test
    public void courierCanLogin() {
        Response response = courierClient.login(new Courier(login, password));

        response.then()
                .statusCode(200)
                .body("id", notNullValue());

        courierId = response.jsonPath().getInt("id");
    }

    // Если неправильный пароль — ошибка
    @Test
    public void loginWithWrongPasswordReturnsError() {
        Response response = courierClient.login(new Courier(login, "wrongpassword"));

        response.then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    // Если неправильный логин — ошибка
    @Test
    public void loginWithWrongLoginReturnsError() {
        Response response = courierClient.login(new Courier("nonexistentlogin", password));

        response.then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    // Если нет поля login — ошибка
    @Test
    public void loginWithoutLoginFieldReturnsError() {
        Response response = courierClient.login(new Courier("", password));

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    // Если нет поля password — ошибка
    @Test
    public void loginWithoutPasswordFieldReturnsError() {
        Response response = courierClient.login(new Courier(login, ""));

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    // Авторизация под несуществующим пользователем — ошибка
    @Test
    public void loginNonExistentCourierReturnsError() {
        Response response = courierClient.login(new Courier("fakecourier999999", "fakepass"));

        response.then()
                .statusCode(404)
                .body("message", notNullValue());
    }
}