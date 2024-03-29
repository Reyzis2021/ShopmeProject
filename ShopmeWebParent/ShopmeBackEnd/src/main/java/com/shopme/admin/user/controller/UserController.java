package com.shopme.admin.user.controller;

import com.shopme.admin.user.exceptions.UserNotFoundException;
import com.shopme.admin.user.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public String listAll(Model model){
        var users = userService.listAllUsers();
        model.addAttribute("listUsers" , users);
        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
            User user = userService.get(id);
            List<Role> roleList = userService.listAllRoles();
            model.addAttribute("user", user);
            model.addAttribute("roleList", roleList);
            model.addAttribute("pageTitle", "Edit user (ID: " + user.getId() +")");

            return "user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
           userService.delete(id);
           redirectAttributes.addFlashAttribute("message", "The user ID " + id
                   + " was been successfully deleted");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model){
        User user = new User();
        user.setEnabled(true);
        List<Role> roleList = userService.listAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        model.addAttribute("pageTitle", "Create New User");
        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes){
        System.out.println(user);
        userService.save(user);
        redirectAttributes.addFlashAttribute("message", "The user was save successfully.");
        return "redirect:/users";
    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";

    }
}
