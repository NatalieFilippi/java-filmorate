package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.FilmService;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;


@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final static LocalDate DATE_BORN_MOVIE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final String NO_DATA_FOUND = "Данные о фильме не заполнены.";
    private static final String EMPTY_NAME = "Название фильма не может быть пустым.";
    private static final String MAX_DESCRIPTION_LENGTH = "Превышена максимальная длина описания — 200 символов";
    private static final String DURATION_IS_POSITIVE = "Продолжительность фильма должна быть больше 0";
    private static final String EARLY_RELEASE_DATE = "Дата релиза не может быть раньше даты 28.12.1895";

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) throws ObjectNotFoundException {
        return filmStorage.findById(id);
    }

    public Film create(Film film) throws ValidationException {
        String message = check(film);
        if (!message.isBlank()) {
            log.debug("Ошибка при попытке добавления нового фильма: " + message);
            throw new ValidationException(message);
        }
        log.debug(String.format("Добавлен новый фильм %d.", film.getId()));
        return filmStorage.create(film);
    }

    public Film put(Film film) throws ValidationException, ObjectNotFoundException {
        String message = check(film);
        if (!message.isBlank()) {
            log.debug("Ошибка при попытке редактирования фильма: " + message);
            throw new ValidationException(message);
        }
        log.debug(String.format("Изменения для фильма %d успешно приняты.", film.getId()));
        return filmStorage.put(film);
    }

    public Film addLike(long filmId, long userId) throws ObjectNotFoundException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        if (film == null) {
            log.debug(String.format("Ошибка при попытке лайкнуть фильм. Фильм %d не найден.",filmId));
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (user == null) {
            log.debug(String.format("Ошибка при попытке лайкнуть фильм. Пользователь %d не найден.",userId));
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        if (filmStorage.addLike(filmId, userId)) {
            log.debug(String.format("Пользователь %d лайкнул фильм %d",
                                    user.getId(), film.getId()));
        };
        return film;
    }

    public Film deleteLike(long filmId, long userId) throws ObjectNotFoundException {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        if (film == null) {
            log.debug(String.format("Ошибка при попытке лайкнуть фильм. Фильм %d не найден.",filmId));
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        if (user == null) {
            log.debug(String.format("Ошибка при попытке лайкнуть фильм. Пользователь %d не найден.",userId));
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        filmStorage.deleteLike(filmId, userId);
        log.debug(String.format("Пользователь %d удалил лайк у фильма %d",
                                user.getId(), film.getId()));
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void deleteAll() {
        log.debug("Все фильмы удалены из системы. :(");
        filmStorage.deleteAll();
    }

    public void delete(Film film) throws ValidationException, ObjectNotFoundException {
        String message = check(film);
        if (!message.isBlank()) {
            log.debug("Ошибка при попытке удаления фильма из системы: " + message);
            throw new ValidationException(message);
        }
        log.debug(String.format("Фильм %d удалён из системы.", film.getId()));
        filmStorage.delete(film);
    }

    public Mpa findMpaById(long id) throws ObjectNotFoundException {
        return filmStorage.findMpaById(id);
    }

    public List<Mpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }

    public Genre findGenreById(long id) throws ObjectNotFoundException {
        return filmStorage.findGenreById(id);
    }

    public List<Genre> findAllGenre() {
        return filmStorage.findAllGenre();
    }

    private String check(Film film) throws ValidationException {
        String message = "";
        if (film == null) {
            message = NO_DATA_FOUND;
        } else if (film.getName() == null || film.getName().isBlank()) {
            message = EMPTY_NAME;
        } else if (film.getDescription().length() > 200) {
            message = MAX_DESCRIPTION_LENGTH;
        } else if (film.getReleaseDate().isBefore(DATE_BORN_MOVIE)) {
            message = EARLY_RELEASE_DATE;
        } else if (film.getDuration() <= 0) {
            message = DURATION_IS_POSITIVE;
        }
        return message;
    }

}