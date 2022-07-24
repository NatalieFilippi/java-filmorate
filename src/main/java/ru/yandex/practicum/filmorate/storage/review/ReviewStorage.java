package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review create(Review review);

    Review getById(int reviewId) throws ObjectNotFoundException;

    List<Review> getAllReviews();

    Review update(Review review) throws ObjectNotFoundException;

    void deleteById(int reviewId);

    List<Review> getReviewsForFilm(Long filmId, int count);

    void updateLike(int useful, Integer reviewId);
}