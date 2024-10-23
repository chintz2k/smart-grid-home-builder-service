package com.homebuilder.repository;

import com.homebuilder.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface StorageRepository extends JpaRepository<Storage, Long> {

	List<Storage> findByOwnerId(Long ownerId);

}
