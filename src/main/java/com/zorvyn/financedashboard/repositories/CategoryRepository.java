package com.zorvyn.financedashboard.repositories;

import com.zorvyn.financedashboard.entities.Category;
import com.zorvyn.financedashboard.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByUserAndIsDeletedFalse(User user);

    Optional<Category> findByIdAndUserAndIsDeletedFalse(Integer id, User user);

    boolean existsByNameAndUserAndIsDeletedFalse(String name, User user);

    Optional<Category> findByNameAndUserAndIsDeletedFalse(String name, User user);

}
