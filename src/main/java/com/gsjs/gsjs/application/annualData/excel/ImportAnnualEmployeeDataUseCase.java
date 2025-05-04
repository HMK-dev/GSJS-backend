package com.gsjs.gsjs.application.annualData.excel;

import com.gsjs.gsjs.domain.annualData.service.AnnualDataExcelService;
import com.gsjs.gsjs.global.annotation.usecase.UseCase;
import com.gsjs.gsjs.presentation.annualData.dto.request.AnnualDataRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@UseCase
@Transactional
@RequiredArgsConstructor
public class ImportAnnualEmployeeDataUseCase {

    private final AnnualDataExcelService annualDataExcelService;

    public void execute(AnnualDataRequest.EmployeeFiles request) {
        annualDataExcelService.importAnnulDataFromExcelFiles(request.getEmployeeFiles());
    }

}
