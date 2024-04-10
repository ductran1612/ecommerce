package com.keysoft.ecommerce.service;

import com.keysoft.ecommerce.dto.GroupDTO;

import java.util.List;

public interface GroupService {
    List<GroupDTO> getAllGroups();
    boolean save(GroupDTO groupDTO);
    Boolean checkGroupExist(String name);
    GroupDTO get(Long id);

    boolean delete(Long id);
}
