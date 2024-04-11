package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.RoleDTO;
import com.keysoft.ecommerce.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
@Slf4j
@CrossOrigin
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseEntity<?> getAllRoles(){
        log.info("controller: get all roles");
        List<RoleDTO> result = roleService.getAllRoles();
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy  nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateRole(@PathVariable("id") String id) {
        log.info("controller: update role form, id = {}", id);
        Map<String, Object> responseData = new HashMap<>();
        try {
            responseData.put("role", roleService.get(Long.valueOf(id)));
            return ResponseEntity.ok(responseData);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable(value = "id") String id) {
        log.info("controller: delete role, id = {}", id);
        try{
            return ResponseEntity.ok(roleService.delete(Long.valueOf(id)));
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Xoá không thành công!");
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody RoleDTO roleDTO) {
        log.info("controller: save role");
        boolean isSaved = roleService.save(roleDTO);
        if(isSaved){
            return ResponseEntity.ok("Lưu thành công");
        }
        return ResponseEntity.badRequest().body("Lỗi khi lưu");
    }
}
