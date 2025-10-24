package com.tricol.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tricol.model.dto.SupplierDTO;
import com.tricol.service.interfaces.SupplierService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

public class SupplierController implements Controller {
    private final SupplierService supplierService;
    private final ObjectMapper mapper = new ObjectMapper();

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String methode = request.getMethod();
        String uri = request.getRequestURI();
        String path = "";
        UUID id = null;

        String prefix = "/suppliers/";
        if (uri.length() > prefix.length()) {
            path = uri.substring("/suppliers/".length());
            id = UUID.fromString(path);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        switch (methode) {
            case "GET" -> {
                if (path.isEmpty()) get(out);
                else show(out, id);
            }
            case "POST" -> create(request, out);
            case "PUT" -> update(request, out, id);
            case "DELETE" -> destroy(out, id);
            default -> {
                out.write("{\"warning\": \"La ressource demandée n'est pas disponible.\"}");
            }
        }

        out.flush();
        return null;
    }

    private void get(PrintWriter out) {
        try {
            Map<String, Object> dtoList = supplierService.getAllSupplier();
            out.write(mapper.writeValueAsString(dtoList));
        } catch (Exception e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void show(PrintWriter out, UUID uuid) {
        try {
            Map<String, Object> result = supplierService.findSupplier(uuid);
            out.write(mapper.writeValueAsString(result));
        } catch (Exception e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void create(HttpServletRequest request, PrintWriter out) {
        try {
            SupplierDTO supplierDTO = mapper.readValue(request.getInputStream(), SupplierDTO.class);
            Map<String, Object> result = supplierService.addSupplier(supplierDTO);
            out.write(mapper.writeValueAsString(result));
        } catch (Exception e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void update(HttpServletRequest request, PrintWriter out, UUID uuid) {
        try {
            SupplierDTO supplierDTO = mapper.readValue(request.getInputStream(), SupplierDTO.class);
            Map<String, Object> result = supplierService.updateSupplier(uuid, supplierDTO);
            out.write(mapper.writeValueAsString(result));
        } catch (Exception e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void destroy(PrintWriter out, UUID uuid) {
        try {
            Map<String, Object> result = supplierService.deleteSupplier(uuid);
            out.write(mapper.writeValueAsString(result));
        } catch (Exception e) {
            out.write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
