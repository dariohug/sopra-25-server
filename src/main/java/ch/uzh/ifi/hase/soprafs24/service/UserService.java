package ch.uzh.ifi.hase.soprafs24.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 * Dario: All logic happens in here 
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public Optional<User> findById(Long Id) {
    Optional<User> userById = userRepository.findById(Id);
    return userById;
  }

  public User createUser(User newUser) {
    if (newUser.getName() == null || newUser.getUsername() == null || newUser.getPassword() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, Username, and Password are required.");
    }

    newUser.setToken(UUID.randomUUID().toString());
    Date creationDate = new Date();
    newUser.setCreationDate(creationDate);
    newUser.setStatus(UserStatus.ONLINE);
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called

    checkIfUserExists(newUser);

    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User logIn(User loginUser){
    checkUserCredentials((loginUser));
    User loggedInUser = userRepository.findByUsername(loginUser.getUsername());
    loggedInUser.setStatus(UserStatus.ONLINE);
    loggedInUser.setToken(UUID.randomUUID().toString());
    userRepository.saveAndFlush(loggedInUser);
    log.debug("Logged in User: {}", loggedInUser);
    return loggedInUser;
  }

  public void logOut(User logoutUser){
    User loggedInUser = userRepository.findByUsername(logoutUser.getUsername());
    loggedInUser.setStatus(UserStatus.OFFLINE);
    userRepository.saveAndFlush(loggedInUser);
    log.debug("Logged out User: {}", loggedInUser);
  }

  public User editUserbyUserID(User user) {
    Long userId = user.getId();
    String username = user.getUsername();
    Date birthday = user.getBirthday();

    Optional<User> optionalUser = userRepository.findById(userId);

    if (optionalUser.isEmpty()) {  // <-- Corrected check for empty Optional
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with user id %s not found!", userId));
    }

    User existingUser = optionalUser.get();

    if (existingUser.getUsername().equals(username)) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
    }

    if (username != null) {
        existingUser.setUsername(username);
    }
    if (birthday != null) {
        existingUser.setBirthday(birthday);
    }

    userRepository.saveAndFlush(existingUser);
    return existingUser;
}




  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the name", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByName != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
    }
  }

  private void checkUserCredentials(User userToBeAuthenticated) {
  
    User userByUsername = userRepository.findByUsername(userToBeAuthenticated.
    
    // Check if username exists
    getUsername());
    if (userByUsername == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown Username, register now!");
    }

    // Check if this is the correct Password 
    if (!userByUsername.getPassword().equals(userToBeAuthenticated.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect Password.");
    }
  }
}