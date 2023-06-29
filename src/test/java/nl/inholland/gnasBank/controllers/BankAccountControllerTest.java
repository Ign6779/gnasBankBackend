package nl.inholland.gnasBank.controllers;

import nl.inholland.gnasBank.models.dto.BankAccountDTO;
import nl.inholland.gnasBank.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

class BankAccountControllerTest {
    private MockMvc mockMvc;

    @Mock
    private BankAccountService bankAccountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BankAccountController(bankAccountService)).build();
    }

    @Test
    void testGetAllBankAccounts() throws Exception {
        mockMvc.perform(get("/bankaccounts")
                        .param("page", "0")
                        .param("size", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).getAll(0, 100);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testGetBankAccountById() throws Exception {
        String iban = "NL02RABO7134384551";
        mockMvc.perform(get("/bankaccounts/{iban}", iban)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).getById(iban);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testGetBankAccountByUsername() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        mockMvc.perform(get("/bankaccounts/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).getByUsername(firstName, lastName);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testCreateBankAccount() throws Exception {
        mockMvc.perform(post("/bankaccounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + UUID.randomUUID().toString() + "\",\"absoluteLimit\":500,\"balance\":200,\"type\":\"CURRENT\"}"))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).create(any(BankAccountDTO.class));
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testUpdateBankAccount() throws Exception {
        String iban = "NL02RABO7134384551";
        mockMvc.perform(put("/bankaccounts/{id}", iban)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":\"" + UUID.randomUUID().toString() + "\",\"absoluteLimit\":700,\"balance\":300,\"type\":\"SAVINGS\"}"))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).update(any(BankAccountDTO.class));
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testDeleteBankAccount() throws Exception {
        String iban = "NL02RABO7134384551";
        mockMvc.perform(delete("/bankaccounts/{id}", iban)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bankAccountService, times(1)).delete(iban);
        verifyNoMoreInteractions(bankAccountService);
    }
}
