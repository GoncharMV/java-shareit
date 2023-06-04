package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.CustomServerErrorException;

public class PageableUtil {

    private PageableUtil() {
    }

    public static Pageable pageManager(int from, int size, String sort) {
        if (size < 0 || from < 0) {
            throw new CustomServerErrorException("Невалидный индекс");
        }

        return PageRequest.of(
                from == 0 ? 0 : (from / size),
                size,
                Sort.by(Sort.Direction.DESC, sort)
        );
    }
}
