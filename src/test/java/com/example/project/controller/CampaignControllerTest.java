package com.example.project.controller;

import com.example.project.model.CampaignDto;
import com.example.project.service.CampaignService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class CampaignControllerTest {

    @Mock
    private CampaignService campaignService;

    @InjectMocks
    private CampaignController campaignController;

    private MockMvc mockMvc;

    @Test
    void testGetAllCampaigns() throws Exception {
        List<CampaignDto> campaigns = Arrays.asList(
                new CampaignDto(1L, "Black Friday", List.of("electronics"), 50.0, 1000.0, true, "NY", 20, null),
                new CampaignDto(2L, "Winter Sale", List.of("clothes"), 30.0, 500.0, false, "LA", 10, null)
        );

        when(campaignService.getAllCampaigns()).thenReturn(campaigns);

        mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();

        mockMvc.perform(get("/api/campaigns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Black Friday"))
                .andExpect(jsonPath("$[1].name").value("Winter Sale"));
    }

    @Test
    void testGetCampaignById_Found() throws Exception {
        CampaignDto campaign = new CampaignDto(1L, "Black Friday", List.of("electronics"), 50.0, 1000.0, true, "NY", 20, null);
        when(campaignService.getCampaignById(1L)).thenReturn(Optional.of(campaign));

        mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();

        mockMvc.perform(get("/api/campaigns/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Black Friday"))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testGetCampaignById_NotFound() throws Exception {
        when(campaignService.getCampaignById(1L)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();

        mockMvc.perform(get("/api/campaigns/1"))
                .andExpect(status().isNotFound());

        verify(campaignService, times(1)).getCampaignById(1L);
    }

    @Test
    void testDeleteCampaign_Found() throws Exception {
        when(campaignService.deleteCampaign(1L)).thenReturn(true);

        mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();

        mockMvc.perform(delete("/api/campaigns/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCampaign_NotFound() throws Exception {
        when(campaignService.deleteCampaign(1L)).thenReturn(false);

        mockMvc = MockMvcBuilders.standaloneSetup(campaignController).build();

        mockMvc.perform(delete("/api/campaigns/1"))
                .andExpect(status().isNotFound());
    }
}
