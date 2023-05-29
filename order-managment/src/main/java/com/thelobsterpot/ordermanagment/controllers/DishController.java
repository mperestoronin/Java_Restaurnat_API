package com.thelobsterpot.ordermanagment.controllers;

import com.thelobsterpot.ordermanagment.models.Dish;
import com.thelobsterpot.ordermanagment.services.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Integer id) {
        return dishService.getDishById(id)
                .map(dish -> ResponseEntity.ok().body(dish))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Dish saveDish(@RequestBody Dish dish) {
        return dishService.saveDish(dish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Integer id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/menu")
    public ResponseEntity<List<Dish>> getMenu() {
        List<Dish> dishes = dishService.getAvailableDishes();
        return ResponseEntity.ok(dishes);
    }
}
