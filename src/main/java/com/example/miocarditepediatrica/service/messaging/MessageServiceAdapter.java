package com.example.miocarditepediatrica.service.messaging;

import com.example.miocarditepediatrica.domain.user.Doctor;
import com.example.miocarditepediatrica.domain.user.Patient;
import com.example.miocarditepediatrica.service.DoctorServicePort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
class MessageServiceAdapter implements MessageServicePort {

    @Autowired
    DoctorServicePort doctorServicePort;

    @Override
    public void processDoctorMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(message);
            String status = getStatusFromJsonNode(node);
            Doctor doctor = getDoctorFromJsonNode(node);

            switch (status){
                case "create":
                    doctorServicePort.addDoctor(doctor);
                    break;
                case "delete":
                    doctorServicePort.deleteDoctor(doctor.getId());
                    break;
            }

        } catch (JsonProcessingException e) {
            System.out.println("Erro ao parsear");
        }

    }

    @Override
    public void processPatientMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(message);
            String status = getStatusFromJsonNode(node);
            Patient patient = getPatientFromJsonNode(node);
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao parsear");
        }

    }

    private String getStatusFromJsonNode(JsonNode node) {
        return node.get("status").asText();
    }

    private Doctor getDoctorFromJsonNode(JsonNode node) {
        node = node.get("medico");

        Doctor doctor = new Doctor(
                node.get("id").asText(),
                node.get("nome").asText(),
                node.get("crm").asText(),
                node.get("especialidade").asText(),
                node.get("dataDeNascimento").asText()
        );

        return doctor;
    }

    private Patient getPatientFromJsonNode(JsonNode node) {
        node = node.get("paciente");

        Patient patient = new Patient(
                node.get("id").asText(),
                node.get("nome").asText(),
                Collections.singletonList(node.get("nomeResponsavel").asText()),
                node.get("altura").asText(),
                node.get("peso").asText(),
                node.get("dataDeNascimento").asText()
        );

        System.out.println(patient.toString());

        return patient;
    }
}
