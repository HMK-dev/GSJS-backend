package com.gsjs.gsjs.domain.company.service;

import com.gsjs.gsjs.domain.common.Industry;
import com.gsjs.gsjs.domain.company.entity.Company;
import com.gsjs.gsjs.domain.company.repository.CompanyRepository;
import com.gsjs.gsjs.exception.object.domain.FileHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CompanyExcelService {

    private final CompanyRepository companyRepository;

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

            // 기업들 정보를 저장하는 List
            // 지역별 좌표 리스트를 저장하는 Map
            List<Company> companiesList = new ArrayList<>();


            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue; // 첫 번째 줄은 헤더이므로 건너뜀

                if (isBatchSizeReached(companiesList)) {
                    saveCompanies(companiesList);
                    companiesList.clear();
                }

            }

            // 나머지 저장
            saveCompanies(companiesList);


        } catch (FileNotFoundException e) {
            throw new FileHandler(ErrorStatus.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new FileHandler(ErrorStatus.FILE_READ_ERROR);
        }
    }

    //todo refactoring -> BatchUpdate
    private void saveCompanies(List<Company> companies) {
        companyRepository.saveAll(companies);
    }

    private boolean isBatchSizeReached(List<Company> companies) {
        return companies.size() >= BATCH_SIZE;
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
        //todo region

        return Company.create(name, address, industry, null);
    }

    private String getCellString(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

}3