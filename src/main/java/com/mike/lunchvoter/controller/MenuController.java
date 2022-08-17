package com.mike.lunchvoter.controller;

import com.mike.lunchvoter.dto.MenuDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(MenuController.MENU_URL)
public class MenuController {

    static final String MENU_URL = "/menu";

    //-- создавать меню
    //-- смотреть
    //-- добавлять в меню блюда
    //-- редактировать блюда
    //-- удалять блюда
    //-- смотреть меню по дате по всем ресторанам
    //-- смотреть меню по ресторану по всем датам

    //-- смотреть все меню за сегодня
    //-- проголосовать за меню текущего дня
    //-- смотреть распределение голосов за сегодня
    //-- смотреть распределение голосов на какую-либо дату

    public MenuDto create(MenuDto menuDto) {
        return null;
    }

    public MenuDto get(Integer menuId) {
        return null;
    }

    public List<MenuDto> getAll() {
        return null;
    }

    public List<MenuDto> getByDate(LocalDate date) {
        return null;
    }

    public List<MenuDto> getByRestaurant(Integer restaurantId) {
        return null;
    }

    public MenuDto update(Long menuId, MenuDto menuDto) {
        return null;
    }

    public void delete(Integer menuId) {
    }
}
