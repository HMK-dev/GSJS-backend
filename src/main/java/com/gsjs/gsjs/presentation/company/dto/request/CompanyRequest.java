package com.gsjs.gsjs.presentation.company.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CompanyRequest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class File {
        private MultipartFile file;
    }

}
