package com.nidhisync.billing.controller;

import com.nidhisync.billing.dto.UserRequestDto;
import com.nidhisync.billing.dto.UserRolesUpdateDto;
import com.nidhisync.billing.dto.UserResponseDto;
import com.nidhisync.billing.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /** 
   * Admin: create a new user (ADMIN or CLERK). 
   * Now requires mobileNumber in the body.
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> create(
      @Valid @RequestBody UserRequestDto dto) {
    UserResponseDto created = userService.createUser(dto);
    return ResponseEntity.ok(created);
  }

  /**
   * Admin: update only this user’s roles.
   * Uses a slim DTO so you don’t clobber mobile/email/password.
   */
  @PutMapping("/{id}/roles")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> updateRoles(
      @PathVariable Long id,
      @Valid @RequestBody UserRolesUpdateDto dto) {

    UserResponseDto updated = userService.updateRoles(id, dto.getRoles());
    return ResponseEntity.ok(updated);
  }

  /** Admin: list all users (includes mobileNumber in response) */
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponseDto>> list() {
    return ResponseEntity.ok(userService.listAllUsers());
  }

  /** Admin: fetch one user by ID */
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponseDto> get(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  /** Admin: delete a user */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
