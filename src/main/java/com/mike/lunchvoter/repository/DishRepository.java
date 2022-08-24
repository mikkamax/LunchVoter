package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {

    boolean existsByIdAndMenu_Id(Integer dishId, Integer menuId);

}
