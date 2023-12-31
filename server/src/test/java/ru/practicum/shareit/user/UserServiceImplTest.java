package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestPropertySource(properties = {"db.name=test"})
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest {
    private final EntityManager em;
    private final UserService service;

    @Test
    void saveUser() {
        UserDto userDto = makeUserDto("some@email.com", "Пётр Иванов");
        service.saveUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUser() {
        UserDto userDto = makeUserDto("some@email.com", "Пётр Иванов");
        service.saveUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        long id = user.getId();
        UserDto userDtoUpdate = makeUserDto("some@NewEmail.com", "Пётр Новый Иванов");
        service.updateUser(userDtoUpdate, id);
        TypedQuery<User> queryAfterUpdate = em.createQuery("Select u from User u where u.id = :id", User.class);
        User userAfterUpdate = queryAfterUpdate.setParameter("id", id).getSingleResult();

        assertThat(userAfterUpdate.getId(), notNullValue());
        assertThat(userAfterUpdate.getId(), equalTo(id));
        assertThat(userAfterUpdate.getName(), equalTo(userDtoUpdate.getName()));
        assertThat(userAfterUpdate.getEmail(), equalTo(userDtoUpdate.getEmail()));
    }

    @Test
    void findAllUsers() {
        UserDto userDto1 = makeUserDto("some@email.com", "Пётр Иванов");
        UserDto userDto2 = makeUserDto("some", "Пётр");
        UserDto userDto3 = makeUserDto("some@email2.com", "Пётр Петров");
        userDto1 = service.saveUser(userDto1);
        userDto2 = service.saveUser(userDto2);
        userDto3 = service.saveUser(userDto3);

        List<UserDto> users = service.findAllUsers();
        assertThat(users, equalTo(List.of(userDto1, userDto2, userDto3)));
    }

    @Test
    void findUserDtoById_whenUserFound_thenReturnedUser() {
        UserDto userDto1 = makeUserDto("some@email.com", "Пётр Иванов");
        UserDto userDto3 = makeUserDto("some@email2.com", "Пётр Петров");
        service.saveUser(userDto1);
        service.saveUser(userDto3);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user1 = query.setParameter("email", userDto1.getEmail()).getSingleResult();

        UserDto userDtoById1 = service.findUserDtoById(user1.getId());
        userDto1.setId(user1.getId());
        assertThat(userDtoById1, equalTo(userDto1));

        User user3 = query.setParameter("email", userDto3.getEmail()).getSingleResult();
        UserDto userDtoById3 = service.findUserDtoById(user3.getId());
        userDto3.setId(user3.getId());
        assertThat(userDtoById3, equalTo(userDto3));
    }

    @Test
    void findUserDtoById_whenUserNotFound_thenNotFoundException() {
        Assertions.assertThrows(NotFoundException.class, () -> service.findUserDtoById(-9999));
    }

    @Test
    void deleteUser() {
        UserDto userDto1 = makeUserDto("some@email.com", "Пётр Иванов");
        service.saveUser(userDto1);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user1 = query.setParameter("email", userDto1.getEmail()).getSingleResult();

        service.deleteUser(user1.getId());
        query = em.createQuery("Select u from User u where u.email = :email", User.class);
        List<User> resultList = query.setParameter("email", userDto1.getEmail()).getResultList();
        assertThat(resultList, equalTo(List.of()));
    }

    private UserDto makeUserDto(String email, String name) {
        return UserDto.builder()
                .email(email)
                .name(name)
                .build();
    }

    private List<User> getUsers() {
        return new ArrayList<>(
                List.of(
                        User.builder()
                                .id(1)
                                .name("Имя пользователя 1")
                                .email("email1@email.com")
                                .build(),
                        User.builder()
                                .id(2)
                                .name("Имя пользователя 2")
                                .email("email2@email.com")
                                .build()
                )
        );
    }
}