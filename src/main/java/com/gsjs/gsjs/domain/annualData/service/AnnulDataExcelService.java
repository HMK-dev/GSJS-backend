package com.gsjs.gsjs.domain.annualData.service;

import com.gsjs.gsjs.domain.annualData.entity.AnnualData;
import com.gsjs.gsjs.domain.annualData.repository.AnnualDataRepository;
import com.gsjs.gsjs.domain.common.Industry;
import com.gsjs.gsjs.domain.common.Region;
import com.gsjs.gsjs.domain.company.adaptor.CompanyAdaptor;
import com.gsjs.gsjs.domain.company.entity.Company;
import com.gsjs.gsjs.exception.object.domain.FileHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AnnulDataExcelService {

    private final CompanyAdaptor companyAdaptor;
    private final AnnualDataRepository annualDataRepository;

    private final int BATCH_SIZE = 1000;

    @Value("${file.pension.path}")
    private String FILE_PATH;

    public void importAnnulDataFromExcelFiles(List<MultipartFile> multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            importAnnualDataFromExcel(multipartFile);
        }
    }

    public void importAnnualDataFromExcel(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()){
            //excel -> Apache POI workbook 객체로 로드
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            Iterator<Row> rowIterator = sheet.iterator(); // 반복 객체 생성

            // 기업들 정보를 저장하는 List
            List<AnnualData> annualDataList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue; // 첫 번째 줄은 헤더이므로 건너뜀

                AnnualData annualData = rowToAnnualData(row);
                annualDataList.add(annualData);

                //BATCH_SIZE 초과시 저장
                if (isBatchSizeReached(annualDataList)) {
                    saveAllAnnualData(annualDataList);
                    annualDataList.clear();
                }

            }

            // 나머지 저장
            saveAllAnnualData(annualDataList);

        } catch (FileNotFoundException e) {
            throw new FileHandler(ErrorStatus.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new FileHandler(ErrorStatus.FILE_READ_ERROR);
        }
    }

    //todo refactoring -> BatchUpdate
    private void saveAllAnnualData(List<AnnualData> annualDataList) {
        annualDataRepository.saveAll(annualDataList);
    }

    private <T> boolean isBatchSizeReached(List<T> list) {
        return list.size() >= BATCH_SIZE;
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

    //excel row -> AnnualData
    /**
     * 강소기업 엑셀 구조
     * Column
     * 0: 작성 날짜 ex: 2018-05, delimiter = "-"
     * 1: 사업자명 ex: (주)석경에이티 - String
     * 2: 사업자등록번호 -unique key - String
     * 18: 전체 가입자 수 - Integer - S col
     * 20: 신규 가입자 수 - Integer - U col
     * 21: 상실 가입자 수 - Integer - V col
     */
    private AnnualData rowToAnnualData(Row row) {
        String date = getCellString(row, 0); // 작성 날짜
        Integer year = Integer.parseInt(date.split("-")[0]); // 연도
        Integer month = Integer.parseInt(date.split("-")[1]); // 월

        String bizNo = getCellString(row, 2); // 사업자등록번호

        String totalEmployees = getCellString(row, 18); // 전체 가입자 수
        String newEmployees = getCellString(row, 20); // 신규 가입자 수
        String lostEmployees = getCellString(row, 21); // 상실 가입자 수

        Company company = companyAdaptor.queryByBizNo(bizNo);
        AnnualData annualData = AnnualData.create(year, month);
        annualData.setCompany(company);
        annualData.updateEmployeeData(
                Integer.parseInt(totalEmployees),
                Integer.parseInt(newEmployees),
                Integer.parseInt(lostEmployees)
        );

        return annualData;
    }

    private String getCellString(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

}
