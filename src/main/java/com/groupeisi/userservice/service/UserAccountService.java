package com.groupeisi.userservice.service;

import com.groupeisi.userservice.dto.UserAccountDto;
import com.groupeisi.userservice.entities.UserAccount;
import com.groupeisi.userservice.mapper.UserAccountMapper;
import com.groupeisi.userservice.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;
    private final PasswordEncoder passwordEncoder;

    public UserAccountDto create(UserAccountDto dto) {
        log.info("Création utilisateur : {}", dto.getEmail());
        if (userAccountRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé : " + dto.getEmail());
        }
        UserAccount user = userAccountMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        UserAccount saved = userAccountRepository.save(user);
        log.info("Utilisateur créé : {}", saved.getId());
        return userAccountMapper.toDto(saved);
    }

    @Cacheable("users")
    public List<UserAccountDto> getAll() {
        log.info("Récupération de tous les utilisateurs");
        return userAccountRepository.findAll()
                .stream()
                .map(userAccountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "users", key = "#id")
    public UserAccountDto getById(Long id) {
        log.info("Récupération utilisateur id : {}", id);
        return userAccountMapper.toDto(
                userAccountRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + id))
        );
    }

    @CacheEvict(value = "users", key = "#id")
    public UserAccountDto update(Long id, UserAccountDto dto) {
        log.info("Mise à jour utilisateur id : {}", id);
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + id));
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userAccountMapper.toDto(userAccountRepository.save(user));
    }

    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        log.info("Suppression utilisateur id : {}", id);
        if (!userAccountRepository.existsById(id)) {
            throw new EntityNotFoundException("Utilisateur non trouvé : " + id);
        }
        userAccountRepository.deleteById(id);
        log.info("Utilisateur supprimé : {}", id);
    }
}