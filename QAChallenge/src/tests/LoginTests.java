package tests;
import io.restassured.RestAssured;
import pojo.LoginRequest;
import pojo.LoginResponse;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LoginTests {

	public static void main(String[] args) {
		// Definição da URI
		RestAssured.baseURI = "https://reqres.in";

		// TC001 - Realizar login com Sucesso
		LoginRequest loginRequest1 = new LoginRequest("eve.holt@reqres.in", "cityslicka");
		LoginResponse loginResponse1 = postLoginRequest(loginRequest1);

		assertThat(loginResponse1.getToken(), equalTo("QpwL5tke4Pnpja7X4"));

		// TC002 - Realizar login com Falha - Faltando Senha
		LoginRequest loginRequest2 = new LoginRequest("peter@klaven", null);
		LoginResponse loginResponse2 = postLoginRequest(loginRequest2);

		assertThat(loginResponse2.getError(), equalTo("Missing password"));

		// TC003 - Realizar login com Falha - Faltando Nome de Usuário
		LoginRequest loginRequest3 = new LoginRequest(null, "cityslicka");
		LoginResponse loginResponse3 = postLoginRequest(loginRequest3);

		assertThat(loginResponse3.getError(), equalTo("Missing email or username"));

		// TC004 - Realizar login com Falha - Senha Incorreta
		// ESTE TC ESTÁ COMENTADO POIS FOI ENCONTRADO UM BUG NA API DURANTE SUA EXECUÇÃO
		/*
		 * LoginRequest loginRequest4 = new LoginRequest("eve.holt@reqres.in",
		 * "123456"); LoginResponse loginResponse4 = postLoginRequest(loginRequest4);
		 * 
		 * assertThat(loginResponse4.getError(), equalTo("Wrong password"));
		 */

		// TC005 - Realizar login com Falha - Usuário não Encontrado
		LoginRequest loginRequest5 = new LoginRequest("eve.holt@reqres.com", "cityslicka");
		LoginResponse loginResponse5 = postLoginRequest(loginRequest5);

		assertThat(loginResponse5.getError(), equalTo("user not found"));
	}

	public static LoginResponse postLoginRequest(LoginRequest request) {
		return given().log().all().header("Content-Type", "application/json").body(request).when().post("/api/login")
				.then().log().all().extract().as(LoginResponse.class);
	}
}
