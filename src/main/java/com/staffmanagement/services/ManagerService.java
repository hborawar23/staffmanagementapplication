package com.staffmanagement.services;

import com.staffmanagement.entities.Manager;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ManagerService {

    String  registerManager(Manager manager, BindingResult result, Model model, HttpSession session);

    boolean existByEmail(String email);

    List<Manager> getAllManagers();


    List<Manager> getAllInWaitingList();

    List<String> saveManager(Manager manager);

    List<String> updateManager(Manager manager);

    void savingIntoBaseLoginEntity(Manager manager);
}
