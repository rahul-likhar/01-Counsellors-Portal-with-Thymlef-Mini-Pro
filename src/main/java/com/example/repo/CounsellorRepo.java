package com.example.repo;


import com.example.entities.Counsellor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounsellorRepo extends JpaRepository<Counsellor, Integer> {

	public Counsellor findByEmailAndPwd(String email, String pwd);
	
	public Optional<Counsellor> findByEmail(String email);
	
}
