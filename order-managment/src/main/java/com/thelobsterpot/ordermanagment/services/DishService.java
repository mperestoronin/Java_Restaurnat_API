package com.thelobsterpot.ordermanagment.services;

import com.thelobsterpot.ordermanagment.models.Dish;
import com.thelobsterpot.ordermanagment.repositories.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Optional<Dish> getDishById(Integer id) {
        return dishRepository.findById(id);
    }

    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public void deleteDish(Integer id) {
        dishRepository.deleteById(id);
    }

    public List<Dish> getAvailableDishes() {
        return dishRepository.findAll().stream()
                .filter(dish -> dish.getQuantity() > 0)
                .collect(Collectors.toList());
    }
}
