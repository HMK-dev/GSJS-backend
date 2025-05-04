package com.gsjs.gsjs.presentation.employeeMonthlyStats.controller;

import com.gsjs.gsjs.application.employeeMonthlyStats.excel.ImportEmployeeMonthlyStatsUseCase;
import com.gsjs.gsjs.exception.payload.code.SuccessStatus;
import com.gsjs.gsjs.exception.payload.dto.ApiResponseDto;
import com.gsjs.gsjs.presentation.employeeMonthlyStats.dto.request.EmployeeMonthlyStatsRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "2000", description = "OK")
@Tag(name = "EmployeeMonthlyStats Api", description = "연간 근로자 수 데이터 API")
@RequestMapping("/employeeMonthlyStats")
public class EmployeeMonthlyStatsController {

    private final ImportEmployeeMonthlyStatsUseCase importEmployeeMonthlyStatsUseCase;

    //test
    //todo admin
    @Operation(summary = "엑셀 파일로 기업 연간 국민연금 데이터 정보 등록",
            description = "엑셀 파일을 통해 기업 연간 국민연금 가입자 정보를 등록합니다.")
    @PostMapping("/import")
    public ApiResponseDto<?> importAnnualEmployeeData(@RequestBody EmployeeMonthlyStatsRequest.Files request) {
        importEmployeeMonthlyStatsUseCase.execute(request);
        return ApiResponseDto.onSuccess(SuccessStatus._SUCCESS);
    }

}
