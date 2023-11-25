package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, long userId);

    ItemDto updateItem(ItemDto itemDto, long itemId, long userId);

    ItemDto findItemDtoById(long itemId, long userId);

    List<ItemDto> findItemsByUserId(long userId);

    List<ItemDto> searchItem(String text);

    CommentDto saveComment(CommentDto commentDto, long itemId, long userId);
}
