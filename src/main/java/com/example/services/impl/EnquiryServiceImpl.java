package com.example.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.dto.DashboardDto;
import com.example.dto.EnquiryDto;
import com.example.entities.Counsellor;
import com.example.entities.Enquiry;
import com.example.repo.CounsellorRepo;
import com.example.repo.EnquiryRepo;
import com.example.services.EnquiryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;



@Service
public class EnquiryServiceImpl implements EnquiryService {
	
	@Autowired
	private EnquiryRepo enquiryRepo;

	@Autowired
	private CounsellorRepo counsellorRepo;
	
	@Override
	public DashboardDto getDashboardInfo(Integer counsellorId) {
		// TODO Auto-generated method stub
		
		//QBE
		Counsellor counsellor = new Counsellor();
		counsellor.setCounsellorId(counsellorId);
		
		Enquiry enquiry = new Enquiry();
		enquiry.setCounsellor(counsellor);
		
		//base on counsellorId find all data
		List<Enquiry> enqlist = enquiryRepo.findAll(Example.of(enquiry));
		int total = enqlist.size();
		
		int open = enqlist.stream().filter(e -> e.getEnqStatus().equals("Open")).collect(Collectors.toList()).size();
		int enrolled = enqlist.stream().filter(e -> e.getEnqStatus().equals("Enrolled")).collect(Collectors.toList()).size();
		int lost = enqlist.stream().filter(e -> e.getEnqStatus().equals("Lost")).collect(Collectors.toList()).size();
		
		DashboardDto dto = new DashboardDto();
		dto.setOpenEnqs(open);
		dto.setEnrolledEnqs(enrolled);
		dto.setLostEnqs(lost);
		dto.setTotalEnqs(total);
		
		//Above or below single line for this we have to write method in enquiryRepo i.e.  findByCounsellorCounsellorId(Integer counsellorId)
		//enquiryRepo.findByCounsellorCounsellorId(counsellorId);
		
		return dto;
	}

	@Override
	public boolean upsertEnquiry(EnquiryDto enqDto, Integer counsellorId) {
		// TODO Auto-generated method stub
		
		Enquiry entity = new Enquiry();
		
		BeanUtils.copyProperties(enqDto, entity);
		
		Counsellor counsellor = counsellorRepo.findById(counsellorId).orElseThrow();
		
		entity.setCounsellor(counsellor);
		
		Enquiry savedEntity = enquiryRepo.save(entity);
		
		return savedEntity.getEnqId() != null;
	}

	@Override
	public List<EnquiryDto> getEnquiries(Integer counsellorId) {
		// TODO Auto-generated method stub
		
		return enquiryRepo.findByCounsellorCounsellorId(counsellorId).stream().map(
				e ->{
					EnquiryDto dto = new EnquiryDto();
					BeanUtils.copyProperties(e, dto);
					return dto;
				}).collect(Collectors.toList());
		
		
//		List<EnquiryDto> dtoList = new ArrayList<>();
//		
//		Counsellor cEntity = new Counsellor();
//		cEntity.setCounsellorId(counsellorId);
//		
//		Enquiry eEntity= new Enquiry();
//		eEntity.setCounsellor(cEntity);
//		
//		List<Enquiry> list = enquiryRepo.findAll(Example.of(eEntity));
//		
//		list.forEach(e -> {
//			EnquiryDto dto = new EnquiryDto();
//			BeanUtils.copyProperties(e, dto);
//			dtoList.add(dto);
//		});
//		return dtoList;
		

		
	}

	@Override
	public List<EnquiryDto> filterEnqs(EnquiryDto filterDto, Integer counsellorId) {
		// TODO Auto-generated method stub
		
		//QBE
		Enquiry entity = new Enquiry();
		
		if(filterDto.getCourseName() != null && !filterDto.getCourseName().equals("")) {
			entity.setCourseName(filterDto.getCourseName());
		}
		
		if(filterDto.getClassMode() != null && !filterDto.getClassMode().equals("")) {
			entity.setClassMode(filterDto.getClassMode());
		}
		
		if(filterDto.getEnqStatus() != null && !filterDto.getEnqStatus().equals("")) {
			entity.setEnqStatus(filterDto.getEnqStatus());
		}
		
		Counsellor counsellor = counsellorRepo.findById(counsellorId).orElseThrow();
		entity.setCounsellor(counsellor);
		
		List<Enquiry> list = enquiryRepo.findAll(Example.of(entity));
		
		List<EnquiryDto> dtoList = new ArrayList<>();
		list.forEach(e -> {
			EnquiryDto dto = new EnquiryDto();
			BeanUtils.copyProperties(e, dto);
			dtoList.add(dto);			
		});
		
		return dtoList;
	}

	@Override
	public EnquiryDto getEnquiry(Integer enqId) {
		// TODO Auto-generated method stub
		
		Enquiry enquiry = enquiryRepo.findById(enqId).orElseThrow();
		
		EnquiryDto dto = new EnquiryDto();
		BeanUtils.copyProperties(enquiry, dto);
		
		return dto;
	}

}
