package com.dudumarquess.namata_sys.service;

import com.dudumarquess.namata_sys.repository.MonthlyRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class MonthlyRecordService {

    private MonthlyRecordRepository monthlyRecordRepository;

    public MonthlyRecordService(MonthlyRecordRepository monthlyRecordRepository) {
        this.monthlyRecordRepository = monthlyRecordRepository;
    }


}
