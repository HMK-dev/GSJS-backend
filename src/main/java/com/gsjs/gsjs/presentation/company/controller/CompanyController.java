package com.gsjs.gsjs.presentation.company.controller;

import com.gsjs.gsjs.application.company.excel.ImportCompaniesUseCase;
import com.gsjs.gsjs.exception.payload.code.SuccessStatus;
import com.gsjs.gsjs.exception.payload.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "2000", description = "OK")
@Tag(name = "Member Api", description = "멤버 API")
@RequestMapping("/company")
public class CompanyController {

    private final ImportCompaniesUseCase importCompaniesUseCase;

    //test
    //todo admin
    @Operation(summary = "엑셀 파일로 기업 정보 등록", description = "엑셀 파일을 통해 기업 정보를 등록합니다.")
    @GetMapping("/import")
    public ApiResponseDto<?> importCompanies(@RequestParam("fileName") String fileName) {
        importCompaniesUseCase.execute(fileName);
        return ApiResponseDto.onSuccess(SuccessStatus._SUCCESS);
    }


}
