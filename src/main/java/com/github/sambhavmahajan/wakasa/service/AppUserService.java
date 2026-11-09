package com.github.sambhavmahajan.wakasa.service;

import com.github.sambhavmahajan.wakasa.dto.RegisterDTO;
import com.github.sambhavmahajan.wakasa.entity.AppToken;
import com.github.sambhavmahajan.wakasa.entity.AppUser;
import com.github.sambhavmahajan.wakasa.exception.BadPasswordException;
import com.github.sambhavmahajan.wakasa.exception.EmailAlreadyExistsException;
import com.github.sambhavmahajan.wakasa.exception.InvalidEmailFormatException;
import com.github.sambhavmahajan.wakasa.exception.PasswordMissmatchException;
import com.github.sambhavmahajan.wakasa.repository.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final BloomFilterService bloomFilterService;
    private final TokenService tokenService;
    @Override
    public @Nullable UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(bloomFilterService.mightContain(username)) {
            return repository.findById(username).orElseThrow(() ->
                    new UsernameNotFoundException("Username or password is wrong"));
        }
        throw new UsernameNotFoundException("Username or password is wrong");
    }
    private void validatePassword(RegisterDTO register) {
        if(register.confirmPassword().length() > 50 || register.password().length() > 50) {
            throw new BadPasswordException("Password can not be longer than 50 characters");
        }
        if(register.confirmPassword().length() < 6 || register.password().length() < 6) {
            throw new BadPasswordException("Password can not be less than 6 characters");
        }
        if(!register.confirmPassword().equals(register.password())) {
            throw new PasswordMissmatchException("Passwords do not match");
        }
        String password = register.password();
        boolean containsLowercase = false, containsUppercase = false, containsDigit = false;
        for(int i = 0; i < password.length(); i++) {
            if(password.charAt(i) == ' ') {
                throw new BadPasswordException("Password cannot contain empty spaces");
            }
            if(password.charAt(i) >= 'a' && password.charAt(i) <= 'z') {
                containsLowercase = true;
            } else if(password.charAt(i) >= 'A' && password.charAt(i) <= 'Z') {
                containsUppercase = true;
            } else if(password.charAt(i) >= '0' && password.charAt(i) <= '9') {
                containsDigit = true;
            }
        }
        if(!containsDigit || !containsLowercase || !containsUppercase) {
            throw new BadPasswordException("Password must contain a number, lower case character and an uppercase character");
        }
    }
    @Transactional
    public void createNewUser(RegisterDTO register) {
        if(!EmailValidator.getInstance().isValid(register.email())) {
            throw new InvalidEmailFormatException("Email format is wrong");
        }
        validatePassword(register);
        boolean contains = true;
        try {
            loadUserByUsername(register.email());
        } catch(UsernameNotFoundException ex) {
            contains = false;
        }
        if(contains) {
            throw new EmailAlreadyExistsException("User with this email already exists");
        }
        String password = passwordEncoder.encode(register.password());
        AppUser user = new AppUser();
        user.setEmail(register.email());
        user.setPassword(password);
        user = repository.save(user);
        AppToken token = new AppToken();
        token.setOwner(user);
        token.setNewValue();
        tokenService.save(token);
        bloomFilterService.save(user.getUsername());
    }
}
