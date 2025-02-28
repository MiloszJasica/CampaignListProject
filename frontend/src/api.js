const API_URL = "http://localhost:8080/api/campaigns";

export async function fetchCampaigns() {
    const response = await fetch(API_URL);
    return response.json();
}

export async function createCampaign(campaign) {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(campaign),
    });
    return response.json();
}

export async function deleteCampaign(id) {
    await fetch(`${API_URL}/${id}`, { method: "DELETE" });
}

export async function updateCampaign(campaign) {
    const response = await fetch(`${API_URL}/${campaign.id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(campaign),
    });
    return response.json();
}

export async function addProductToCampaign(campaignId, product) {
    const response = await fetch(`${API_URL}/${campaignId}/products`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(product),
    });
    return response.json();
}