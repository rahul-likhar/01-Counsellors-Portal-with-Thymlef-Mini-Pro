package com.example.services;

import com.example.dto.DashboardDto;
import com.example.dto.EnquiryDto;

import java.util.List;


public interface EnquiryService {

	public DashboardDto getDashboardInfo(Integer counsellorId);

	public boolean upsertEnquiry(EnquiryDto enqDto, Integer counsellorId);

	public List<EnquiryDto> getEnquiries(Integer counsellorId);

	public List<EnquiryDto> filterEnqs(EnquiryDto filterDto, Integer counsellorId);

	public EnquiryDto getEnquiry(Integer enqId);
}
