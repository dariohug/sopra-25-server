package ch.uzh.ifi.hase.soprafs24.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;


/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setName("testName");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertNotNull(createdUser.getId());
    assertEquals(testUser.getName(), createdUser.getName());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));
  
      User testUser = new User();
      testUser.setName("testName");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      userService.createUser(testUser);
  
      User testUser2 = new User();
      testUser2.setName("testName2");
      testUser2.setUsername("testUsername");
      testUser2.setPassword("password");

  
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, 
        () -> userService.createUser(testUser2), 
        "Expected ResponseStatusException when creating a user with duplicate username"
    );

    assertEquals(HttpStatus.CONFLICT, exception.getStatus(), "Expected HTTP 409 CONFLICT status");
    assertTrue(exception.getReason().contains("not unique"), 
        "Exception message should indicate duplicate username");
  }
  

}
