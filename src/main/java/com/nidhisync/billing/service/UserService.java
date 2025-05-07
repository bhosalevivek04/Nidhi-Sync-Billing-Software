package com.nidhisync.billing.service;

import com.nidhisync.billing.dto.UserRequestDto;
import com.nidhisync.billing.dto.UserResponseDto;

import java.util.List;

public interface UserService {

	UserResponseDto createUser(UserRequestDto dto);

	List<UserResponseDto> listAllUsers();

	UserResponseDto getUserById(Long id);

	void deleteUser(Long id);
}