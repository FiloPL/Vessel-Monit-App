package pl.filo.vesselmonit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import pl.filo.vesselmonit.service.AuthenticationUserService;
import pl.filo.vesselmonit.service.UserService;
import pl.filo.vesselmonit.entity.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
public class UserController {
    private final UserService userService;
    private final AuthenticationUserService authenticationUserService;

    public UserController(final UserService userService,
                          final AuthenticationUserService authenticationUserService) {
        this.userService = userService;
        this.authenticationUserService = authenticationUserService;
    }

    @GetMapping("/users")
    public String getUsers(
            final ModelMap modelMap,
            @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser,
            @RequestParam(required = false, defaultValue = "1") final Integer page,
            @RequestParam(required = false, defaultValue = "5") final Integer size,
            final String keyword)
    {
        modelMap.addAttribute("pageName", "users");
        authenticationUserService.setAuthorizationInfo(modelMap, authenticationUser);
        if(keyword != null) {
            modelMap.addAttribute("usersList", userService.getByKeyword(keyword, page - 1, size).getContent());
            final Page<User> userPage = userService.getByKeyword(keyword, page - 1, size);
            modelMap.addAttribute("userPage", userPage);
            setPageNumbers(modelMap, page, userPage.getTotalPages());
            modelMap.addAttribute("addedKeyword", keyword);
        }
        else {
            modelMap.addAttribute("usersList", userService.getAllUsers(page - 1, size).getContent());
            final Page<User> userPage = userService.getAllUsers(page - 1, size);
            modelMap.addAttribute("userPage", userPage);
            setPageNumbers(modelMap, page, userPage.getTotalPages());
            modelMap.addAttribute("addedKeyword", null);
        }
        return "users";
    }

    private void setPageNumbers(final ModelMap modelMap, final Integer page, final int totalPages)
    {
        if(totalPages > 0) {
            final int pageOffset = 2;
            final List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .filter(integer ->
                            (integer == 1)
                                    || ((integer >= page - pageOffset) && (integer <= page + pageOffset))
                                    || (integer == totalPages))
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
    }

    @GetMapping("/users/{id}")
    public String getOneUserById(final ModelMap modelMap, @PathVariable final Long id,
                                 @AuthenticationPrincipal final org.springframework.security.core.userdetails.User authenticationUser) {
        modelMap.addAttribute("pageName", "users");
        authenticationUserService.setAuthorizationInfo(modelMap, authenticationUser);
        modelMap.addAttribute("user", userService.getUserById(id));
        modelMap.addAttribute("userTable", userService.getUserById(id));
        return "one-user";
    }

    @PostMapping("/users/{id}")
    public String updateUserById(@Valid @ModelAttribute("user") final User user, final Errors errors,
                                 @PathVariable final Long id, final ModelMap modelMap) {
        authenticationUserService.adminAuthorized(modelMap);
        modelMap.addAttribute("userTable", userService.getUserById(id));
        if(errors.hasErrors()) {
            return "one-user";
        }
        final User updatedUser = userService.updateUserById(id, user);
        if(updatedUser == null) {
            modelMap.addAttribute("userExistsError", "Can't create new user, " +
                    "because that username or email already exist.");
            return "one-user";
        }
        return "redirect:/users/" + id;
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUserById(@PathVariable(name = "id") final Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping("/users/add")
    public String showAddUser(final ModelMap modelMap) {
        authenticationUserService.adminAuthorized(modelMap);
        modelMap.addAttribute("user", new User());
        return "user-add";
    }

    @PostMapping("/users/add")
    public String addUser(@Valid @ModelAttribute("user") final User user, final Errors errors, final ModelMap modelMap) {
        authenticationUserService.adminAuthorized(modelMap);
        if(errors.hasErrors()) {
            return "user-add";
        }
        final User newUser = userService.createNewUser(user);
        if(newUser == null) {
            modelMap.addAttribute("userExistsError", "Can't create new user, " +
                    "because that username or email already exist.");
            return "user-add";
        }
        return "redirect:/users";
    }



}
