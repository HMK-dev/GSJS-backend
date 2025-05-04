package com.gsjs.gsjs.application.employeeMonthlyStats.excel;

import com.gsjs.gsjs.domain.employeeMonthlyStats.service.EmployeeMonthlyStatsExcelService;
import com.gsjs.gsjs.global.annotation.usecase.UseCase;
import com.gsjs.gsjs.presentation.employeeMonthlyStats.dto.request.EmployeeMonthlyStatsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@Transactional
@RequiredArgsConstructor
public class ImportEmployeeMonthlyStatsUseCase {

    private final EmployeeMonthlyStatsExcelService employeeMonthlyStatsExcelService;

    public void execute(EmployeeMonthlyStatsRequest.Files request) {
        employeeMonthlyStatsExcelService.importEmployeeDataFromExcelFiles(request.getFiles());
    }

}
