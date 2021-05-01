package loja;

import io.restassured.response.Response;
import javafx.animation.PathTransitionBuilder;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Servico {

    String tokenGeral;

    public String lerJason(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));

    }

    @Test
    public void ordemDaExecucao() throws IOException {
        incluirPet();
        consultarPet();
        alterarPet();
        excluirPet();
    }

    // Create / Incluir / POST
    @Test
    public void incluirPet() throws IOException {

        String jsonBody = lerJason("data/pet.json");

        given()                                 // Dado que
                .contentType("application/json")    // Tipo de conteúdo da requisição
                .log().all()                        // Gera um log completo da requisição
                .body(jsonBody)                     // Conteúdo do corpo da requisição
                .when()
                .post("https://petstore.swagger.io/v2/pet") // Operação e endpoint(URi)
                .then()
                .log().all()                        // Gerar um log completo da resposta
                .statusCode(200)                    // Validou o código de status da requisição como 200
                //.body("code", is(200))   // Valida o code como 200
                .body("id", is(42157))    // Validou a tag id com o conteúdo esperado (usar o 'is' do Matches hamcrest)
                .body("name", is("Mutley"))
                .body("tags.name", contains("cartoon"))
        ;
        System.out.println("Executou o serviço");

    }

    // Reach or Research / Consultar / Get
    @Test
    public void consultarPet() {
        String petId = "42157";

        given()                                             // Dado que
                .contentType("application/json")                        // Tipo de conteúdo da requisição
                .log().all()                                            // Mostrar tudo que foi enviado
                .when()                                             // Quando
                .get("https://petstore.swagger.io/v2/pet/" + petId) // Consulta pelo petId
                .then()                                             // Então
                .log().all()                                            // Mostrar tudo que foi recebido
                .statusCode(200)                                        // Validou que a operação foi realizada
                .body("name", is("Mutley"))                // Validou o nome do pet
                .body("category.name", is("dog"))            // Validou a espécie
        ;
    }

    @Test
    public void alterarPet() throws IOException {
        String jsonBody = lerJason("data/petput.json");

        given()                                 // Dado que
                .contentType("application/json")    // Tipo de conteúdo da requisição
                .log().all()                        // Gera um log completo da requisição
                .body(jsonBody)                     // Conteúdo do corpo da requisição
                .when()
                .put("https://petstore.swagger.io/v2/pet/")
                .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Mutley"))
                .body("status", is("adopted"))
        ;

    }

    // Delete / Excluir / Delete
    @Test
    public void excluirPet() {
        String petId = "42157";

        given()                                             // Dado que
                .contentType("application/json")                        // Tipo de conteúdo da requisição
                .log().all()                                            // Mostrar tudo que foi enviado
                .when()                                             // Quando
                .delete("https://petstore.swagger.io/v2/pet/" + petId) // Consulta pelo petId
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("message", is(petId))
        ;
    }

    // Login
    @Test
    public void loginUser(){
        // public String loginUser(){

        String token =
                given()                                             // Dado que
                        .contentType("application/json")                        // Tipo de conteúdo da requisição
                        .log().all()                                            // Mostrar tudo que foi enviado
                        .when()
                        .get("https://petstore.swagger.io/v2/user/login?username=charlie&password=brown")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("message", containsString("logged in user session:"))
                        .extract()
                        .path("message")
                ;
        tokenGeral = token.substring(24); // separa o token da frase
        System.out.println("O token valido eh " + tokenGeral);
        //return tokenGeral;
    }

    @Test
    public void sempreMetodo(){
        loginUser2();
    }

    public String loginUser2(){

        String token =
                given()                                             // Dado que
                        .contentType("application/json")                        // Tipo de conteúdo da requisição
                        .log().all()                                            // Mostrar tudo que foi enviado
                        .when()
                        .get("https://petstore.swagger.io/v2/user/login?username=charlie&password=brown")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .body("message", containsString("logged in user session:"))
                        .extract()
                        .path("message")
                ;
        tokenGeral = token.substring(24); // separa o token da frase
        System.out.println("O token valido eh " + tokenGeral);
        return tokenGeral;
    }
}


