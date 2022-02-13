package pl.filo.vesselmonit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pl.filo.vesselmonit.entity.User;
import pl.filo.vesselmonit.exception.WrongPageException;
import pl.filo.vesselmonit.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService
{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserSettingsService userSettingsService;

    public UserService(
            final UserRepository userRepository,
            final BCryptPasswordEncoder bCryptPasswordEncoder,
            final UserSettingsService userSettingsService)
    {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userSettingsService = userSettingsService;
    }

    @Override
    public UserDetails loadUserByUsername(final String loginUsername) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(loginUsername);
        if(user == null) {
            user = userRepository.findByEmail(loginUsername);
            if(user == null) {
                throw new UsernameNotFoundException("Nie znaleziono uzytkownika o loginie " + loginUsername);
            }
        }
        return new org.springframework.security.core.userdetails.User(loginUsername, user.getPassword(),
                user.getEnabled(), true, true, true,
                AuthorityUtils.createAuthorityList(user.getRole()));
    }

    public User createNewUser(final User user) {
        if(userRepository.findByUsername(user.getUsername()) == null
                && userRepository.findByEmail(user.getEmail()) == null)
        {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            if(user.getEnabled() == null) {
                user.setEnabled(true);
            }
            if(user.getRole() == null) {
                user.setRole("ROLE_USER");
            }
            final User createdUser = userRepository.saveAndFlush(user);
            userSettingsService.createDefaultSettings(createdUser.getId());
            return createdUser;
        }
        return null;
    }

    public Page<User> getAllUsers(final Integer page, final Integer size) {
        final Pageable pageable = getPageable(page, size);
        return userRepository.findAll(pageable);
    }

    public User getUserById(final Long id) {
        final Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    public User updateUserById(final Long id, final User user) {
        if(userRepository.existsById(id)) {
            user.setId(id);
            if(!getUserById(id).getPassword().equals(user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(user);
        }
        return null;
    }

    public Boolean deleteUserById(final Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<User> getByKeyword(final String keyword, final Integer page, final Integer size) {
        final Pageable pageable = getPageable(page, size);


        return userRepository.findByKeyword(keyword, pageable);
    }

    private Pageable getPageable(Integer page, Integer size) {
        if(Objects.isNull(page)) {
            page = 0;
        }
        if(Objects.isNull(size)) {
            size = 5;
        }
        if(page < 0) {
            throw new WrongPageException("Page number can't be less than 1");
        }
        return PageRequest.of(page, size);
    }

    public User getByUsernameOrEmail(final String usernameOrEmail) {
        return userRepository.findByUsernameAndEmail(usernameOrEmail);
    }

}