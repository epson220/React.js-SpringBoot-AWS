package com.example.demo.service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("hi").build();
        // TodoEntity 저장
        repository.save(entity);
        // TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity) {

        // 유효성 검증
        validate(entity);

        repository.save(entity);

        log.info("Entity id : {} is saved", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    private void validate(final TodoEntity entity) {
        // 유효성 검증
        if(entity == null) {
            log.warn("entity 가 비어있음");
            throw new RuntimeException("entity가 비어있음");
        }

        if(entity.getUserId() == null) {
            log.warn("알려지지 않은 유저");
            throw new RuntimeException("알려지지 않은 유저");
        }
    }
}
