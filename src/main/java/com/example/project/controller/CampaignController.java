package com.example.project.controller;

import com.example.project.model.CampaignDto;
import com.example.project.model.Product;
import com.example.project.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<CampaignDto> getAllCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignDto> getCampaignById(@PathVariable Long id) {
        return campaignService.getCampaignById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    public Long createCampaign(@RequestBody CampaignDto campaign) {
        return campaignService.createCampaign(campaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampaignDto> updateCampaign(@PathVariable Long id, @RequestBody CampaignDto updatedCampaign) {
        return campaignService.updateCampaign(id,updatedCampaign)
                .map(ResponseEntity::ok)
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long id) {
        return campaignService.deleteCampaign(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{campaignId}/products")
    public ResponseEntity<CampaignDto> addProductToCampaign(@PathVariable Long campaignId, @RequestBody Product product) {
        return campaignService.addProductToCampaign(campaignId, product)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
