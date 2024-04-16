package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.RoleDTO;
import com.keysoft.ecommerce.dto.UserDTO;
import com.keysoft.ecommerce.model.Group;
import com.keysoft.ecommerce.model.Role;
import com.keysoft.ecommerce.model.User;
import com.keysoft.ecommerce.repository.GroupRepository;
import com.keysoft.ecommerce.repository.RoleRepository;
import com.keysoft.ecommerce.repository.UserRepository;
import com.keysoft.ecommerce.service.UserService;
import com.keysoft.ecommerce.specification.UserSpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserSpecification userSpecification;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<UserDTO> getAllUsers(UserDTO userDTO) {
        log.info("service: get all users");
        Page<User> page = userRepository.findAll(userSpecification.filter(userDTO), PageRequest.of(userDTO.getPage(), userDTO.getSize()));
        List<UserDTO> results = new ArrayList<>();
        for (User user : page.getContent()) {
            results.add(modelMapper.map(user, UserDTO.class));
        }
        if(results.isEmpty())
            throw new IllegalStateException("Chưa có người dùng");
        return new PageImpl<>(results, PageRequest.of(userDTO.getPage(), userDTO.getSize()), page.getTotalElements());
    }

    @Override
    @Transactional
    public boolean save(UserDTO userDTO) {
        log.info("service: save user");
        if (isUsernameUsed(userDTO))
            throw new IllegalStateException("Username đã tồn tại");
        User userEntity;

        if (userDTO.getId() == null) {
            userEntity = modelMapper.map(userDTO, User.class);
            userEntity.setEnable(true);
        } else {
            userEntity = userRepository.findById(userDTO.getId()).orElse(new User());
            if (userEntity.getId() == null) {
                throw new IllegalStateException("Thông tin người dùng không tồn tại");
            }
        }

        if (userDTO.getPassword().trim().length() > 0) {
            userEntity.setPassword(BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(10)));
        }

        Group group = groupRepository.findById(userDTO.getGroup().getId()).orElse(new Group());
        userEntity.setGroup(group);

        return modelMapper.map(userRepository.save(userEntity), UserDTO.class).getId() != null;
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        return false;
    }

    @Override
    public UserDTO get(String id) {
        try {
            long selectedId = Long.parseLong(id);
            User resultEntity = userRepository.findById(selectedId).orElse(null);

            if (resultEntity == null) {
                throw new IllegalStateException("Không tìm thấy người dùng");
            }
            return modelMapper.map(resultEntity, UserDTO.class);
        }catch (NumberFormatException e) {
            throw new IllegalStateException("Người dùng không hợp lệ");
        }
    }

    @Override
    public boolean assignRole(UserDTO userDTO) {
        User userEntity = userRepository.findById(userDTO.getId()).orElse(new User());
        if (userEntity.getId() == null) {
            throw new IllegalStateException("Thông tin người dùng không tồn tại");
        }
        if(userDTO.getRoles().isEmpty())
            throw new IllegalStateException("Chưa chọn quyền nào");

        userEntity.getRoles().removeAll(userEntity.getRoles());
        Set<Role> roleSet = new HashSet<>();
        for(RoleDTO roleDTO : userDTO.getRoles()) {
            Role role = roleRepository.findById(roleDTO.getId()).orElse(null);
            if(!userEntity.getGroup().getRoles().contains(role)) {
                roleSet.add(role);
            }
        }
        userEntity.setRoles(roleSet);
        userRepository.save(userEntity);
        return true;
    }

    public boolean isUsernameUsed(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername()).orElse(null);
        if(user == null)
            return false;
        if(userDTO.getId() == null)
            return true;
        return (!Objects.equals(user.getId(), userDTO.getId()));
    }
}
