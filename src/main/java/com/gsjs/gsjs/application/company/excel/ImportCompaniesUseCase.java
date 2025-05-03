package com.gsjs.gsjs.application.company.excel;

import com.gsjs.gsjs.domain.company.service.CompanyExcelService;
import com.gsjs.gsjs.global.annotation.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@Transactional
@RequiredArgsConstructor
public class ImportCompaniesUseCase {

    private final CompanyExcelService companyExcelService;

    public void execute(String fileName) {
        log.info("Importing companies from Excel file: {}", fileName);
        companyExcelService.importCompaniesFromExcel(fileName);
        log.info("Successfully imported companies from Excel file: {}", fileName);
    }

}
