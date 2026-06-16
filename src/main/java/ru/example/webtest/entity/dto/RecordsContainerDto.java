package ru.example.webtest.entity.dto;

import ru.example.webtest.entity.Record;

import java.util.List;

public record RecordsContainerDto(
        String userName,
        List<Record> records,
        long numberOfDoneRecords,
        long numberOfActiveRecords
) {

}
