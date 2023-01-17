package com.github.jan5757.restaurantvoting.repository;

import com.github.jan5757.restaurantvoting.util.validation.ValidationUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id=:id")
    int delete(int id);

    default void deleteExisted(int id) {
        ValidationUtil.checkModification(delete(id), id);
    }

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id")
    Optional<T> get(int id);

    default T getExisted(int id) {
        return ValidationUtil.checkExisted(get(id), id);
    }
}
