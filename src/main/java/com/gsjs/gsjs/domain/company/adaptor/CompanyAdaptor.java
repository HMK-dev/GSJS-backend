package com.gsjs.gsjs.domain.company.adaptor;

import com.gsjs.gsjs.domain.company.entity.Company;
import com.gsjs.gsjs.domain.company.repository.CompanyRepository;
import com.gsjs.gsjs.exception.object.domain.CompanyHandler;
import com.gsjs.gsjs.exception.payload.code.ErrorStatus;
import com.gsjs.gsjs.global.annotation.adaptor.Adaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Adaptor
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyAdaptor {

    private final CompanyRepository repository;

    public Company queryByBizNo(String bizNo) {
        return repository.findByBizNo(bizNo).orElseThrow(
                () -> new CompanyHandler(ErrorStatus.COMPANY_NOT_FOUND)
        );
    }

}
