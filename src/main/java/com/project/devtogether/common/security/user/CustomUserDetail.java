package com.project.devtogether.common.security.user;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/*
* 추가적인 정보를 담기 위해 기본 Spring Security에서 제공하는 User를 상속받아 custommodel 구현
* */
@Getter
public class CustomUserDetail extends User {

    private final Long id;

    public CustomUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities,
                            Long id) {
        super(username, password, authorities);
        this.id = id;
    }
}
