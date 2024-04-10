package com.keysoft.ecommerce.service.impl;

import com.keysoft.ecommerce.dto.GroupDTO;
import com.keysoft.ecommerce.dto.RoleDTO;
import com.keysoft.ecommerce.model.Group;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public boolean save(GroupDTO groupDTO) {
        log.info("SERVICE : SAVE GROUP, GROUP: {}", groupDTO);
        boolean isCheck = checkGroupExist(groupDTO.getName());
        if(isCheck){
            throw  new IllegalStateException("Nhóm người dùng đã tồn tại");
        }

        Set<RoleDTO> roles;

        if(groupDTO.getId() != null && GroupRepository.findById(groupDTO.getId()).isEmpty())
            throw new NullPointerException("Nhóm người dùng không tồn tại");

        if (StringUtils.hasText(groupDTO.getName()) && groupDTO.getRoles() != null) {
            roles = Set.copyOf(groupDTO.getRoles());
        } else {
            throw new NullPointerException("Không có quyền của nhóm hợp lệ");
        }

        groupDTO.setRoles(roles);
        groupDTO.setCode(CodeHelper.spawnCodeFromName(groupDTO.getName()));

        return GroupRepository.save(modelMapper.map(groupDTO, Group.class)).getId() != null;
    }

    public Boolean checkGroupExist(String name) {
        log.info("SERVICE PROCESS: CHECK GROUP NAME USED, NAME: {}", name);

        Group group = GroupRepository.findByName(name).orElse(new Group());
        return group.getId() != null;
    }

    @Override
    public GroupDTO get(Long id) {
        GroupDTO groupDTO = modelMapper.map(GroupRepository.findById(id).orElse(new Group()), GroupDTO.class);

        List<RoleDTO> roles = List.copyOf(groupDTO.getRoles());
        List<Long> roleIds = new ArrayList<>();

        roles.forEach(item -> roleIds.add(item.getId()));
        groupDTO.setListId(roleIds);

        return groupDTO;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Throwable.class})
    public boolean delete(Long id) {

        Group selected = GroupRepository.findById(id).orElse(new Group());

        if (selected.getId() == null) {
            throw new IllegalStateException("Không tìm được nhóm người dùng");
        }

        Hibernate.initialize(selected.getUsers());

        if (selected.getUsers().isEmpty()) {
            GroupRepository.deleteById(id);
        } else {
            return false;
        }

        return true;
    }
}
