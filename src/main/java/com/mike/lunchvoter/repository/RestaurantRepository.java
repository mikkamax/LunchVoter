package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    /**
     * Checks if object with same name and address (and DIFFERENT id) already exists in database
     *
     * @param id      of object to save (in case of persisting a new object, should be non-existing value e.g. -1)
     * @param name    of object to save
     * @param address of object to save
     * @return true if such object exists in the database, false otherwise
     */
    boolean existsByIdNotAndNameEqualsIgnoreCaseAndAddressEqualsIgnoreCase(Integer id, String name, String address);

}
