package com.mike.lunchvoter.repository;

import com.mike.lunchvoter.entity.Dish;
import com.mike.lunchvoter.entity.DishIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, DishIdentity> {

}
