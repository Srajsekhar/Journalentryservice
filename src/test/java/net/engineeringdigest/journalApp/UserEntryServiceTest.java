package net.engineeringdigest.journalApp;// Import these
import static java.util.Arrays.asList;
import static net.engineeringdigest.journalApp.service.UserEntryService.passwordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import net.engineeringdigest.journalApp.entity.Userentry;
import net.engineeringdigest.journalApp.repositry.UserentryRepo;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserEntryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import javax.xml.transform.Result;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class UserEntryServiceTest {

	@Mock
	private UserentryRepo userentryRepo;    // 1. MOCK the repo

	@InjectMocks
	private UserEntryService userEntryService;    // 2. Inject MOCK into Service
	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;
	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);    // 3. Initialize Mocks
	}

	@Test
	void testSaveUserEntry_shouldSaveWithEncodedPasswordAndRole() {
		// 4. Create dummy Userentry
		Userentry user = new Userentry();
		user.setName("testuser");
		user.setPassword("plaintextpassword");

		// 5. Call service method
		userEntryService.saveUserEntry(user);

		// 6. Verify that repo.save() was called once
		verify(userentryRepo, times(1)).save(any(Userentry.class));

		// 7. Assert password is encoded
		assertNotEquals("plaintextpassword", user.getPassword()); // It should NOT be plain text
		assertTrue(user.getPassword().startsWith("$2a$")); // bcrypt encoded passwords start like this

		// 8. Assert roles set properly
		assertEquals(Collections.singletonList("USERS"), user.getRoles());
	}
	@Test
	void UserDetailsServiceImpl(){
		Userentry user = new Userentry();
		user.setName("testuser");
		user.setPassword("plaintextpassword");
		user.setRoles(asList("USERS"));
		when(userentryRepo.findByName("testuser")).thenReturn((user));
		UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
		assertEquals(userDetails.getUsername(),user.getName());
		assertEquals(userDetails.getPassword(), user.getPassword());
		assertNotNull(userDetails);
		verify(userentryRepo, times(1)).findByName("testuser");
		assertTrue(userDetails.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_USERS")));
	}
	@Test
	void loadUserByUsername_shouldThrowException_whenUserNotFound() {
		// Arrange
		String username = "nonexistent_user";
		when(userentryRepo.findByName(username)).thenReturn(null);

		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () ->
				userDetailsService.loadUserByUsername("nonexistent_user")
		);
		// Verify
		verify(userentryRepo, times(1)).findByName(username);
	}


	@Test
	public void saveUserEntry_ShouldEncodePasswordAndSetDefaultRole() {
		// Arrange
		Userentry user = new Userentry();
		user.setPassword("plainPassword");

		// Mock the repository save method to return the same user
		when(userentryRepo.save(any(Userentry.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// Act
		userEntryService.saveUserEntry(user);

		// Assert
		// Verify password was encoded (not equal to plain text)
		assertNotEquals("plainPassword", user.getPassword());
		assertTrue(passwordEncoder.matches("plainPassword", user.getPassword()));

		// Verify default role was set
		assertEquals(List.of("USERS"), user.getRoles());

		// Verify repository was called
		verify(userentryRepo).save(user);
	}
	@Test
	public  void getAlltest(){
		Userentry user1 = new Userentry();
		user1.setId("1");
		user1.setName("john");
		user1.setPassword("encoded1");
		user1.setRoles(List.of("USER"));

		Userentry user2 = new Userentry();
		user2.setId("2");
		user2.setName("jane");
		user2.setPassword("encoded2");
		user2.setRoles(List.of("ADMIN"));
		when(userentryRepo.findAll()).thenReturn(List.of(user1,user2));
		List<Userentry> result = userEntryService.getAll();
		assertNotNull(result);
		assertEquals("1", result.get(0).getId());
		assertEquals("john", result.get(0).getName());
		assertEquals("encoded1", result.get(0).getPassword());
		assertTrue(result.get(0).getRoles().contains("USER"));
		assertEquals("2", result.get(1).getId());
		assertEquals("jane", result.get(1).getName());
		assertTrue(result.get(1).getRoles().contains("ADMIN"));
	}
	@Test
	public void saveNewUser_ShouldAssignDefaultRole() {
		// Arrange
		Userentry newUser = new Userentry();
		newUser.setName("newuser");
		newUser.setPassword("plainpass");

		when(userentryRepo.save(any(Userentry.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		// Act
		userEntryService.saveUserEntry(newUser);
		// Assert
		assertNotNull(newUser.getRoles());
		assertEquals(1, newUser.getRoles().size());
		assertTrue(newUser.getRoles().contains("USERS"));
	}
}
