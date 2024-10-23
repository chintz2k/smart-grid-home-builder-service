package com.homebuilder.service;

import com.homebuilder.entity.Consumer;
import com.homebuilder.exception.DeviceNotFoundException;
import com.homebuilder.exception.UnauthorizedAccessException;
import com.homebuilder.repository.ConsumerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author André Heinen
 */
public class ConsumerServiceImplTest {

	@Mock
	private ConsumerRepository consumerRepository;

	@InjectMocks
	private ConsumerServiceImpl consumerService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);  // Initialisiert die Mocks vor jedem Test
	}

	@Test
	public void testCreateConsumerForUser() {
		Consumer consumer = new Consumer("TV", 1L, 100.0);
		when(consumerRepository.save(any(Consumer.class))).thenReturn(consumer);

		Consumer createdConsumer = consumerService.createConsumerForUser(consumer, 1L);

		assertNotNull(createdConsumer);
		assertEquals("TV", createdConsumer.getName());
		assertEquals(1L, createdConsumer.getOwnerId());
	}

	@Test
	public void testGetAllConsumersForUser() {
		List<Consumer> consumers = new ArrayList<>();
		consumers.add(new Consumer("TV", 1L, 100.0));
		consumers.add(new Consumer("Fridge", 2L, 100.0));
		when(consumerRepository.findByOwnerId(1L)).thenReturn(consumers);

		List<Consumer> result = consumerService.getAllConsumersForUser(1L);

		assertEquals(2, result.size());
		assertEquals("TV", result.get(0).getName());
		assertEquals("Fridge", result.get(1).getName());
	}

	@Test
	public void testGetConsumerByIdForUserSuccess() {
		Consumer consumer = new Consumer("TV", 1L, 100.0);
		consumer.setOwnerId(1L);
		when(consumerRepository.findById(1L)).thenReturn(Optional.of(consumer));

		Consumer result = consumerService.getConsumerByIdForUser(1L, 1L);

		assertNotNull(result);
		assertEquals("TV", result.getName());
	}

	@Test
	public void testGetConsumerByIdForUserUnauthorized() {
		Consumer consumer = new Consumer("TV", 1L, 100.0);
		consumer.setOwnerId(2L);  // Falscher Besitzer
		when(consumerRepository.findById(1L)).thenReturn(Optional.of(consumer));

		assertThrows(UnauthorizedAccessException.class, () -> {
			consumerService.getConsumerByIdForUser(1L, 1L);  // Falscher Nutzer
		});
	}

	@Test
	public void testUpdateConsumerForUserSuccess() {
		Consumer existingConsumer = new Consumer("TV", 1L, 100.0);
		existingConsumer.setOwnerId(1L);
		Consumer updatedConsumerDetails = new Consumer();
		updatedConsumerDetails.setName("Smart TV");

		when(consumerRepository.findById(1L)).thenReturn(Optional.of(existingConsumer));
		when(consumerRepository.save(any(Consumer.class))).thenReturn(existingConsumer);

		Consumer updatedConsumer = consumerService.updateConsumerForUser(1L, 1L, updatedConsumerDetails);

		assertNotNull(updatedConsumer);
		assertEquals("Smart TV", updatedConsumer.getName());
	}

	@Test
	public void testDeleteConsumerForUserSuccess() {
		Consumer consumer = new Consumer("TV", 1L, 100.0);
		consumer.setOwnerId(1L);
		when(consumerRepository.findById(1L)).thenReturn(Optional.of(consumer));

		consumerService.deleteConsumerForUser(1L, 1L);

		verify(consumerRepository, times(1)).delete(consumer);
	}

	@Test
	public void testDeleteConsumerForUserUnauthorized() {
		Consumer consumer = new Consumer("TV", 1L, 100.0);
		consumer.setOwnerId(2L);  // Falscher Besitzer
		when(consumerRepository.findById(1L)).thenReturn(Optional.of(consumer));

		assertThrows(UnauthorizedAccessException.class, () -> {
			consumerService.deleteConsumerForUser(1L, 1L);
		});

		verify(consumerRepository, never()).delete(consumer);
	}

	@Test
	public void testGetAllConsumers() {
		List<Consumer> consumers = new ArrayList<>();
		consumers.add(new Consumer("TV", 1L, 100.0));
		consumers.add(new Consumer("Fridge", 2L, 100.0));
		when(consumerRepository.findAll()).thenReturn(consumers);

		List<Consumer> result = consumerService.getAllConsumers();

		assertEquals(2, result.size());
		assertEquals("TV", result.get(0).getName());
		assertEquals("Fridge", result.get(1).getName());
	}

	@Test
	public void testGetConsumerByIdSuccess() {
		Consumer consumer = new Consumer("TV", 1L, 100.0);
		when(consumerRepository.findById(1L)).thenReturn(Optional.of(consumer));

		Consumer result = consumerService.getConsumerById(1L);

		assertNotNull(result);
		assertEquals("TV", result.getName());
	}

	@Test
	public void testGetConsumerByIdNotFound() {
		when(consumerRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(DeviceNotFoundException.class, () -> {
			consumerService.getConsumerById(1L);
		});
	}
}
