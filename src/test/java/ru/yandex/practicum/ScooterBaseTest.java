package ru.yandex.practicum;

import io.restassured.RestAssured;
import org.junit.Before;

public class ScooterBaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        //RestAssured.filters(new io.restassured.filter.log.RequestLoggingFilter(),
        //        new io.restassured.filter.log.ResponseLoggingFilter());
    }
}