package com.hx.tools;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class Tool {


    public static void main(String[] args) {
        UserInfo userInfoPar = new UserInfo();
        userInfoPar.setUsername("hx");
        userInfoPar.setId(1L);
        userInfoPar.setAddress("1号小区");
        userInfoPar.setParentId(0L);
        userInfoPar.setLevel("今年是年");
        log.info(userInfoPar.toString());

    }
}
