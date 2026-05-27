package ru.example.webtest.entity.dto;

import ru.example.webtest.entity.Record;

import java.util.List;

public record RecordsContainerDto(
        List<Record> records,
        int numberOfDoneRecords,
        int numberOfActiveRecords
) {

}
