package ru.practicum.shareit.user.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class UserBookingDto {
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserBookingDto that = (UserBookingDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
