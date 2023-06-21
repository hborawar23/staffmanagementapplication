package com.SDS.staffmanagement.configuration;
import com.SDS.staffmanagement.entities.BaseLoginEntity;
import com.SDS.staffmanagement.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
public class CustomUserDetails implements UserDetails {
//    public CustomUserDetails(User user) {
//        super();
//        this.user = user;
//    }

    private BaseLoginEntity baseLogin;
    public CustomUserDetails(BaseLoginEntity baseLogin) {
        this.baseLogin = baseLogin;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(baseLogin.getRole());
        return List.of(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        System.out.println(baseLogin.getPassword() + "**********");
        return baseLogin.getPassword();
    }

    @Override
    public String getUsername() {
        return baseLogin.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
