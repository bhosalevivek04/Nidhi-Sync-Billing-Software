package com.nidhisync.billing.service;

import java.util.List;
import java.util.Set;

import com.nidhisync.billing.dto.UserRequestDto;
import com.nidhisync.billing.dto.UserResponseDto;

public interface UserService {

	UserResponseDto createUser(UserRequestDto dto);
	UserResponseDto findByUsername(String username);

	List<UserResponseDto> listAllUsers();

	UserResponseDto getUserById(Long id);

	void deleteUser(Long id);

	UserResponseDto updateRoles(Long id, Set<String> newRoles);
	// New method to update user roles
	UserResponseDto updateUserRoles(Long id, UserRequestDto dto);
}
