package com.github.hu553in.to_do_list.repository.jpa;

import com.github.hu553in.to_do_list.entity.TaskEntity;
import com.github.hu553in.to_do_list.enumeration.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    Page<TaskEntity> findAllByStatusAndOwnerId(TaskStatus status, Integer ownerId, Pageable pageable);

    Optional<TaskEntity> findByIdAndOwnerId(Integer id, Integer ownerId);

    Optional<TaskEntity> deleteByIdAndOwnerId(Integer id, Integer ownerId);

}
