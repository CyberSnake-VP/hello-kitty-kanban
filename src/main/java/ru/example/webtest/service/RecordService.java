package ru.example.webtest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.webtest.entity.User;
import ru.example.webtest.repository.dao.RecordDao;
import ru.example.webtest.entity.Record;
import ru.example.webtest.entity.RecordStatus;
import ru.example.webtest.entity.dto.RecordsContainerDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecordService {
    private final RecordDao recordDao;
    private final UserService userService;

    public RecordsContainerDto findAllRecords(String filterMode) {
        User user = userService.getCurrntUser();
        List<Record> records = user.getRecords();

        long numberOfDoneRecords = records.stream()
                .filter(record -> record.getStatus() == RecordStatus.DONE)
                .count();

        long numberOfActiveRecords = records.stream()
                .filter(record -> record.getStatus() == RecordStatus.ACTIVE)
                .count();

        if (filterMode == null || filterMode.isBlank()) {
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        }

        String filterModeInUpperCase = filterMode.toUpperCase();
        List<String> allowedFilterModes = List.of("DONE", "ACTIVE");

        if (allowedFilterModes.contains(filterModeInUpperCase)) {
            List<Record> filterRecords = records.stream()
                    .filter(record -> record.getStatus().name().equals(filterModeInUpperCase))
                    .toList();
            return new RecordsContainerDto(user.getName(), filterRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    @Transactional
    public void saveRecord(String formTitle) {
        if (formTitle != null && !formTitle.isBlank()) {
            User user = userService.getCurrntUser();
            Record record = new Record();
            record.setUser(user);
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
