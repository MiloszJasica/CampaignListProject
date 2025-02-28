package com.example.project.model;

import java.util.List;

public record CampaignDto(Long id,
                          String name,
                          List<String> keywords,
                          double bidAmount,
                          double campaignFund,
                          boolean status,
                          String town,
                          int radius,
                          List<Product> products)  {

    public static CampaignDto toDto(Campaign campaign) {
        return new CampaignDto(campaign.getId(),
                campaign.getName(),
                campaign.getKeywords(),
                campaign.getBidAmount(),
                campaign.getCampaignFund(),
                campaign.isStatus(),
                campaign.getTown(),
                campaign.getRadius(),
                campaign.getProducts()
        );
    }

    public static Campaign toEntity(CampaignDto campaignDto) {
        Campaign campaign = new Campaign();
        campaign.setName(campaignDto.name());
        campaign.setKeywords(campaignDto.keywords());
        campaign.setBidAmount(campaignDto.bidAmount());
        campaign.setCampaignFund(campaignDto.campaignFund());
        campaign.setStatus(campaignDto.status());
        campaign.setTown(campaignDto.town());
        campaign.setRadius(campaignDto.radius());
        campaign.setProducts(campaignDto.products());
        return campaign;
    }
}
