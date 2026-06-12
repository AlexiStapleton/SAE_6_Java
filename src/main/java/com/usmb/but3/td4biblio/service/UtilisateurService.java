package com.usmb.but3.td4biblio.service;

import com.usmb.but3.td4biblio.dto.UtilisateurDetailResponseDto;
import com.usmb.but3.td4biblio.dto.RegisterRequest;
import com.usmb.but3.td4biblio.dto.UtilisateurResponseDto;
import com.usmb.but3.td4biblio.entity.Utilisateur;
import com.usmb.but3.td4biblio.exception.RessourceNotFoundException;
import com.usmb.but3.td4biblio.mapper.UtilisateurMapper;
import com.usmb.but3.td4biblio.mapper.UtilisateurRepo;
import com.usmb.but3.td4biblio.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class UtilisateurService extends AbstractGenericService<Utilisateur, Integer, UtilisateurResponseDto, UtilisateurDetailResponseDto, RegisterRequest>{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UserRepository repository, UtilisateurMapper mapper, PasswordEncoder passwordEncoder) {
        super(repository, mapper);
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UtilisateurDetailResponseDto update(Integer id, RegisterRequest dto) {
        Utilisateur utilisateur = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Utilisateur non trouvé : " + id));

        mapper.updateFromDto(dto, utilisateur);

        return mapper.toDetailResponse(repository.save(utilisateur));
    }

    @Override
    public UtilisateurDetailResponseDto create(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email déjà utilisé");
        }

        Utilisateur utilisateur = mapper.toEntity(request);
        utilisateur.setDateFinAbonnement(LocalDate.now().plusYears(1));
        utilisateur.setHashMotDePasse(passwordEncoder.encode(request.getPassword()));

        return mapper.toDetailResponse(repository.save(utilisateur));
    }

    public Optional<Utilisateur> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getHashMotDePasse()));
    }
}
