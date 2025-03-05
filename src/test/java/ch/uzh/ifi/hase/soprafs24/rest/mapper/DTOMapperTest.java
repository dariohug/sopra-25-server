package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.controller.UserController;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;


@ExtendWith(MockitoExtension.class)
public class DTOMapperTest {

  @Mock
  private UserService userService;            // Create a Mock instace of user service

  @InjectMocks
  private UserController userController;


  // Test POST by creating a new User 
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setName("name");
    userPostDTO.setUsername("username");

    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    assertEquals(userPostDTO.getName(), user.getName());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
  }

  // Test if GET return expected values
  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    User user = new User();
    user.setName("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");

    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getName(), userGetDTO.getName());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
  }

  // Test if a duplicate Name will throw an exception
  @Test
  public void testCreateUser_withDuplicateUsernameOrName_failure() {
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("existingUser");
    userPostDTO.setName("existingName");
    userPostDTO.setPassword("password");

    String errorMessage = "The username and the name provided are not unique. Therefore, the user could not be created!";
    
    when(userService.createUser(any())).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, errorMessage));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      userController.createUser(userPostDTO);
    });

    assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    assertEquals(errorMessage, exception.getReason());
  }

  // Test that an invalid ID search will throw an exception
  @Test
  public void testFindById_userNotFound() {
      long userId = 1L;
  
      when(userService.findById(userId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + userId + " was not found"));
  
      Exception exception = assertThrows(ResponseStatusException.class, () -> {
          userController.getUserbyUserID(userId);
      });
  
      assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatus());
      assertThat(exception.getMessage(), containsString("User with ID 1 was not found"));
  }
  

  // Tests successfull user Update
  @Test
  public void testUpdateUserProfile_success() {
      UserPutDTO userPutDTO = new UserPutDTO();
      userPutDTO.setUsername("updatedUsername");

      assertDoesNotThrow(() -> userController.editUser(userPutDTO, 1L));

      verify(userService, times(1)).editUserbyUserID(any());
  }


  // Test that trying to update a nonexisting User will thwo Exception
  @Test
  public void testUpdateUserProfile_userNotFound() {
    UserPutDTO userPutDTO = new UserPutDTO();
    userPutDTO.setUsername("updatedUsername");

    when(userService.editUserbyUserID(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid id: 2"));

    Exception exception = assertThrows(ResponseStatusException.class, () -> {
      userController.editUser(userPutDTO, 2L);
    });

    assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatus());
  }
}
