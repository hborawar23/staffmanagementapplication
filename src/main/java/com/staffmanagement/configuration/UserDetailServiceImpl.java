package com.staffmanagement.configuration;
import com.staffmanagement.entities.BaseLoginEntity;
import com.staffmanagement.entities.Manager;
import com.staffmanagement.entities.User;
import com.staffmanagement.repositories.BaseLoginRepository;
import com.staffmanagement.repositories.ManagerRepository;
import com.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    BaseLoginRepository baseLoginRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
            Manager manager = managerRepository.findByEmail(email);


            if(manager.getRole().equalsIgnoreCase("ROLE_MANAGER")){
                if(manager.getBaseLoginEntity().isApprovedStatus()){
                    BaseLoginEntity baseLogin = manager.getBaseLoginEntity();
                    return new CustomUserDetails(baseLogin);
                }
            } else {
                User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
                if (user.getRole().equalsIgnoreCase("ROLE_STAFF")){
                    if(user.getBaseLoginEntity().isApprovedStatus()){
                        BaseLoginEntity baseLogin = user.getBaseLoginEntity();
                        return new CustomUserDetails(baseLogin);
                    }
                }
            }


//            BaseLoginEntity baseLogin = baseLoginRepository.findByEmail(email);
//
//            String myRole = baseLogin.getRole();
//            if (myRole.contains("ROLE_STAFF") || myRole.contains("ROLE_MANAGER" )|| myRole.contains("ROLE_HR")) {
//                if (baseLogin.isApprovedStatus()) {
//                    if (baseLogin != null) {
//                        return new CustomUserDetails(baseLogin);
//                    }
//                }
//            } else {
//                if (baseLogin != null) {
//                    return new CustomUserDetails(baseLogin);
//                }
//            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        throw new UsernameNotFoundException("User not available");
    }
}
