import './App.css';
import React, { useEffect, useState } from "react";
import { fetchCampaigns, createCampaign, updateCampaign, deleteCampaign, addProductToCampaign } from "./api";

function App() {
    const [campaigns, setCampaigns] = useState([]);
    const [balance, setBalance] = useState(1000);
    const [previousFund, setPreviousFund] = useState(0);
    const [newCampaign, setNewCampaign] = useState({
        id: null,
        name: "",
        keywords: "",
        bidAmount: 0,
        campaignFund: 0,
        status: false,
        town: "",
        radius: 0,
        products: []
    });
    const [newProduct, setNewProduct] = useState({
        name: "",
        quantity: 0,
        price: 0
    });
    const [error, setError] = useState(null);
    const [selectedCampaignId, setSelectedCampaignId] = useState(null);

    useEffect(() => {
        const storedBalance = localStorage.getItem("balance");
        if (storedBalance) {
            setBalance(parseFloat(storedBalance));
        }
        loadCampaigns();
    }, []);

    async function loadCampaigns() {
        try {
            const data = await fetchCampaigns();
            setCampaigns(data);
        } catch (err) {
            console.error("Error loading campaigns:", err);
            setError("The campaigns failed to load.");
        }
    }

    async function handleDelete(id) {
        try {
            const campaignToDelete = campaigns.find(campaign => campaign.id === id);
            if (campaignToDelete) {
                setBalance(prevBalance => prevBalance + campaignToDelete.campaignFund);
            }

            await deleteCampaign(id);
            loadCampaigns();
        } catch (err) {
            console.error("Error deleting campaign:", err);
        }
    }

    function validateCampaign(campaign) {
        if (!campaign.name || !campaign.keywords || campaign.bidAmount <= 0 || campaign.campaignFund <= 0 || !campaign.town || campaign.radius <= 0) {
            setError("Complete all fields.");
            return false;
        }
        if (campaign.campaignFund > balance) {
            setError("Insufficient funds.");
            return false;
        }
        return true;
    }

    async function handleCreate() {
        if (!validateCampaign(newCampaign)) return;

        try {
            const updatedCampaign = {
                ...newCampaign,
                keywords: newCampaign.keywords.split(",").map(keyword => keyword.trim())
            };

            await createCampaign(updatedCampaign);
            setBalance(prevBalance => prevBalance - updatedCampaign.campaignFund);
            resetForm();
            loadCampaigns();
        } catch (err) {
            console.error("Error while creating the campaign:", err);
            setError("Failed to add campaign.");
        }
    }

    async function handleUpdate() {
        if (!validateCampaign(newCampaign)) return;

        try {
            setBalance(prevBalance => prevBalance + previousFund - newCampaign.campaignFund);

            await updateCampaign(newCampaign);
            resetForm();
            loadCampaigns();
        } catch (err) {
            console.error("Error during update:", err);
            setError("The campaign failed to update.");
        }
    }

    function handleEdit(campaign) {
        setNewCampaign({
            id: campaign.id,
            name: campaign.name,
            keywords: campaign.keywords,
            bidAmount: campaign.bidAmount,
            campaignFund: campaign.campaignFund,
            status: campaign.status,
            town: campaign.town,
            radius: campaign.radius,
            products: campaign.products || []
        });
        setPreviousFund(campaign.campaignFund);
    }

    async function handleAddProduct() {
        if (!newProduct.name || newProduct.quantity <= 0 || newProduct.price <= 0) {
            setError("Complete product fields.");
            return;
        }

        try {
            const product = { ...newProduct };
            await addProductToCampaign(selectedCampaignId, product);
            setNewProduct({ name: "", quantity: 0, price: 0 });
            loadCampaigns();
        } catch (err) {
            console.error("Error adding product:", err);
            setError("Failed to add product.");
        }
    }

    function resetForm() {
        setNewCampaign({
            id: null,
            name: "",
            keywords: "",
            bidAmount: 0,
            campaignFund: 0,
            status: false,
            town: "",
            radius: 0,
            products: []
        });
        setPreviousFund(0);
        setError(null);
    }

    return (
        <div>
            <h1>Campaigns List</h1>
            <h3>Your Balance: {balance} zł</h3>

            {error && <div className="error-message">{error}</div>}

            {error && <p style={{ color: "red" }}>{error}</p>}

            <table border="1" class="campaignTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Campaign Name</th>
                    <th>Keywords</th>
                    <th>Bid amount</th>
                    <th>Fund</th>
                    <th>Status</th>
                    <th>Town</th>
                    <th>Radius</th>
                    <th>Products</th>
                    <th>Campaigns Management</th>
                </tr>
                </thead>
                <tbody>
                {campaigns.map((campaign) => (
                    <tr key={campaign.id}>
                        <td>{campaign.id}</td>
                        <td>{campaign.name}</td>
                        <td>{campaign.keywords.join(", ")}</td>
                        <td>{campaign.bidAmount} zł</td>
                        <td>{campaign.campaignFund} zł</td>
                        <td>{campaign.status ? "ON" : "OFF"}</td>
                        <td>{campaign.town}</td>
                        <td>{campaign.radius} km</td>
                        <td>
                            {campaign.products.length > 0 ? (
                                <table border="1">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {campaign.products.map((product, index) => (
                                        <tr key={index}>
                                            <td>{index + 1}</td>
                                            <td>{product.name}</td>
                                            <td>{product.price} zł</td>
                                            <td>{product.quantity}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            ) : (
                                <p>No products</p>
                            )}
                        </td>
                        <td>
                            <button class="delete" onClick={() => handleDelete(campaign.id)}>Delete</button>
                            <button class="add" onClick={() => handleEdit(campaign)}>Edit</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className="form-container">
                <div className="form-column">
                    <h2>{newCampaign.id ? "Edit Campaign" : "Add Campaign"}</h2>
                    <p><input type="text" placeholder="Campaign name"
                           value={newCampaign.name} onChange={(e) => setNewCampaign({ ...newCampaign, name: e.target.value })} /></p>
                    <p><input type="text" placeholder="Keywords"
                           value={newCampaign.keywords} onChange={(e) => setNewCampaign({ ...newCampaign, keywords: e.target.value })} /></p>
                    <p>Bid amount: <input type="number" placeholder="Bid amount"
                           value={newCampaign.bidAmount} onChange={(e) => setNewCampaign({ ...newCampaign, bidAmount: parseFloat(e.target.value) || 0 })} /></p>
                    <p>Fund: <input type="number" placeholder="Campaign Fund"
                           value={newCampaign.campaignFund} onChange={(e) => setNewCampaign({ ...newCampaign, campaignFund: parseFloat(e.target.value) || 0 })} /></p>
                    <p><select value={newCampaign.town} onChange={(e) => setNewCampaign({ ...newCampaign, town: e.target.value })}>
                        <option value="">Choose town</option>
                        <option value="Warszawa">Warszawa</option>
                        <option value="Krakow">Krakow</option>
                        <option value="Wroclaw">Krakow</option>
                        <option value="Lodz">Krakow</option>
                        <option value="Torun">Krakow</option>
                    </select></p>
                    <p>Radius: <input type="number" placeholder="Radius" value={newCampaign.radius} onChange={(e) => setNewCampaign({ ...newCampaign, radius: parseFloat(e.target.value) || 0 })} /></p>
                        <p><button onClick={newCampaign.id ? handleUpdate : handleCreate}>{newCampaign.id ? "Save Campaign" : "Add Campaign"}</button></p>
                </div>

                <div className="form-column">
                    <h2>Add Product</h2>
                    <select value={selectedCampaignId} onChange={(e) => setSelectedCampaignId(e.target.value)}>
                        <option value="">Select Campaign</option>
                        {campaigns.map((campaign) => (
                            <option key={campaign.id} value={campaign.id}>{campaign.name}</option>
                        ))}
                    </select>
                    <p><input type="text" placeholder="Product Name" value={newProduct.name} onChange={(e) => setNewProduct({ ...newProduct, name: e.target.value })} /></p>
                    <p>Quantity: <input type="number" placeholder="Quantity" value={newProduct.quantity} onChange={(e) => setNewProduct({ ...newProduct, quantity: parseInt(e.target.value) || 0 })} /></p>
                    <p>Price: <input type="number" placeholder="Price (zł)" value={newProduct.price} onChange={(e) => setNewProduct({ ...newProduct, price: parseFloat(e.target.value) || 0 })} /></p>
                    <button onClick={handleAddProduct}>Add Product</button>
                </div>
            </div>
        </div>
    );
}

export default App;
