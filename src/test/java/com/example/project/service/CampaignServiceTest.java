package com.example.project.service;

import com.example.project.model.Campaign;
import com.example.project.model.CampaignDto;
import com.example.project.model.Product;
import com.example.project.repository.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignService campaignService;

    private Campaign campaign1;
    private Campaign campaign2;
    private Product product1;

    @BeforeEach
    void setUp() {
        campaign1 = new Campaign();
        campaign1.setId(1L);
        campaign1.setName("Campaign 1");
        campaign1.setCampaignFund(100);
        campaign1.setBidAmount(50);
        campaign1.setKeywords(Arrays.asList("keyword1", "keyword2"));
        campaign1.setStatus(true);

        campaign1.setProducts(new ArrayList<>());

        campaign2 = new Campaign();
        campaign2.setId(2L);
        campaign2.setName("Campaign 2");
        campaign2.setCampaignFund(300);
        campaign2.setBidAmount(150);
        campaign2.setKeywords(Arrays.asList("keyword3", "keyword4"));
        campaign2.setStatus(false);

        campaign2.setProducts(new ArrayList<>());

        product1 = new Product();
        product1.setName("Product 1");
        product1.setId(1L);
        product1.setQuantity(5);
        product1.setPrice(10.0);
    }

    @Test
    void testGetAllCampaigns() {
        when(campaignRepository.findAll()).thenReturn(Arrays.asList(campaign1, campaign2));

        List<CampaignDto> result = campaignService.getAllCampaigns();

        assertEquals(2, result.size());
        assertEquals("Campaign 1", result.get(0).name());
        assertEquals("Campaign 2", result.get(1).name());
    }

    @Test
    void testGetCampaignById_Found() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign1));

        Optional<CampaignDto> result = campaignService.getCampaignById(1L);

        assertTrue(result.isPresent());
        assertEquals("Campaign 1", result.get().name());
        assertEquals(50.0, result.get().bidAmount());
    }

    @Test
    void testGetCampaignById_NotFound() {
        when(campaignRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<CampaignDto> result = campaignService.getCampaignById(3L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateCampaign() {
        CampaignDto campaignDto = new CampaignDto(1L, "Campaign 1", Arrays.asList("tech", "gaming"), 50.0, 1000.0, true, "Warszawa", 10, List.of());
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign1);

        Long campaignId = campaignService.createCampaign(campaignDto);

        assertEquals(1L, campaignId);
    }

    @Test
    void testUpdateCampaign_Success() {
        CampaignDto updatedDto = new CampaignDto(1L, "Updated Kampania", Arrays.asList("business", "finance"), 60.0, 1500.0, true, "Gdańsk", 20, List.of());
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign1));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign1);

        Optional<CampaignDto> result = campaignService.updateCampaign(1L, updatedDto);

        assertTrue(result.isPresent());
        assertEquals("Updated Kampania", result.get().name());
        assertEquals(60.0, result.get().bidAmount());
    }

    @Test
    void testUpdateCampaign_NotFound() {
        CampaignDto updatedDto = new CampaignDto(1L, "Updated Kampania", Arrays.asList("business", "finance"), 60.0, 1500.0, true, "Gdańsk", 20, List.of());
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CampaignDto> result = campaignService.updateCampaign(1L, updatedDto);

        assertFalse(result.isPresent());

    }

    @Test
    void testDeleteCampaign_Success() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign1));

        boolean result = campaignService.deleteCampaign(1L);

        assertTrue(result);
    }

    @Test
    void testDeleteCampaign_NotFound() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = campaignService.deleteCampaign(1L);

        assertFalse(result);
    }

    @Test
    void testAddProductToCampaign_Success() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign1));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign1);

        Optional<CampaignDto> result = campaignService.addProductToCampaign(1L, product1);

        assertTrue(result.isPresent());
        assertEquals(1, campaign1.getProducts().size());
        assertEquals("Product 1", campaign1.getProducts().get(0).getName());
    }

    @Test
    void testAddProductToCampaign_NotFound() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CampaignDto> result = campaignService.addProductToCampaign(1L, product1);

        assertFalse(result.isPresent());
    }
}
