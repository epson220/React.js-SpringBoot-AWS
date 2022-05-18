package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try{
            String temporayUserId = "temporary-user";

            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.todoEntity(dto);
            // id를 null로 초기화한다. 생성당시엔 id가 없어야 하기 때문이다.
            entity.setId(null);
            // 임시 사용자 아이디를 설정해준다. 이 부분은 인증과 인가에서 수정할 예정임.
            // 지금은 인증과 인가 기능이 없으므로 한 사용자(temporary-user)만 로그인없이
            // 사용할 수 있는 애플리케이션인 셈이다.
            entity.setUserId(temporayUserId);
            // 서비스를 이용해 Todo엔티티를 생성
            List<TodoEntity> entities = service.create(entity);
            // 자바스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            // ResponseDTO를 리턴한다.
            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            // 혹시 예외가 있는 경우 dto 대신 error에 메세지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
   }
}
