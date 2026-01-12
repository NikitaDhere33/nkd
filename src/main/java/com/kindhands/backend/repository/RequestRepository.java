package com.kindhands.backend.repository;

import com.kindhands.backend.entity.Request;
import com.kindhands.backend.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByOrganizationId(Long organizationId);

    List<Request> findByDonorId(Long donorId);


    List<Request> findByStatus(RequestStatus requestStatus);
}
