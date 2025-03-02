package ch.uzh.ifi.hase.soprafs24.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // To edit
import org.springframework.web.bind.annotation.RequestBody; // to pass method parameters
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

/**
 * User Controller This class is responsible for handling all REST request that
 * are related to the user. The controller will receive the request and delegate
 * the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    Get all users
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User createdUser = userService.createUser(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody UserPostDTO loginUserPostDTO) {
        User userInput = DTOMapper.INSTANCE.convertLoginUserPostDTOtoEntity(loginUserPostDTO);
        User userData = userService.logIn(userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userData);
    }

    @GetMapping(value = "/users/{id}")
    @ResponseBody
    public UserGetDTO getUserbyUserID(@PathVariable("id") long id) {
        User user = userService.findById(id).get();
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

    @PutMapping(value = "/logout/{id}")
    @ResponseBody
    public UserGetDTO logoutUser(@PathVariable("id") Long id) {
       User userToLogout = userService.findById(id).get();
        userService.logOut(userToLogout);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userToLogout);
    }

    @PutMapping(value = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editUser(@RequestBody UserPutDTO editUserPutDTO, @PathVariable("id") Long id) {
        User editUser = DTOMapper.INSTANCE.convertEditUserPutDTOtoEntity(editUserPutDTO);
        editUser.setId(id);

        userService.editUserbyUserID(editUser);
    }

}
