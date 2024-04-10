package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.GroupDTO;
import com.keysoft.ecommerce.model.Group;
import com.keysoft.ecommerce.repository.GroupRepository;
import com.keysoft.ecommerce.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository GroupRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<GroupDTO> getAllGroups() {
        List<GroupDTO> results = new ArrayList<>();
        for(Group Group : GroupRepository.findAll()) {
            results.add(modelMapper.map(Group, GroupDTO.class));
        }
        return results;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean save(GroupDTO GroupDTO) {
        Group Group;
        if(GroupDTO.getId() != null) {
            Group = GroupRepository.findById(GroupDTO.getId()).orElse(null);
            Group.setName(GroupDTO.getName());
        }else {
            Group = modelMapper.map(GroupDTO, Group.class);
        }
        return GroupRepository.save(Group).getId() != null;
    }

    @Override
    public GroupDTO get(Long id) {
        return modelMapper.map(GroupRepository.findById(id), GroupDTO.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(Long id) {
        try {
            GroupRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
