package com.homebuilder.repository;

import com.homebuilder.entity.Storage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author André Heinen
 */
public interface StorageRepository extends JpaRepository<Storage, Long> {

	List<Storage> findByUserId(Long userId);

	Page<Storage> findAllByArchivedFalse(Pageable pageable);

	List<Storage> findAllByUserIdAndArchivedFalse(Long userId);

	void deleteAllByUserId(Long userId);
}
