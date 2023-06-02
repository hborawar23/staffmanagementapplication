package com.SDS.staffmanagement.configuration;
import com.SDS.staffmanagement.entities.User;
import com.SDS.staffmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fetching user from database
        System.out.println("Inside UserDetailService */*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*");
        User user = userRepository.getUserByUserName(username);
        System.out.println(user.getEmail() + "***********+++++++++++++++");
        if(user==null)
        {
            throw new UsernameNotFoundException("Could not found user!!");
        }
        CustomUserDetails customUserDetails=new CustomUserDetails(user);
        return customUserDetails;
    }
}
