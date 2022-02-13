package pl.filo.vesselmonit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.filo.vesselmonit.service.UserService;
import pl.filo.vesselmonit.entity.User;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(required = false) final Integer page,
            @RequestParam(required = false) final Integer size)
    {
        return ResponseEntity.ok(userService.getAllUsers(page, size).getContent());
    }

    @PostMapping
    public ResponseEntity<?> registerNewUser(
            @RequestBody final User user,
            @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser)
    {
        final User createdUser = userService.createNewUser(user);
        if(Objects.isNull(createdUser)) {
            return ResponseEntity.badRequest().build();
        }
        log.info("USERNAME: " + authenticationUser.getUsername());
        log.info("USER: " + authenticationUser.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

}
