package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;


public class UserGetDTO {

  private Long id;
  private Date creationDate;
  private String name;
  private String username;
  private UserStatus status;
  private String password; 
  private Date birthday;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  // @Dario Hug 
  public void setPassword(String password){
    this.password = password;
  }

  public String getPassword(){
    return password;
  }  

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Date getBirthday() {
      return birthday;
  }  
}
