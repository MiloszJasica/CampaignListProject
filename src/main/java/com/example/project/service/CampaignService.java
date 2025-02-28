package com.example.project.service;

import com.example.project.model.Campaign;
import com.example.project.model.CampaignDto;
import com.example.project.model.Product;
import com.example.project.repository.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<CampaignDto> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(CampaignDto::toDto)
                .collect(Collectors.toList());
    }

    public Optional<CampaignDto> getCampaignById(Long id) {
        return campaignRepository.findById(id).map(CampaignDto::toDto);
    }

    public Long createCampaign(CampaignDto campaign) {
        return campaignRepository.save(CampaignDto.toEntity(campaign)).getId();
    }

    public Optional<CampaignDto> updateCampaign(Long id, CampaignDto updatedCampaign) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    campaign.setName(updatedCampaign.name());
                    campaign.setKeywords(updatedCampaign.keywords());
                    campaign.setBidAmount(updatedCampaign.bidAmount());
                    campaign.setCampaignFund(updatedCampaign.campaignFund());
                    campaign.setStatus(updatedCampaign.status());
                    campaign.setTown(updatedCampaign.town());
                    campaign.setRadius(updatedCampaign.radius());
                    campaign.setProducts(updatedCampaign.products());
                    Campaign updated = campaignRepository.save(campaign);
                    return CampaignDto.toDto(updated);
                });
    }


    public boolean deleteCampaign(@PathVariable Long id) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    campaignRepository.delete(campaign);
                    return true;
                }).orElse(false);
    }

    public Optional<CampaignDto> addProductToCampaign(Long campaignId, Product product) {
        return campaignRepository.findById(campaignId).map(campaign -> {
            campaign.getProducts().add(product);
            campaignRepository.save(campaign);
            return CampaignDto.toDto(campaign);
        });
    }

}