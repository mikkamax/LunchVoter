package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    /**
     * Checks if object with same restaurantId and date (and DIFFERENT id) already exists in database
     *
     * @param menuId       of object to save (in case of persisting a new object, should be non-existing value e.g. -1)
     * @param restaurantId of object to save
     * @param date         of object to save
     * @return true if such object exists in the database, false otherwise
     */
    boolean existsByIdNotAndRestaurantIdAndDate(Integer menuId, Integer restaurantId, LocalDate date);

}
