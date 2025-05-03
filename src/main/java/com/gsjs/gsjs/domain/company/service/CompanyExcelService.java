package com.gsjs.gsjs.domain.company.service;

import com.gsjs.gsjs.domain.common.Industry;
import com.gsjs.gsjs.domain.common.Region;
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
            List<Company> companiesList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue; // 첫 번째 줄은 헤더이므로 건너뜀

                Company company = rowToCompany(row);
                companiesList.add(company);

                //BATCH_SIZE 초과시 저장
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
    /**
     * 강소기업 엑셀 구조
     * Column
     * 0: 연번
     * 1: 사업자명 ex: (주)석경에이티 - String
     * 2: 사업자등록번호 -unique key - String
     * 3: 업종명(대분류) - enum
     * 4: 소재지 - address - string
     * 5: 지역 ex: 경기, 경남 .. - enum
     * 6: 우편번호 - string
     * 7: 사업장업종상세정보 - string
     */
    private Company rowToCompany(Row row) {
        String name = getCellString(row, 1); // 사업자명
        String bizNo = getCellString(row, 2); // 사업자등록번호
        String industry = getCellString(row, 3); // 업종명(대분류)
        String address = getCellString(row, 4); // 소재지
        String region = getCellString(row, 5); // 지역
        String postalCode = getCellString(row, 6); // 우편번호
        String industryDetail = getCellString(row, 7); // 사업장업종상세정보

        Region regionEnum = Region.fromName(region);
        Industry industryEnum = Industry.fromName(industry);

        return Company.create(name, bizNo, address, industryEnum, industryDetail, regionEnum, postalCode);
    }

    private String getCellString(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

}