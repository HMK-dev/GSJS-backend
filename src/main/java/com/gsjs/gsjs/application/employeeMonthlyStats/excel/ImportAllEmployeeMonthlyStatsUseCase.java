package com.gsjs.gsjs.application.employeeMonthlyStats.excel;

import com.gsjs.gsjs.domain.employeeMonthlyStats.service.EmployeeMonthlyStatsExcelService;
import com.gsjs.gsjs.global.annotation.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@UseCase
@Transactional
@RequiredArgsConstructor
public class ImportAllEmployeeMonthlyStatsUseCase {

    private final EmployeeMonthlyStatsExcelService employeeMonthlyStatsExcelService;

    public void execute(List<MultipartFile> files) {
        employeeMonthlyStatsExcelService.importEmployeeDataFromExcelFiles(files);
    }

}
