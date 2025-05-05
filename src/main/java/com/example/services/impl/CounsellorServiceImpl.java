package com.example.services.impl;

import java.util.Optional;

import com.example.dto.CounsellorDto;
import com.example.entities.Counsellor;
import com.example.repo.CounsellorRepo;
import com.example.services.CounsellorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CounsellorServiceImpl implements CounsellorService {
	
	@Autowired
	private CounsellorRepo counsellorRepo;

	@Override
	public CounsellorDto login(String email, String pwd) {
		// TODO Auto-generated method stub
		
		Counsellor entity = counsellorRepo.findByEmailAndPwd(email, pwd);
		if(entity!=null) {
			CounsellorDto dto = new CounsellorDto();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}
		return null;
	}

	@Override
	public boolean isEmailUnique(String email) {
		// TODO Auto-generated method stub
		Optional<Counsellor> byEmail = counsellorRepo.findByEmail(email);
		if(byEmail.isPresent()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean register(CounsellorDto counsellorDto) {
		// TODO Auto-generated method stub
		
		Counsellor entity = new Counsellor();
		BeanUtils.copyProperties(counsellorDto, entity);
		Counsellor saveEntity = counsellorRepo.save(entity);
		
		return saveEntity.getCounsellorId() != null;
	}

}
