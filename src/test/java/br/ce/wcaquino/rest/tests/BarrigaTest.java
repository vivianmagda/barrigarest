package br.ce.wcaquino.rest.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.ce.wcaquino.rest.core.BaseTest;
import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {

    private String TOKEN;

    @Before //executa antes de cada test
    public void login(){
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
    }

    @Test
    public void t01_naoDeveAcessarAPISemToken() {
        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401);
    }

    @Test
    public void t02_deveIncluirContaComSucesso(){    
            given()
                .header("Authorization", "JWT " + TOKEN) //API mais recente seria bearer
                .body("{\"nome\": \"conta qualquer\"}")
            .when()
                .post("/contas")
            .then()
                .statusCode(201)
            ;
    }

    @Test
    public void t03_deveAlterarContaComSucesso(){    
            given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \"conta alterada\"}")
            .when()
                .put("/contas/2558260")
            .then()
                .statusCode(200)
                .body("nome", is("conta alterada"))
            ;
    }

    @Test
    public void t04_naoDeveIncluirContaComNomeRepetido(){    
            given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \"conta alterada\"}")
            .when()
                .post("/contas")
            .then()
                .statusCode(400)
                .body("error", is("JÃ¡ existe uma conta com esse nome!"))
            ;
    }

    @Test
    public void t05_deveInserirMovimentacaoComSucesso(){    
        Movimentacao mov = getMovimentacaoValida();

            given()
                .header("Authorization", "JWT " + TOKEN)
                .body(mov)
            .when()
                .post("/transacoes")
            .then()
                .statusCode(201)
            ;
    }

    @Test
    public void t06_deveValidarCamposObrigatoriosNaMovimentacao(){    
        given()
            .header("Authorization", "JWT " + TOKEN)
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
        mov.setData_transacao("01/12/2026");

            given()
                .header("Authorization", "JWT " + TOKEN)
                .body(mov)
            .when()
                .post("/transacoes")
            .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItems("Data da Movimentação deve ser menor ou igual à data atual "))
            ;
    }

     @Test
    public void t08_naoDeveRemoverContaComMovimentacao(){ 
            given()
                .header("Authorization", "JWT " + TOKEN)
            .when()
                .delete("/contas/2558260")
            .then()
                .statusCode(500)
                .body("constraint",is("transacoes_conta_id_foreign"))
            ;
    }

     @Test
    public void t09_deveCalcularSaldoContas(){ 
            given()
                .header("Authorization", "JWT " + TOKEN)
            .when()
                .get("/saldo")
            .then()
                .statusCode(200)                
                .body("find{it.conta_id == 2558260}.saldo", is("19500.00"))
            ;
    }

    //2403491 mov a excluir
     @Test
    public void t10_deveRemoverMovimentacao(){ 
            given()
                .header("Authorization", "JWT " + TOKEN)
            .when()
                .delete("/transacoes/2403491")
            .then()
                .statusCode(204)
            ;
    }
    

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(2558260);
        // mov.setUsuario_id(usuario_id);
        mov.setDescricao("Descricao da movimentacao");
        mov.setEnvolvido("Envolvido na movimentacao");
        mov.setTipo("REC"); // ou "DES
        mov.setData_transacao("01/06/2025");
        mov.setData_pagamento("02/06/2025");
        mov.setValor(1500.00f);
        mov.setStatus(true);
        return mov;
    }

    
}