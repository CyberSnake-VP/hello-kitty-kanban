package ru.example.webtest.entity.dto;

import java.util.List;

public record RecordsContainerDto(
        List<Record> records,
        int numberOfDoneRecords,
        int numberOfActiveRecords
) {

}
