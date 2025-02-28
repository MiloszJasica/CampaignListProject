package com.example.project.controller;

import com.example.project.model.Campaign;
import com.example.project.model.Product;
import com.example.project.repository.CampaignRepository;
import com.example.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    @Autowired
    private CampaignRepository CampaignRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Campaign> getAllCampaigns() {
        return CampaignRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable Long id) {
        Optional<Campaign> campaign = CampaignRepository.findById(id);
        return campaign.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Campaign createCampaign(@RequestBody Campaign campaign) {
        return CampaignRepository.save(campaign);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id, @RequestBody Campaign updatedCampaign) {
        return CampaignRepository.findById(id)
                .map(campaign -> {
                    campaign.setName(updatedCampaign.getName());
                    campaign.setKeywords(updatedCampaign.getKeywords());
                    campaign.setBidAmount(updatedCampaign.getBidAmount());
                    campaign.setCampaignFund(updatedCampaign.getCampaignFund());
                    campaign.setStatus(updatedCampaign.isStatus());
                    campaign.setTown(updatedCampaign.getTown());
                    campaign.setRadius(updatedCampaign.getRadius());
                    campaign.setProducts(updatedCampaign.getProducts());
                    return ResponseEntity.ok(campaignRepository.save(campaign));
                })
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCampaign(@PathVariable Long id) {
        return CampaignRepository.findById(id).map(campaign -> {
            CampaignRepository.delete(campaign);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{campaignId}/products")
    public ResponseEntity<Campaign> addProductToCampaign(@PathVariable Long campaignId, @RequestBody Product product) {
        return campaignRepository.findById(campaignId).map(campaign -> {
            productRepository.save(product);
            campaign.getProducts().add(product);
            campaignRepository.save(campaign);
            return ResponseEntity.ok(campaign);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
