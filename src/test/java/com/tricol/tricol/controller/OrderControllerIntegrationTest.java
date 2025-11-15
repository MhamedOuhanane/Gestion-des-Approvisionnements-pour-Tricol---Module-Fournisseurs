package com.tricol.tricol.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String supplierId = "4e4cd7cf-b3ae-43cb-a003-ae4a04e9d16b";

    @Test
    public void shouldCreateOrderSuccessfully() throws Exception {

        String orderJson = """
                {
                  "orderDate": "2025-11-04T12:30:00",
                  "status": "PENDING",
                  "supplierId": "%s",
                  "productsOrders": [
                    {
                      "productName": "Fil",
                      "quantity": 10
                    }
                  ]
                }
                """.formatted(supplierId);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Commande créée avec succès"))
                .andExpect(jsonPath("$.data.status").value("PENDING"));
    }

    @Test
    public void shouldReturnBadRequestWhenOrderIsNull() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFailWhenSupplierNotFound() throws Exception {

        String orderJson = """
                {
                  "orderDate": "2025-11-04T12:30:00",
                  "status": "PENDING",
                  "supplierId": "00000000-0000-0000-0000-000000000000",
                  "productsOrders": [
                    {
                      "productName": "Fil",
                      "quantity": 10
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Aucun fournisseur trouvé avec cet identifiant"));
    }

    @Test
    public void shouldFailWhenQuantityExceedsStock() throws Exception {

        String orderJson = """
                {
                  "orderDate": "2025-11-04T12:30:00",
                  "status": "PENDING",
                  "supplierId": "%s",
                  "productsOrders": [
                    {
                      "productName": "Fil",
                      "quantity": 999999
                    }
                  ]
                }
                """.formatted(supplierId);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Quantité demandée du produit 'Fil' supérieure au stock disponible"));
    }

    @Test
    public void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/v1/orders/" +
                        UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Aucun commande trouvé avec cet identifiant"));
    }

    @Test
    public void shouldUpdateStatusSuccessfully() throws Exception {

        String createJson = """
                {
                  "orderDate": "2025-10-10T10:10:10",
                  "status": "PENDING",
                  "supplierId": "%s",
                  "productsOrders": [
                    {
                      "productName": "Fil",
                      "quantity": 5
                    }
                  ]
                }
                """.formatted(supplierId);

        String response = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andReturn().getResponse().getContentAsString();

        String uuid = response.split("\"uuid\":\"")[1].split("\"")[0];

        String patchJson = """
                {
                  "status": "VALIDATED"
                }
                """;

        mockMvc.perform(patch("/api/v1/orders/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Le commande est Validé avec succès!"));
    }

    @Test
    public void shouldFailDeletingOrderNotFound() throws Exception {

        mockMvc.perform(delete("/api/v1/orders/" +
                        UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Aucun commande trouvé avec cet identifiant"));
    }
}
