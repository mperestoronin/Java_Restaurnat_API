package com.thelobsterpot.ordermanagment.repositories;

import com.thelobsterpot.ordermanagment.models.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDishRepository extends JpaRepository<OrderDish, Integer> {
}