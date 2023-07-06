package com.vishwaas; 

import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<CertificateData, Integer>
{

}
