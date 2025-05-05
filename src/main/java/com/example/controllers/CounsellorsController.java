package com.example.controllers;

import com.example.dto.CounsellorDto;
import com.example.dto.DashboardDto;
import com.example.services.CounsellorService;
import com.example.services.EnquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CounsellorsController {

    @Autowired
    private CounsellorService counsellorService;
    
    @Autowired
    private EnquiryService enquiryService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request){

        CounsellorDto counsellorDto = new CounsellorDto();
        model.addAttribute("counsellor", counsellorDto);
        return "index";
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest request, CounsellorDto counsellor) {

        CounsellorDto dto = counsellorService.login(counsellor.getEmail(), counsellor.getPwd());

        if (dto == null) {
            model.addAttribute("emsg", "Invalid Credentials");
            CounsellorDto counsellorDto = new CounsellorDto();
            model.addAttribute("counsellor", counsellorDto);
            return "index";
        } else {
            HttpSession session = request.getSession(true);
            session.setAttribute("COUNSELLOR_ID", dto.getCounsellorId());
            return "redirect:dashboard";
        }
    }

    @GetMapping("/dashboard")
    public String buildDashboard(Model model, HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Integer cid = (Integer) session.getAttribute("COUNSELLOR_ID");
        DashboardDto dashboardInfo = enquiryService.getDashboardInfo(cid);
        model.addAttribute("dashboardInfo", dashboardInfo);

        return "dashboardReportView";
    }

    @GetMapping("/register")
    public String registerPage(Model model){

        CounsellorDto counsellorDto = new CounsellorDto();
        model.addAttribute("counsellor", counsellorDto);
        return "registerView";
    }

    @PostMapping("/register")
    public String handleRegistration(Model model, @ModelAttribute("counsellor") CounsellorDto counsellor){

        boolean unique = counsellorService.isEmailUnique(counsellor.getEmail());
        if (unique){
            boolean registered = counsellorService.register(counsellor);
            if (registered){
                model.addAttribute("smsg", "Register Successfully");
            }else {
                model.addAttribute("emsg", "Registration Failed");
            }
        }else {
            model.addAttribute("emsg", "Email already exist");
        }
        return "registerView";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();

        return "redirect:/";
    }

}
