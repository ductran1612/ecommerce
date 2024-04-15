package com.keysoft.ecommerce.controller.admin;

import com.keysoft.ecommerce.service.GroupService;
import com.keysoft.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;


}
