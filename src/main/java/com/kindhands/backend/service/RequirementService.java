package com.kindhands.backend.service; // Missing package statement fix

import com.kindhands.backend.entity.Requirement;
import com.kindhands.backend.entity.RequirementStatus;
import com.kindhands.backend.entity.User;
import com.kindhands.backend.entity.Organization;
import com.kindhands.backend.repository.RequirementRepository;
import com.kindhands.backend.repository.UserRepository;
import com.kindhands.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementService {
    @Autowired private RequirementRepository requirementRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private OrganizationRepository orgRepository;
    @Autowired private EmailService emailService;

    public Requirement saveRequirement(Requirement req, Long orgId) {
        Organization org = orgRepository.findById(orgId).orElseThrow();
        req.setOrganization(org);
        req.setStatus(RequirementStatus.OPEN);
        return requirementRepository.save(req);
    }

    public List<Requirement> getActiveRequirements() {
        return requirementRepository.findByStatus(RequirementStatus.OPEN);
    }

    public void acceptAndNotify(Long reqId, Long donorId) {
        Requirement req = requirementRepository.findById(reqId).orElseThrow();
        User donor = userRepository.findById(donorId).orElseThrow();

        req.setStatus(RequirementStatus.ACCEPTED);
        req.setDonor(donor);
        requirementRepository.save(req);

        // Organization la email pathvane
        String orgEmail = req.getOrganization().getEmail();
        String subject = "Kind Hands: A Donor is ready to help!";
        String message = "Hello " + req.getOrganization().getName() + ",\n\n" +
                "A donor has accepted your requirement: " + req.getDetails() + "\n\n" +
                "Donor Details:\n" +
                "Name: " + donor.getName() + "\n" +
                "Mobile: " + donor.getMobile() + "\n" +
                "Email: " + donor.getEmail() + "\n\n" +
                "Please coordinate with them. Thank you!";

        emailService.sendSimpleEmail(orgEmail, subject, message);
    }
}