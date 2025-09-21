// UserRegisterVO.java
package com.easy.entity.vo;

import lombok.Data;

@Data
public class UserRegisterVO {
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String code;   // 邮箱验证码
    private Boolean IsPrivate;
}
