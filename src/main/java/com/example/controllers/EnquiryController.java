package com.example.controllers;


import com.example.dto.EnquiryDto;
import com.example.services.EnquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EnquiryController {
    
    @Autowired
    private EnquiryService enquiryService;

    @GetMapping("/enquiry")
    public String addEnquiry(Model model){

        EnquiryDto enquiryDto = new EnquiryDto();
        model.addAttribute("enq", enquiryDto);
        return "addEnq";

    }

    @PostMapping("/enquiry")
    public String handleAddEnquiry(Model model, @ModelAttribute("enq") EnquiryDto enq,
                                   HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Integer cid = (Integer) session.getAttribute("COUNSELLOR_ID");
        boolean upsertEnquiry = enquiryService.upsertEnquiry(enq, cid);
        if(upsertEnquiry){
            model.addAttribute("smsg", "Enquiry Added");
        }else {
            model.addAttribute("emsg", "Failed to add enquiry");
        }
        return "addEnq";
    }

    @GetMapping("/viewEnqs")
    public String getEnquiries(Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Integer cid = (Integer) session.getAttribute("COUNSELLOR_ID");
        List<EnquiryDto> enquiryList = enquiryService.getEnquiries(cid);
        model.addAttribute("enquiries", enquiryList);

        EnquiryDto searchFormDto = new EnquiryDto();
        model.addAttribute("filterDto", searchFormDto);

        return "view-enqs";
    }

    @PostMapping("/filter-enqs")
    public String handleFilterEnquiries(Model model, HttpServletRequest request,
                                        @ModelAttribute("filterDto") EnquiryDto filterDto){

        HttpSession session = request.getSession(false);
        Integer cid = (Integer) session.getAttribute("COUNSELLOR_ID");
        List<EnquiryDto> enqsList = enquiryService.filterEnqs(filterDto, cid);
        model.addAttribute("enquiries", enqsList);

        return "view-enqs";
    }

    //http://localhost:8080/edit-enquiry?enqId=101
    @GetMapping("/edit-enquiry")
    public String editEnquiry(Model model, @RequestParam("enqId") Integer enqId){

        EnquiryDto enquiryDto = enquiryService.getEnquiry(enqId);
        model.addAttribute("enq", enquiryDto);
        return "addEnq";
    }

}
