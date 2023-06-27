package nl.inholland.gnasBank.controllers;

import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.services.UserService;
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

class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "100")
                        .param("hasAccount", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAll(0, 100, true);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testGetUserByEmail() throws Exception {
        String email = "john.doe@example.com";
        mockMvc.perform(get("/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getByEmail(email);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testGetByUsername() throws Exception {
        String firstName = "John";
        String lastName = "Doe";
        mockMvc.perform(get("/users/search")
                        .param("firstName", firstName)
                        .param("lastName", lastName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getByFirstAndLastName(firstName, lastName);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testCreateUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"apitest\",\"password\":\"test\",\"firstName\":\"gnas\",\"lastName\":\"theone\",\"phone\":\"0000000\",\"dayLimit\":\"100\",\"transactionLimit\":\"100\",\"roles\":[\"EMPLOYEE\", \"CUSTOMER\"]}"))
                .andExpect(status().isOk());

        // Unfortunately, we cannot verify the exact `User` object passed to `userService.create()` here
        verify(userService, times(1)).create(any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"apitest\",\"password\":\"test\",\"firstName\":\"gnasUpdated\",\"lastName\":\"theone\",\"phone\":\"0000000\",\"dayLimit\":\"100\",\"transactionLimit\":\"100\",\"roles\":[\"EMPLOYEE\", \"CUSTOMER\"]}"))
                .andExpect(status().isOk());

        // Unfortunately, we cannot verify the exact `User` object passed to `userService.update()` here
        verify(userService, times(1)).update(eq(userId), any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();
        mockMvc.perform(delete("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
        verifyNoMoreInteractions(userService);
    }
}
