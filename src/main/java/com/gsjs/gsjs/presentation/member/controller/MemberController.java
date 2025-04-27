package com.gsjs.gsjs.presentation.member.controller;

import com.gsjs.gsjs.exception.payload.code.SuccessStatus;
import com.gsjs.gsjs.exception.payload.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@ApiResponse(responseCode = "2000", description = "OK")
@Tag(name = "Member Api", description = "멤버 API")
@RequestMapping("/member")
public class MemberController {

    @Operation(summary = "test summary", description = "test description")
    @GetMapping("/test")
    public ApiResponseDto<?> test() {
        return ApiResponseDto.onSuccess(SuccessStatus._SUCCESS);
    }

}
