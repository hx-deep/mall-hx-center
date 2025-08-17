package com.hx.tools;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class UserInfo {

    private Long id;

    private Long parentId;

    private String username;

    private String address;

    private UserInfo child;

    private Integer level;

    public void setLevel(String level) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(level);
        if (matcher.find()) {
            this.level = Integer.valueOf(matcher.group());
        }

    }


}
