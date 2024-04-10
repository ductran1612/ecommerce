package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.RoleDTO;
import com.keysoft.ecommerce.model.Role;
import com.keysoft.ecommerce.repository.RoleRepository;
import com.keysoft.ecommerce.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> results = new ArrayList<>();
        for(Role role : roleRepository.findAll()) {
            results.add(modelMapper.map(role, RoleDTO.class));
        }
        return results;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean save(RoleDTO roleDTO) {
        Role role;
        if(roleDTO.getId() != null) {
            role = roleRepository.findById(roleDTO.getId()).orElse(null);
            role.setName(roleDTO.getName());
        }else {
            role = modelMapper.map(roleDTO, Role.class);
        }
        return roleRepository.save(role).getId() != null ;
    }

    @Override
    public RoleDTO get(Long id) {
        return modelMapper.map(roleRepository.findById(id), RoleDTO.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(Long id) {
        try {
            roleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
