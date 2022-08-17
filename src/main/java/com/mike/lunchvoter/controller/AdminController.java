package com.mike.lunchvoter.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AdminController.ADMIN_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    static final String ADMIN_URL = "/admin";

    //админ может

    //- рестораны
    //-- создавать
    //-- смотреть
    //-- редактировать название
    //-- активировать/деактивировать ресторан
    //-- смотреть весь список
    //-- смотреть список активных
    //-- искать по названию ресторана

    //- меню
    //-- создавать меню
    //-- смотреть
    //-- добавлять в меню блюда
    //-- редактировать блюда
    //-- удалять блюда
    //-- смотреть меню по дате по всем ресторанам
    //-- смотреть меню по ресторану по всем датам

    //- пользователи
    //-- добавлять
    //-- смотреть
    //-- смотреть всех
    //-- деактивировать
    //-- менять роли
    //-- редактировать
    //-- удалять

}
