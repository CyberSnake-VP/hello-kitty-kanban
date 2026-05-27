package ru.example.webtest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.webtest.dao.RecordDao;
import ru.example.webtest.entity.Record;
import ru.example.webtest.entity.RecordStatus;
import ru.example.webtest.entity.dto.RecordsContainerDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordService {
    private final RecordDao recordDao;

    public RecordsContainerDto findAllRecords(String filterMode) {

        int numberOfDoneRecords = recordDao.countByStatus(RecordStatus.DONE);
        int numberOfActiveRecords = recordDao.countByStatus(RecordStatus.ACTIVE);


        List<Record> records = List.of();

        if(filterMode == null || filterMode.isBlank()) {
           records = recordDao.findAll();
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }

        String filterModeInUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterModes = List.of("DONE", "ACTIVE");

        if(allowedFilterModes.contains(filterModeInUpperCase)) {
            records = recordDao.findAllByStatus(RecordStatus.valueOf(filterModeInUpperCase));
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }

        return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
    }

    public void saveRecord(String title) {
        if(title != null && !title.isBlank()) {
            Record record = new Record();
            record.setTitle(title);
            recordDao.save(record);
        }
    }

    public void updateRecordStatus(int id, RecordStatus newStatus) {
        recordDao.updateRecordStatus(id, newStatus);
    }

    public void deleteRecord(int id) {
        recordDao.delete(id);
    }
}
