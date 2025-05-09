package com.nidhisync.billing.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.dto.UserRequestDto;
import com.nidhisync.billing.dto.UserResponseDto;
import com.nidhisync.billing.entity.Role;
import com.nidhisync.billing.entity.User;
import com.nidhisync.billing.repository.RoleRepository;
import com.nidhisync.billing.repository.UserRepository;
import com.nidhisync.billing.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponseDto createUser(UserRequestDto dto) {
		Set<Role> roles = dto.getRoles().stream()
				.map(name -> roleRepo.findByName(name)
						.orElseThrow(() -> new IllegalArgumentException("Role not found: " + name)))
				.collect(Collectors.toSet());

		User user = User.builder().username(dto.getUsername()).email(dto.getEmail())
				.password(passwordEncoder.encode(dto.getPassword())).mobileNumber(dto.getMobileNumber()) // ✅ important
				.roles(roles).build();

		User saved = userRepo.save(user);
		return toDto(saved);
	}

	@Override
	public UserResponseDto updateUserRoles(Long id, UserRequestDto dto) {
		User user = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id));

		Set<Role> roles = dto.getRoles() == null ? Set.of()
				: dto.getRoles().stream()
						.map(name -> roleRepo.findByName(name)
								.orElseThrow(() -> new IllegalArgumentException("Role not found: " + name)))
						.collect(Collectors.toSet());

		user.setRoles(roles);
		User updated = userRepo.save(user);
		return toDto(updated);
	}

	@Override
	public List<UserResponseDto> listAllUsers() {
		return userRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public UserResponseDto getUserById(Long id) {
		return userRepo.findById(id).map(this::toDto)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
	}

	@Override
	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	private UserResponseDto toDto(User user) {
		return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getMobileNumber(), // ←
																												// mobile
				user.getRoles().stream().map(Role::getName).collect(Collectors.toList()) // ← List<String>
		);
	}

	@Override
	public UserResponseDto findByUsername(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		return UserResponseDto.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail())
				.mobileNumber(user.getMobileNumber()).roles(user.getRoles().stream().map(Role::getName).toList())
				.build();
	}

	@Override
	public UserResponseDto updateRoles(Long id, Set<String> newRoles) {
		UserRequestDto dto = new UserRequestDto();
		dto.setRoles(newRoles);
		// reuse your existing logic
		return updateUserRoles(id, dto);
	}

}
