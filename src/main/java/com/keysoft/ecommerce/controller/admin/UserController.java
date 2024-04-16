package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.service.GroupService;
import com.keysoft.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10") int size) {
        log.info("controller: get all users");
        try{
            UserDTO user = new UserDTO();
            user.setPage(page);
            user.setSize(size);
            return ResponseEntity.ok(userService.getAllUsers(user));
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") String id) {
        log.info("controller: form update user");
        try{
            return ResponseEntity.ok(userService.get(id));
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UserDTO userDTO) {
        log.info("controller: get all users");
        try {
            if(userService.save(userDTO))
                return ResponseEntity.ok("Lưu người dùng thành công");
            return ResponseEntity.badRequest().body("Lưu người dùng thất bại");
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping ("/assignRole/{id}")
    public ResponseEntity<?> assignRole(@PathVariable("id")String id) {
        log.info("controller: form assign role");
        try{
            return ResponseEntity.ok(userService.get(id));
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/saveRole")
    public ResponseEntity<?> saveAssignRole(@RequestBody UserDTO userDTO) {
        try {
            if(userService.assignRole(userDTO))
                return ResponseEntity.ok("Thành công");
            return ResponseEntity.badRequest().body("Thất bại");
        }catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
