package com.SDS.staffmanagement.configuration;
import com.SDS.staffmanagement.entities.BaseLoginEntity;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.BaseLoginRepository;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    BaseLoginRepository baseLoginRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            BaseLoginEntity baseLogin = baseLoginRepository.findByEmail(email);
            String myRole = baseLogin.getRole();
            if (myRole.contains("ROLE_STAFF") || myRole.contains("ROLE_MANAGER" )|| myRole.contains("ROLE_HR")) {
                if (baseLogin.isApprovedStatus()) {
                    if (baseLogin != null) {
                        return new CustomUserDetails(baseLogin);
                    }
                }
            } else {
                if (baseLogin != null) {
                    return new CustomUserDetails(baseLogin);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        throw new UsernameNotFoundException("User not available");
    }
}
