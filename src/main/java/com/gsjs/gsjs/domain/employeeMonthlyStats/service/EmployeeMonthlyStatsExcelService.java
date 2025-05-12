package com.gsjs.gsjs.domain.employeeMonthlyStats.service;

import com.gsjs.gsjs.domain.employeeMonthlyStats.entity.EmployeeMonthlyStats;
import com.gsjs.gsjs.domain.employeeMonthlyStats.repository.EmployeeMonthlyStatsRepository;
import com.gsjs.gsjs.domain.company.adaptor.CompanyAdaptor;
import com.gsjs.gsjs.domain.company.entity.Company;
import com.gsjs.gsjs.exception.object.domain.FileHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeMonthlyStatsExcelService {

    private final CompanyAdaptor companyAdaptor;
    private final EmployeeMonthlyStatsRepository employeeMonthlyStatsRepository;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final int BATCH_SIZE = 5000;

    public void importEmployeeDataFromExcelFiles(List<MultipartFile> multipartFiles) {
        for (MultipartFile multipartFile : multipartFiles) {
            importEmployeeDataFromExcel(multipartFile);
        }
    }

    public void importEmployeeDataFromExcel(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            //excel -> Apache POI workbook 객체로 로드
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            Iterator<Row> rowIterator = sheet.iterator(); // 반복 객체 생성

            // 기업들 정보를 저장하는 List
            List<EmployeeMonthlyStats> employeeMonthlyStatsList = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue; // 첫 번째 줄은 헤더이므로 건너뜀

                EmployeeMonthlyStats employeeMonthlyStats = rowToEmployeeData(row);
                employeeMonthlyStatsList.add(employeeMonthlyStats);

                //BATCH_SIZE 초과시 저장
                if (isBatchSizeReached(employeeMonthlyStatsList)) {
//                    saveAllEmployeeDataBatch(employeeMonthlyStatsList);
                    saveAllEmployeeDataBatchV2(employeeMonthlyStatsList);
                    employeeMonthlyStatsList.clear();
                }

            }

            // 나머지 저장
//            saveAllEmployeeDataBatch(employeeMonthlyStatsList);
            saveAllEmployeeDataBatchV2(employeeMonthlyStatsList);

        } catch (FileNotFoundException e) {
            throw new FileHandler(ErrorStatus.FILE_NOT_FOUND);
        } catch (IOException e) {
            throw new FileHandler(ErrorStatus.FILE_READ_ERROR);
        }
    }

    //todo resolve duplicate error
    //BatchUpdate
    private void saveAllEmployeeDataBatch(List<EmployeeMonthlyStats> employeeMonthlyStatsList) {
        if (employeeMonthlyStatsList.isEmpty()) return;
        //BatchInsert
        String sql = "INSERT INTO employee_monthly_stats (year, month, total_employees, new_employees, lost_employees, company_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +  // DUPLICATE KEY 발생시 UPDATE
                "total_employees = VALUES(total_employees), " +
                "new_employees = VALUES(new_employees), " +
                "lost_employees = VALUES(lost_employees)";

        int batchSize = employeeMonthlyStatsList.size();
        SqlParameterSource[] batch = new SqlParameterSource[batchSize];
        for (int i = 0; i < batchSize; i++) {
            EmployeeMonthlyStats stats = employeeMonthlyStatsList.get(i);
            batch[i] = new MapSqlParameterSource()
                    .addValue("year", stats.getYear())
                    .addValue("month", stats.getMonth())
                    .addValue("total_employees", stats.getTotalEmployees())
                    .addValue("new_employees", stats.getNewEmployees())
                    .addValue("lost_employees", stats.getLostEmployees())
                    .addValue("company_id", stats.getCompany().getId());
        }

        try {
            namedParameterJdbcTemplate.batchUpdate(sql, batch);
        } catch (DataIntegrityViolationException e) {
            // 배치 전체가 실패할 경우 개별적으로 처리
            handleBatchFailure(employeeMonthlyStatsList, sql);
            log.info("배치 전체 실패, 개별 실행");
        } catch (Exception e) {
            log.info("배치 저장 중 오류 발생: {}", e.getMessage());
        }
    }

    private void saveAllEmployeeDataBatchV2(List<EmployeeMonthlyStats> employeeMonthlyStatsList) {
        if (employeeMonthlyStatsList.isEmpty()) return;

        String sql = "INSERT INTO employee_monthly_stats " +
                "(year, month, total_employees, new_employees, lost_employees, company_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "total_employees = VALUES(total_employees), " +
                "new_employees = VALUES(new_employees), " +
                "lost_employees = VALUES(lost_employees)";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    EmployeeMonthlyStats stats = employeeMonthlyStatsList.get(i);
                    ps.setInt(1, stats.getYear());
                    ps.setInt(2, stats.getMonth());
                    ps.setInt(3, stats.getTotalEmployees());
                    ps.setInt(4, stats.getNewEmployees());
                    ps.setInt(5, stats.getLostEmployees());
                    ps.setLong(6, stats.getCompany().getId());
                }

                @Override
                public int getBatchSize() {
                    return employeeMonthlyStatsList.size();
                }
            });
        } catch (DataIntegrityViolationException e) {
            // 배치 전체가 실패하면 개별 insert 시도
            handleBatchFailureV2(employeeMonthlyStatsList, sql);
            log.info("배치 전체 실패, 개별 실행");
        } catch (Exception e) {
            log.info("배치 저장 중 오류 발생: {}", e.getMessage());
        }
    }


    // 배치 실패 시 개별 처리를 위한 메소드
    private void handleBatchFailure(List<EmployeeMonthlyStats> statsList, String sql) {
        for (EmployeeMonthlyStats stats : statsList) {
            try {
                MapSqlParameterSource params = new MapSqlParameterSource()
                        .addValue("year", stats.getYear())
                        .addValue("month", stats.getMonth())
                        .addValue("totalEmployees", stats.getTotalEmployees())
                        .addValue("newEmployees", stats.getNewEmployees())
                        .addValue("lostEmployees", stats.getLostEmployees())
                        .addValue("companyId", stats.getCompany().getId());

                namedParameterJdbcTemplate.update(sql, params);

            } catch (DataIntegrityViolationException e) {
                if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                    log.warn("중복 키로 인해 무시됨: year={}, month={}, companyId={}",
                            stats.getYear(), stats.getMonth(), stats.getCompany().getId());
                    continue;
                }
                // 그 외 예외는 다시 던지거나 로깅
                log.error("DataIntegrityViolationException 발생: {}", e.getMessage());


            }
        }
    }

    private void handleBatchFailureV2(List<EmployeeMonthlyStats> statsList, String sql) {
        for (EmployeeMonthlyStats stats : statsList) {
            try {
                jdbcTemplate.update(sql, ps -> {
                    ps.setInt(1, stats.getYear());
                    ps.setInt(2, stats.getMonth());
                    ps.setInt(3, stats.getTotalEmployees());
                    ps.setInt(4, stats.getNewEmployees());
                    ps.setInt(5, stats.getLostEmployees());
                    ps.setLong(6, stats.getCompany().getId());
                });
            } catch (DataIntegrityViolationException e) {
                // 개별 항목 저장 실패 시 로그만 남기고 계속 진행
                log.warn("중복 데이터 저장 시도 무시: year={}, month={}, companyId={}",
                        stats.getYear(), stats.getMonth(), stats.getCompany().getId());
            }
        }
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

    //excel row -> employeeData

    /**
     * 국민연금 엑셀 구조
     * Column
     * 0: 작성 날짜 ex: 2018-05, delimiter = "-"
     * 1: 사업자명 ex: (주)석경에이티 - String
     * 2: 사업자등록번호 -unique key - String
     * 18: 전체 가입자 수 - Integer - S col
     * 20: 신규 가입자 수 - Integer - U col
     * 21: 상실 가입자 수 - Integer - V col
     */
    private EmployeeMonthlyStats rowToEmployeeData(Row row) {
        String date = getCellString(row, 0); // 작성 날짜
        Integer year = Integer.parseInt(date.split("-")[0]); // 연도
        Integer month = Integer.parseInt(date.split("-")[1]); // 월

        String bizNo = getCellString(row, 2); // 사업자등록번호

        String totalEmployees = getCellString(row, 18); // 전체 가입자 수
        String newEmployees = getCellString(row, 20); // 신규 가입자 수
        String lostEmployees = getCellString(row, 21); // 상실 가입자 수

        Company company = companyAdaptor.queryByBizNo(bizNo);

        EmployeeMonthlyStats employeeMonthlyStats = EmployeeMonthlyStats.create(year, month,
                Integer.parseInt(totalEmployees), Integer.parseInt(newEmployees), Integer.parseInt(lostEmployees));

        company.addEmployeeMonthlyStats(employeeMonthlyStats);
        return employeeMonthlyStats;
    }

    private String getCellString(Row row, int index) {
        return row.getCell(index).getStringCellValue();
    }

}
