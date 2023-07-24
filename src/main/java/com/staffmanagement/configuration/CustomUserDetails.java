package com.staffmanagement.configuration;
import com.staffmanagement.entities.BaseLoginEntity;
import com.staffmanagement.repositories.ManagerRepository;
import com.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;
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
        String password = null;
        if (baseLogin.getRole().equalsIgnoreCase("ROLE_STAFF")) {
             password = baseLogin.getUser().getPassword();
        }
        if (baseLogin.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
             password = baseLogin.getManager().getPassword();
        }
        return password;
    }

    @Override
    public String getUsername() {
        String username = null;
        if (baseLogin.getRole().equalsIgnoreCase("ROLE_STAFF")) {
              username= baseLogin.getUser().getEmail();
        }
        if (baseLogin.getRole().equalsIgnoreCase("ROLE_MANAGER")) {
            username= baseLogin.getManager().getEmail();
        }
        return username;
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
