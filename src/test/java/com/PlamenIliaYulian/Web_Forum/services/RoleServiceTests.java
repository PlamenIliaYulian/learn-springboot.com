package com.PlamenIliaYulian.Web_Forum.services;

import com.PlamenIliaYulian.Web_Forum.helpers.TestHelpers;
import com.PlamenIliaYulian.Web_Forum.models.Role;
import com.PlamenIliaYulian.Web_Forum.repositories.contracts.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    RoleRepository roleMockRepository;

    @InjectMocks
    RoleServiceImpl roleMockService;

    @Test
    public void getRoleById_Should_ReturnRole_When_MethodCalled() {
        Mockito.when(roleMockRepository.getRoleById(2))
                .thenReturn(TestHelpers.createMockRoleMember());

        Role role = roleMockService.getRoleById(2);

        Assertions.assertEquals(2, role.getRoleId());
    }

    @Test
    public void getRoleByName_Should_ReturnRole_When_MethodCalled() {
        Mockito.when(roleMockRepository.getRoleByName(Mockito.anyString()))
                .thenReturn(TestHelpers.createMockRoleMember());

        Role role = roleMockService.getRoleByName("ROLE_MEMBER");

        Assertions.assertEquals(2, role.getRoleId());
    }

    @Test
    public void getAllRoles_Should_CallRepository() {
        roleMockService.getAllRoles();

        Mockito.verify(roleMockRepository, Mockito.times(1))
                .getAllRoles();
    }
}
