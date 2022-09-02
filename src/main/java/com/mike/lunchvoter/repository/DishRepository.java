package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

    /**
     * Checks if object with provided id exists and belongs to provided menu
     *
     * @param dishId of dish
     * @param menuId of menu
     * @return true if such object exists in the database, false otherwise
     */
    boolean existsByIdAndMenu_Id(Long dishId, Long menuId);

}
