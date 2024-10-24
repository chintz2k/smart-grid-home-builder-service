package com.homebuilder.repository;

import com.homebuilder.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author André Heinen
 */
public interface StorageRepository extends JpaRepository<Storage, Long> {

	Optional<List<Storage>> findByUserId(Long userId);

}
