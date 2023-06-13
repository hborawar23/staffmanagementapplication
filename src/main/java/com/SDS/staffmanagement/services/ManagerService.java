package com.SDS.staffmanagement.services;

import com.SDS.staffmanagement.entities.Manager;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;

public interface ManagerService {

    String registerManager(Manager manager, boolean agreement, BindingResult result, Model model, HttpSession session);

}
