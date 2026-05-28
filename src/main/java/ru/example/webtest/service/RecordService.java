package ru.example.webtest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.webtest.dao.RecordDao;
import ru.example.webtest.entity.Record;
import ru.example.webtest.entity.RecordStatus;
import ru.example.webtest.entity.dto.RecordsContainerDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordService {
    private final RecordDao recordDao;

    public RecordsContainerDto findAllRecords(String filterMode) {

        long numberOfDoneRecords = recordDao.countByStatus(RecordStatus.DONE);
        long numberOfActiveRecords = recordDao.countByStatus(RecordStatus.ACTIVE);


        List<Record> records;

        if(filterMode == null || filterMode.isBlank()) {
           records = recordDao.findAll();
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }

        String filterModeInUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterModes = List.of("DONE", "ACTIVE");

        if(allowedFilterModes.contains(filterModeInUpperCase)) {
            records = recordDao.findAllByStatus(RecordStatus.valueOf(filterModeInUpperCase));
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            records = recordDao.findAll();
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    @Transactional
    public void saveRecord(String formTitle) {
        if(formTitle != null && !formTitle.isBlank()) {
            Record record = new Record();
            record.setTitle(formTitle);
            recordDao.save(record);
        }
    }

    @Transactional
    public void updateRecordStatus(int id, RecordStatus newStatus) {
        recordDao.updateRecordStatus(id, newStatus);
    }

    @Transactional
    public void deleteRecord(int id) {
        recordDao.delete(id);
    }
}
