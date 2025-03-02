package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "password", target = "password")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  User convertLoginUserPostDTOtoEntity(UserPostDTO loginUserPostDTO);


  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "birthday", target = "birthday")
  User convertEntityToFullUserGetDTO(User user);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  User convertEditUserPutDTOtoEntity(UserPutDTO editUserPutDTO);

}
