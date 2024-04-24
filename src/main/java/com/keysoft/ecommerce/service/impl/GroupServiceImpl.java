package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.GroupDTO;
import com.keysoft.ecommerce.dto.RoleDTO;
import com.keysoft.ecommerce.model.Customer;
import com.keysoft.ecommerce.model.Group;
import com.keysoft.ecommerce.model.Role;
import com.keysoft.ecommerce.repository.GroupRepository;
import com.keysoft.ecommerce.service.GroupService;
import com.keysoft.ecommerce.util.CodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<GroupDTO> getAllGroups() {
        List<GroupDTO> results = new ArrayList<>();
        for(Group Group : groupRepository.findAll()) {
            results.add(modelMapper.map(Group, GroupDTO.class));
        }
        return results;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean save(GroupDTO groupDTO) {
        log.info("SERVICE : SAVE GROUP, GROUP: {}", groupDTO);
        boolean isCheck = checkGroupExist(groupDTO);
        if(isCheck){
            throw  new IllegalStateException("Nhóm người dùng đã tồn tại");
        }

        Group group;
        if(groupDTO.getId() != null){
            group = groupRepository.findById(groupDTO.getId()).orElse(null);
            if(group != null)
                group.getRoles().removeAll(group.getRoles());
        }

        groupDTO.setCode(CodeHelper.spawnCodeFromName(groupDTO.getName()));
        group = modelMapper.map(groupDTO, Group.class);
        return groupRepository.save(group).getId()!=null;
    }

    public Boolean checkGroupExist(GroupDTO criteria) {
        Group group = groupRepository.findByName(criteria.getName()).orElse(null);

        if (group == null)
            return false;

        if (criteria.getId() == null) {
            return true;
        }

        return (!Objects.equals(group.getId(), criteria.getId()));
    }

    @Override
    public GroupDTO get(Long id) {
        GroupDTO groupDTO = modelMapper.map(groupRepository.findById(id).orElse(new Group()), GroupDTO.class);

        List<RoleDTO> roles = List.copyOf(groupDTO.getRoles());
        List<Long> roleIds = new ArrayList<>();

        roles.forEach(item -> roleIds.add(item.getId()));
        groupDTO.setListId(roleIds);

        return groupDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(Long id) {

        Group selected = groupRepository.findById(id).orElse(new Group());

        if (selected.getId() == null) {
            throw new IllegalStateException("Không tìm được nhóm người dùng");
        }

        Hibernate.initialize(selected.getUsers());

        if (selected.getUsers().isEmpty()) {
            groupRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
