package com.gsjs.gsjs.exception.object.domain;

import com.gsjs.gsjs.exception.object.general.GeneralException;
import com.gsjs.gsjs.exception.payload.code.BaseCode;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseCode code) {
        super(code);
    }
}