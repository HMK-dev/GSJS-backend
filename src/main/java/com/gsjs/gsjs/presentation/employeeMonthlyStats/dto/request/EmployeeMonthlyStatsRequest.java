package com.gsjs.gsjs.presentation.employeeMonthlyStats.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class EmployeeMonthlyStatsRequest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Files {
        private List<MultipartFile> files;
    }

}
