package com.thelobsterpot.ordermanagment.repositories;

import com.thelobsterpot.ordermanagment.models.Dish;
import com.thelobsterpot.ordermanagment.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByStatus(String status);
}
