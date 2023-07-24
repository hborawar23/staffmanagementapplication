package com.staffmanagement.services;

import com.staffmanagement.entities.Project;
import com.staffmanagement.entities.User;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface HrService {

    boolean updateUser(User user, HttpSession session);

    boolean updateProject(Project project, HttpSession session,List<String> errorMsgList);

    String processUpdateProject(Project project, HttpSession session, Model model);
}
