package com.gsjs.gsjs.application.company.excel;

import com.gsjs.gsjs.domain.company.service.CompanyExcelService;
import com.gsjs.gsjs.global.annotation.usecase.UseCase;
import com.gsjs.gsjs.presentation.company.dto.request.CompanyRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@Transactional
@RequiredArgsConstructor
public class ImportCompaniesUseCase {

    private final CompanyExcelService companyExcelService;

    public void execute(CompanyRequest.File request) {
        log.info("Importing companies from Excel file: {}", request.getFile().getName());
        companyExcelService.importCompaniesFromExcel(request.getFile());
        log.info("Successfully imported companies from Excel file: {}", request.getFile().getName());
    }

}
