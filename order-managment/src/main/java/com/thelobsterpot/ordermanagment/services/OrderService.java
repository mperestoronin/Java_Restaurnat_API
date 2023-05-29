package com.thelobsterpot.ordermanagment.services;

import com.thelobsterpot.ordermanagment.exceptions.OrderNotFoundException;
import com.thelobsterpot.ordermanagment.models.Dish;
import com.thelobsterpot.ordermanagment.models.Order;
import com.thelobsterpot.ordermanagment.models.OrderDish;
import com.thelobsterpot.ordermanagment.repositories.DishRepository;
import com.thelobsterpot.ordermanagment.repositories.OrderDishRepository;
import com.thelobsterpot.ordermanagment.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final OrderDishRepository orderDishRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, DishRepository dishRepository, OrderDishRepository orderDishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.orderDishRepository = orderDishRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Integer id) {
        return orderRepository.findById(id);
    }


    @Transactional
    public Order saveOrder(Order order) {
        // Save order first, but without the dishes
        order.setOrderDishes(new ArrayList<>());
        Order savedOrder = orderRepository.save(order);

        List<OrderDish> orderDishes = order.getOrderDishes();

        // Create a new list to hold the saved order dishes
        List<OrderDish> savedOrderDishes = new ArrayList<>();

        // Process each order dish
        for (OrderDish orderDish : orderDishes) {
            // Check if the dish is available
            Optional<Dish> optionalDish = dishRepository.findByIdAndQuantityGreaterThan(orderDish.getDish().getId());
            if (!optionalDish.isPresent()) {
                throw new IllegalArgumentException("Dish with id " + orderDish.getDish().getId() + " is not available");
            }
            Dish dish = optionalDish.get();
            dish.decreaseQuantity();

            // Set the order reference in order dish
            orderDish.setOrder(savedOrder);
            orderDish.setDish(dish);

            // Save the order dish
            OrderDish savedOrderDish = orderDishRepository.save(orderDish);

            // Add the saved order dish to the list
            savedOrderDishes.add(savedOrderDish);

            // Save the updated dish
            dishRepository.save(dish);
        }

        // Set the saved order dishes to the order
        savedOrder.setOrderDishes(savedOrderDishes);

        return savedOrder;
    }



    public void deleteOrder(Integer id) {
        orderRepository.deleteById(id);
    }

    public void updateOrderStatus(Integer orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            // Check if the status is valid (can be modified according to your requirements)
            if (!Arrays.asList("Pending", "Processing", "Delivered").contains(status)) {
                throw new IllegalArgumentException("Invalid status " + status + " isn't a valid status");
            }

            order.setStatus(status);
            orderRepository.save(order);
        } else {
            throw new OrderNotFoundException("Order with id " + orderId + " not found");
        }
    }

    @Async
    @Scheduled(fixedDelay = 10000)
    public CompletableFuture<Void> processOrders() {
        // find orders in 'pending' status
        List<Order> orders = orderRepository.findByStatus("Pending");

        // simulate processing delay and change status to 'completed'
        orders.forEach(order -> {
            try {
                Thread.sleep(10000);
                order.setStatus("Processing");
                orderRepository.save(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Scheduled(fixedDelay = 20000, initialDelay = 10000)
    public CompletableFuture<Void> processProcessingOrders() {
        // find orders in 'pending' status
        List<Order> orders = orderRepository.findByStatus("Processing");

        // simulate processing delay and change status to 'completed'
        orders.forEach(order -> {
            try {
                Thread.sleep(15000);
                order.setStatus("Completed");
                orderRepository.save(order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return CompletableFuture.completedFuture(null);
    }
}
