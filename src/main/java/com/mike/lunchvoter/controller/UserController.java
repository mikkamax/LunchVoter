package com.mike.lunchvoter.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = UserController.USER_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    static final String USER_URL = "/user";

    //пользователь может

    //- аккаунт
    //-- создать
    //-- редактировать
    //-- изменить данные и пароль
    //-- удалить

    //- меню
    //-- смотреть все меню за сегодня
    //-- проголосовать за меню текущего дня
    //-- смотреть распределение голосов за сегодня
    //-- смотреть распределение голосов на какую-либо дату


}
