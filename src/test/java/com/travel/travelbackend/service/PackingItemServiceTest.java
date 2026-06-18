package com.travel.travelbackend.service;

import com.travel.travelbackend.dto.PackingItemResponse;
import com.travel.travelbackend.entity.PackingItem;
import com.travel.travelbackend.entity.Trip;
import com.travel.travelbackend.entity.User;
import com.travel.travelbackend.repository.PackingItemRepository;
import com.travel.travelbackend.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackingItemServiceTest {

    @Mock
    private PackingItemRepository packingItemRepository;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private PackingItemService packingItemService;

    @Test
    void shouldUpdatePackedSuccessfully() {
        PackingItem item = createItem();

        when(authenticatedUserProvider.getCurrentUser()).thenReturn(createUser());
        when(packingItemRepository.findByIdAndTripUserId(10L, 2L))
                .thenReturn(Optional.of(item));

        when(packingItemRepository.save(item))
                .thenReturn(item);

        PackingItemResponse result = packingItemService.updatePacked(10L, true);

        assertEquals(10L, result.getId());
        assertEquals("Passport", result.getName());
        assertTrue(result.isPacked());
        assertEquals(1L, result.getTripId());

        verify(packingItemRepository).findByIdAndTripUserId(10L, 2L);
        verify(packingItemRepository).save(item);
    }

    private PackingItem createItem() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setUser(createUser());

        PackingItem item = new PackingItem();
        item.setId(10L);
        item.setName("Passport");
        item.setPacked(false);
        item.setTrip(trip);

        return item;
    }

    private User createUser() {
        User user = new User();
        user.setId(2L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        return user;
    }
}
