package nl.inholland.gnasBank.controllers;

import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.Transaction;
import nl.inholland.gnasBank.models.dto.TransactionDTO;
import nl.inholland.gnasBank.services.BankAccountService;
import nl.inholland.gnasBank.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TransactionControllerTest {
    private MockMvc mockMvc;
    @Mock
    private TransactionService transactionService;
    @Mock
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService, bankAccountService)).build();
    }

    @Test
    void testGetAll() throws Exception{
        Integer page = 0;
        Integer size = 100;

        List<Transaction> transactions = List.of(new Transaction(), new Transaction());

        when(transactionService.getAll(page,size))
                .thenReturn(transactions);

        mockMvc.perform(get("/transactions")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(transactionService).getAll(eq(0), eq(100));
    }
    
    @Test
    void testGetById() throws Exception{
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction();

        when(transactionService.getById(transactionId)).thenReturn(transaction);

        mockMvc.perform(get("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).getById(transactionId);
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testCreate() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        Transaction transaction = new Transaction();

        when(transactionService.create(transactionDTO)).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionService).create(any(TransactionDTO.class));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void testDelete() throws Exception{
        UUID transactionId = UUID.randomUUID();

        mockMvc.perform(delete("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).delete(transactionId);
        verifyNoMoreInteractions(transactionService);
    }
}
