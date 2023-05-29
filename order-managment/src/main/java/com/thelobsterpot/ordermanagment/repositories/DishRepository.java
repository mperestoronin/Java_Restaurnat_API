package com.thelobsterpot.ordermanagment.repositories;

import com.thelobsterpot.ordermanagment.models.Dish;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.quantity > 0")
    Optional<Dish> findByIdAndQuantityGreaterThan(Integer id);
}

