package com.nidhisync.billing.service;

import com.nidhisync.billing.dto.UserRequestDto;
import com.nidhisync.billing.dto.UserResponseDto;

import java.util.List;

public interface UserService {

	UserResponseDto createUser(UserRequestDto dto);
	UserResponseDto findByUsername(String username);

	List<UserResponseDto> listAllUsers();

	UserResponseDto getUserById(Long id);

	void deleteUser(Long id);
}