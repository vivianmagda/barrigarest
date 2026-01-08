package br.ce.wcaquino.rest.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.utils.DataUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class BarrigaTest extends BaseTest {

    private static String CONTA_NAME = "Conta" + System.nanoTime();
    private static Integer CONTA_ID; 
    private static Integer MOV_ID;
    private static String TOKEN;

    @BeforeClass //executa uma vez antes da classe
    public static void login(){
         Map<String, String> login = new HashMap<>();
        login.put("email", "vivianmagda@live.com");
        login.put("senha", "6ir.Le,J5G6'FA6");

        TOKEN = given()
            .body(login)
        .when()
            .post("/signin")
        .then()
            .statusCode(200)
            .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN); //API mais recente seria bearer
    }

    @Test
    public void t01_naoDeveAcessarAPISemToken() {
        // Cria uma especificação local sem Authorization
       FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
       req.removeHeader("Authorization"); // tira o token global


        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401);

            // recoloca o token para os próximos testes
    req.header("Authorization", "JWT " + TOKEN);


    }

    @Test
    public void t02_deveIncluirContaComSucesso(){    
            CONTA_ID = given()
                .body("{\"nome\": \""+CONTA_NAME+"\"}")
            .when()
                .post("/contas")
            .then()
                .statusCode(201)
                .extract().path("id")
            ;
    }

    @Test
    public void t03_deveAlterarContaComSucesso(){    
            given()
                .body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
                .pathParam("id", CONTA_ID)
            .when()
                .put("/contas/{id}")
            .then()
                .statusCode(200)
                .body("nome", is(CONTA_NAME + " alterada"))
            ;
    }

    @Test
    public void t04_naoDeveIncluirContaComNomeRepetido(){    
            given()
                .body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
            .when()
                .post("/contas")
            .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
            ;
    }

    @Test
    public void t05_deveInserirMovimentacaoComSucesso(){    
        Movimentacao mov = getMovimentacaoValida();

            MOV_ID = given()
                .body(mov)
            .when()
                .post("/transacoes")
            .then()
                .statusCode(201)
                .extract().path("id")
            ;
    }

    @Test
    public void t06_deveValidarCamposObrigatoriosNaMovimentacao(){    
        given()
            .body("{}")
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$", hasSize(8))
            .body("msg", hasItems(
                "Data da Movimentação é obrigatório",
                  "Data do pagamento é obrigatório",
                  "Descrição é obrigatório",
                  "Interessado é obrigatório",
                  "Valor é obrigatório",
                  "Valor deve ser um número",
                  "Conta é obrigatório",
                  "Situação é obrigatório"
            ))
        ;
    }

    @Test
    public void t07_naoDeveCadastrarMovimentacaoFutura(){    
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2)); //retorna a data de depois de amanha ja formatada em String

            given()
                .body(mov)
            .when()
                .post("/transacoes")
            .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItems("Data da Movimentação deve ser menor ou igual à data atual"))
            ;
    }

     @Test
    public void t08_naoDeveRemoverContaComMovimentacao(){ 
            given()       
                .pathParam("id", CONTA_ID)
            .when()
                .delete("/contas/{id}")
            .then()
                .statusCode(500)
                .body("constraint",is("transacoes_conta_id_foreign"))
            ;
    }

     @Test
    public void t09_deveCalcularSaldoContas(){ 
            given()
            .when()
                .get("/saldo")
            .then()
                .statusCode(200)                
                .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("1500.00"))
            ;
    }

    //2403491 mov a excluir
     @Test
    public void t10_deveRemoverMovimentacao(){ 
            given()
                .pathParam("id", MOV_ID)
            .when()
                .delete("/transacoes/{id}")
            .then()
                .statusCode(204)
            ;
    }
    

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(CONTA_ID);
        // mov.setUsuario_id(usuario_id);
        mov.setDescricao("Descricao da movimentacao");
        mov.setEnvolvido("Envolvido na movimentacao");
        mov.setTipo("REC"); // ou "DES
        mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
        mov.setValor(1500.00f);
        mov.setStatus(true);
        return mov;
    }

    
}