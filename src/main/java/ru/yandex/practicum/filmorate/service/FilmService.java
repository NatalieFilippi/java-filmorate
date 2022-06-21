package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(long filmId, long userId) throws ObjectNotFoundException {
        if (filmStorage.findById(filmId) == null) {
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (userStorage.findById(userId) == null) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        filmStorage.findById(filmId).addLike(userStorage.findById(userId));
        return filmStorage.findById(filmId);
    }

    public Film deleteLike(long filmId, long userId) throws ObjectNotFoundException {
        if (filmStorage.findById(filmId) == null) {
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (userStorage.findById(userId) == null) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        filmStorage.findById(userId).deleteLike(userStorage.findById(userId));
        return filmStorage.findById(userId);
    }

    public List<Film> findByRating(int count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2) -> {
                    if(o1.getLikes().size() == o2.getLikes().size())
                        return 0;
                    else if(o1.getLikes().size() > o2.getLikes().size())
                        return -1;
                    else return 1;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

}
