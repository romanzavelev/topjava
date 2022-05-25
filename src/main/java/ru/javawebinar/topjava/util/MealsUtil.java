package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate,Integer> caloriesSumPerData = new HashMap<>();
        for (Meal meal : meals) {
            LocalDate localDate = meal.getDateTime().toLocalDate();
            caloriesSumPerData.put(localDate,caloriesSumPerData.getOrDefault(localDate,0) + meal.getCalories());
        }
        List<MealTo> mealsExceeded  = new ArrayList<>();
        for (Meal meal : meals) {
            LocalTime lt = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(lt,startTime,endTime)) {
                MealTo mealWithExcess =
                        new MealTo(meal.getDateTime(),meal.getDescription(),meal.getCalories(),caloriesSumPerData.get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
                mealsExceeded.add(mealWithExcess);
            }

        }

        return mealsExceeded;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        System.out.println("------------------------test----------------------");
        meals.forEach(m -> System.out.println(m.toString()));
        System.out.println("------------------------test----------------------");
        Map<LocalDate, Integer> ColoriesSunByDay = meals.stream().collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(), Collectors.summingInt(um -> um.getCalories())));
        return meals.stream()
                .filter(um -> TimeUtil.isBetweenHalfOpen(um.getDateTime().toLocalTime(),startTime,endTime))
                .map(um -> new MealTo(um.getDateTime(),um.getDescription(),um.getCalories(),
                        ColoriesSunByDay.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());

    }

    //private static MealTo
}
