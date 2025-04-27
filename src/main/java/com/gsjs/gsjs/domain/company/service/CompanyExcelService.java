package com.gsjs.gsjs.domain.company.service;

import com.gsjs.gsjs.domain.common.Industry;
import com.gsjs.gsjs.domain.company.entity.Company;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyExcelService {

    private final int BATCH_SIZE = 1000;

    @Value("${file.gangso-company.path}")
    private String FILE_PATH;

    public void importCompaniesFromExcel(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(FILE_PATH + fileName);
            FileInputStream fis = new FileInputStream(resource.getFile());

            //excel -> Apache POI workbook 객체로 로드
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            Iterator<Row> rowIterator = sheet.iterator(); // 반복 객체 생성

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue; // 첫 번째 줄은 헤더이므로 건너뜀

            }

        } catch (FileNotFoundException e) {
            //todo implement exception
            throw new IllegalArgumentException();
        } catch (IOException e) {
            //todo implement exception
            throw new IllegalArgumentException();
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true; //row 자체가 null이면 빈 행으로 처리

        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; //
            }
        }
        return true;
    }

    //excel row -> Company
    private Company rowToCompany(Row row) {
        String name = getCellString(row, 1); //B
        String industryName = getCellString(row, 2); //C
        String address = getCellString(row, 3);//D

        Industry industry = Industry.fromName(industryName);

        return Company.builder().build();
    }

    private String getCellString(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

}