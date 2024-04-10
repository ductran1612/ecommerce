package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.dto.GroupDTO;
import com.keysoft.ecommerce.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/group")
@Slf4j
@CrossOrigin
public class GroupController {
    @Autowired
    private GroupService groupService;
    @GetMapping("/list")
    public ResponseEntity<?> getAllGroups(){
        log.info("controller: get all groups");
        List<GroupDTO> result = groupService.getAllGroups();
        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy  nào!");
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable("id") String id) {
        log.info("controller: update group form, id = {}", id);
        try {
            return ResponseEntity.ok(groupService.get(Long.valueOf(id)));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable(value = "id") String id) {
        log.info("controller: delete group, id = {}", id);
        try{
            return ResponseEntity.ok(groupService.delete(Long.valueOf(id)));
        }catch (NumberFormatException e){
            return ResponseEntity.badRequest().body("Xoá không thành công!");
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody GroupDTO groupDTO) {
        log.info("controller: save Group");
        boolean isSaved = groupService.save(groupDTO);
        if(isSaved){
            return ResponseEntity.ok("Save successfully");
        }
        return ResponseEntity.badRequest().body("Save error");
    }
}
