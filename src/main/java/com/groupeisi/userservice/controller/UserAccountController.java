package com.groupeisi.userservice.controller;

import com.groupeisi.userservice.dto.UserAccountDto;
import com.groupeisi.userservice.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @PostMapping
    public ResponseEntity<UserAccountDto> create(@RequestBody UserAccountDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userAccountService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserAccountDto>> getAll() {
        return ResponseEntity.ok(userAccountService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userAccountService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccountDto> update(@PathVariable Long id,
                                                 @RequestBody UserAccountDto dto) {
        return ResponseEntity.ok(userAccountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userAccountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}