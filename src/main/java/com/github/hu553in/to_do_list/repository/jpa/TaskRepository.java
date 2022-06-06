package com.github.hu553in.to_do_list.repository.jpa;

import com.github.hu553in.to_do_list.entity.TaskEntity;
import com.github.hu553in.to_do_list.model.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

    Collection<TaskEntity> findAllByStatusAndOwnerId(TaskStatus status, Integer ownerId, Sort sort);

    Optional<TaskEntity> findByIdAndOwnerId(Integer id, Integer ownerId);

    Optional<TaskEntity> deleteByIdAndOwnerId(Integer id, Integer ownerId);

}
