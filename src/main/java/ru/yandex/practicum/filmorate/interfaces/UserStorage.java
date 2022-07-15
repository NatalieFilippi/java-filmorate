package ru.yandex.practicum.filmorate.interfaces;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    List<User> findAll();
    User findById(long id) throws ObjectNotFoundException;
    User create(User user);
    User put(User user) throws  ObjectNotFoundException;
    void deleteAll();
    User delete(User user) throws ObjectNotFoundException;
    void addFriend(Long userId, Long friendId);
    boolean deleteFriend(Long userId, Long friendId);
    List<User> getFriends(Long userId);
    List<User> getCommonFriends(Long userId, Long otherId);
}
