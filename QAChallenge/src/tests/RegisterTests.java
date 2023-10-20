package tests;
import io.restassured.RestAssured;
import pojo.RegisterRequest;
import pojo.RegisterResponse;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class RegisterTests {

	public static void main(String[] args) {
		RestAssured.baseURI = "https://reqres.in";

		// TC006 - Cadastrar Usuário com Sucesso
		RegisterRequest registerRequest1 = new RegisterRequest("eve.holt@reqres.in", "pistol");
		RegisterResponse registerResponse1 = postRegisterRequest(registerRequest1);

		assertThat(registerResponse1.getId(), is(notNullValue()));
		assertThat(registerResponse1.getToken(), is(notNullValue()));

		// TC007 - Cadastrar Usuário com Falha - Faltando Senha
		RegisterRequest registerRequest2 = new RegisterRequest("sydney@fife", null);
		RegisterResponse registerResponse2 = postRegisterRequest(registerRequest2);

		assertThat(registerResponse2.getError(), equalTo("Missing password"));

		// TC008 - Cadastrar Usuário com Falha - Faltando Nome de Usuário
		RegisterRequest registerRequest3 = new RegisterRequest(null, "pistol");
		RegisterResponse registerResponse3 = postRegisterRequest(registerRequest3);

		assertThat(registerResponse3.getError(), equalTo("Missing email or username"));
	}

	public static RegisterResponse postRegisterRequest(RegisterRequest request) {
		return given().log().all().header("Content-Type", "application/json").body(request).when().post("/api/register")
				.then().log().all().extract().as(RegisterResponse.class);
	}
}
