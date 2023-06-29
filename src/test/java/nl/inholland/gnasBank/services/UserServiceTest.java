package nl.inholland.gnasBank.services;

import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(users));

        List<User> result = userService.getAll(0, 10, true);

        assertEquals(users, result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setUuid(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertEquals(user, result);
    }

    @Test
    void testGetByFirstAndLastName() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findUserByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(user));

        User result = userService.getByFirstAndLastName("John", "Doe");

        assertEquals(user, result);
    }

    @Test
    void testGetByEmail() {
        User user = new User();
        user.setEmail("johndoe@gmail.com");

        when(userRepository.findUserByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        User result = userService.getByEmail("johndoe@gmail.com");

        assertEquals(user, result);
    }

    @Test
    void testCreate() {
        User user = new User();
        user.setEmail("johndoe@gmail.com");

        when(userRepository.findUserByEmail("johndoe@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);

        assertEquals(user, result);
    }

    @Test
    void testUpdate() {
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.update(uuid, user);

        assertEquals(user, result);
    }

    @Test
    void testDelete() {
        UUID userId = UUID.randomUUID();

        doNothing().when(userRepository).deleteById(userId);

        boolean result = userService.delete(userId);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
