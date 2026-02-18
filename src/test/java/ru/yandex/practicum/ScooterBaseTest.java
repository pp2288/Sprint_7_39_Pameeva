package ru.yandex.practicum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import org.junit.Before;

public class ScooterBaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}