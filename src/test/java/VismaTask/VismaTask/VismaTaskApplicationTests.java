package VismaTask.VismaTask;

import VismaTask.VismaTask.Services.DataRW;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.PrintWriter;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VismaTaskApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	public static void setup() {
		DataRW.fileName = "meetings_testing.json";
		try (PrintWriter out = new PrintWriter(VismaTaskApplication.class.getClassLoader().getResource("meetings_testing.json").getFile())) {
			out.write("[{\"id\":1,\"name\":\"labas dienas\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-09-02 18:00\",\"endDate\":\"2023-09-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"},{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriuxx@gmail.com\"}]},{\"id\":2,\"name\":\"labas vakaras\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-06-02 18:00\",\"endDate\":\"2023-06-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"}]},{\"id\":3,\"name\":\"labas rytas\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-06-02 18:00\",\"endDate\":\"2023-06-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"},{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriuxx@gmail.com\"}]}]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Order(1)
	void getAllMeetingsReturnsAllMeetings() throws Exception {
		String body = this.restTemplate.getForObject("/meetings", String.class);
		assertThat(body).isEqualTo("[{\"id\":1,\"name\":\"labas dienas\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-09-02 18:00\",\"endDate\":\"2023-09-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"},{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriuxx@gmail.com\"}]},{\"id\":2,\"name\":\"labas vakaras\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-06-02 18:00\",\"endDate\":\"2023-06-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"}]},{\"id\":3,\"name\":\"labas rytas\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-06-02 18:00\",\"endDate\":\"2023-06-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"},{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriuxx@gmail.com\"}]}]");
	}

	@Test
	@Order(2)
	void getMeetingsReturnsNotFound() throws Exception {
		String body = this.restTemplate.getForObject("/meetings?min_attendees=10", String.class);
		assertThat(body).isEqualTo("NOT FOUND");
	}

	@Test
	@Order(3)
	void getAllMeetingsAllFilters() throws Exception {
		String body = this.restTemplate.getForObject("/meetings?desc=labas&resp=petriux@gmail.com&category=CodeMonkey&type=Live&start_date=2020-01-01&end_date=2030-01-01&min_attendees=0&max_attendees=100", String.class);
		assertThat(body).isEqualTo("[{\"id\":1,\"name\":\"labas dienas\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-09-02 18:00\",\"endDate\":\"2023-09-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"},{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriuxx@gmail.com\"}]},{\"id\":2,\"name\":\"labas vakaras\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-06-02 18:00\",\"endDate\":\"2023-06-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"}]},{\"id\":3,\"name\":\"labas rytas\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2023-06-02 18:00\",\"endDate\":\"2023-06-02 19:00\",\"responsiblePerson\":\"petriux@gmail.com\",\"participants\":[{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriux@gmail.com\"},{\"dateAdded\":\"2023-06-07 18:13\",\"emailAddress\":\"petriuxx@gmail.com\"}]}]");
	}

	@Test
	@Order(4)
	void addPersonToMeetingReturnsWarning() throws Exception {
		final String uri = "/meetings/2/person";
		String body = "{\"emailAddress\": \"petriuxx@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("WARNING: this meeting overlaps with another meeting this person is participating in");
	}

	@Test
	@Order(5)
	void addPersonToMeetingReturnsErrorAlreadyAdded() throws Exception {
		final String uri = "/meetings/1/person";
		String body = "{\"emailAddress\": \"petriuxx@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Person has already been added to this meeting");
	}

	@Test
	@Order(6)
	void addPersonToMeetingReturnsErrorMeetingNotFound() throws Exception {
		final String uri = "/meetings/5/person";
		String body = "{\"emailAddress\": \"petriuxx@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Meeting with this ID has not been found");
	}

	@Test
	@Order(7)
	void addPersonToMeetingReturnsSuccess() throws Exception {
		final String uri = "/meetings/1/person";
		String body = "{\"emailAddress\": \"petriuxttt@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Added successfully");
	}

	@Test
	@Order(8)
	void removePersonFromMeetingReturnsMeetingNotFound() throws Exception {
		final String uri = "/meetings/5/person";
		String body = "{\"emailAddress\": \"petriuxx@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Meeting with this ID has not been found");
	}

	@Test
	@Order(9)
	void removePersonFromMeetingReturnsCannotRemoveResponsible() throws Exception {
		final String uri = "/meetings/1/person";
		String body = "{\"emailAddress\": \"petriux@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Cannot remove a responsible person");
	}

	@Test
	@Order(10)
	void removePersonFromMeetingReturnsParticipantNotFound() throws Exception {
		final String uri = "/meetings/1/person";
		String body = "{\"emailAddress\": \"notfound@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Could not find a participant in this meeting");
	}

	@Test
	@Order(11)
	void removePersonFromMeetingReturnsSuccess() throws Exception {
		final String uri = "/meetings/1/person";
		String body = "{\"emailAddress\": \"petriuxx@gmail.com\"}";
		HttpEntity<String> request = new HttpEntity<>(body);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Removed successfully");
	}

	@Test
	@Order(12)
	void removeMeetingReturnsErrorNotResponsible() throws Exception {
		final String uri = "/meetings/1";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic cGV0cml1eGFzQGdtYWlsLmNvbTpwYXNzd29yZA==");
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("You are not responsible for this meeting and cannot delete it");
	}

	@Test
	@Order(13)
	void removeMeetingReturnsErrorMeetingNotFound() throws Exception {
		final String uri = "/meetings/5";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic cGV0cml1eGFzQGdtYWlsLmNvbTpwYXNzd29yZA==");
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Meeting not found");
	}

	@Test
	@Order(14)
	void removeMeetingReturnsErrorNotAuthorized() throws Exception {
		final String uri = "/meetings/1";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer cGV0cml1eGFzQGdtYWlsLmNvbTpwYXNzd29yZA==");
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Authorization failed");
	}

	@Test
	@Order(15)
	void removeMeetingReturnsSuccess() throws Exception {
		final String uri = "/meetings/1";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic cGV0cml1eEBnbWFpbC5jb206cGFzc3dvcmQ=");
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, request, String.class);
		assertThat(response.getBody()).isEqualTo("Deleted successfully");
	}

	@Test
	@Order(16)
	void createNewMeetingReturnsErrorCategoryIncorrect() throws Exception {
		final String uri = "/meetings";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		String body = "{\"id\":4,\"name\":\"labas rytas\",\"responsiblePerson\":\"petriux@gmail.com\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"gera diena\",\"type\":\"Live\",\"startDate\":\"2024-06-02 18:00\",\"endDate\":\"2024-06-02 19:00\"}";
		HttpEntity<String> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Category is not correct");
	}

	@Test
	@Order(17)
	void createNewMeetingReturnsErrorTypeIncorrect() throws Exception {
		final String uri = "/meetings";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		String body = "{\"id\":4,\"name\":\"labas rytas\",\"responsiblePerson\":\"petriux@gmail.com\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live not correct\",\"startDate\":\"2024-06-02 18:00\",\"endDate\":\"2024-06-02 19:00\"}";
		HttpEntity<String> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Type is not correct");
	}

	@Test
	@Order(18)
	void createNewMeetingReturnsSuccess() throws Exception {
		final String uri = "/meetings";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		String body = "{\"id\":4,\"name\":\"labas rytas\",\"responsiblePerson\":\"petriux@gmail.com\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2024-06-02 18:00\",\"endDate\":\"2024-06-02 19:00\"}";
		HttpEntity<String> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Created successfully");
	}

	@Test
	@Order(19)
	void createNewMeetingReturnsErrorIdExists() throws Exception {
		final String uri = "/meetings";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		String body = "{\"id\":2,\"name\":\"labas rytas\",\"responsiblePerson\":\"petriux@gmail.com\",\"description\":\"jkashdjk askjdh aksd haslkd halksd as\",\"category\":\"CodeMonkey\",\"type\":\"Live\",\"startDate\":\"2024-06-02 18:00\",\"endDate\":\"2024-06-02 19:00\"}";
		HttpEntity<String> request = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		assertThat(response.getBody()).isEqualTo("Meeting with this ID already exists");
	}
}
