package br.ce.wcaquino.rest.tests;

public class Movimentacao {

    private Integer id;
    private Integer conta_id;
    private Integer usuario_id;
    private String descricao;
    private String envolvido;
    private String tipo;
    private String data_transacao;
    private String data_pagamento;
    private Float valor;
    private Boolean status;
    
    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Integer return the conta_id
     */
    public Integer getConta_id() {
        return conta_id;
    }

    /**
     * @param conta_id the conta_id to set
     */
    public void setConta_id(Integer conta_id) {
        this.conta_id = conta_id;
    }

    /**
     * @return Integer return the usuario_id
     */
    public Integer getUsuario_id() {
        return usuario_id;
    }

    /**
     * @param usuario_id the usuario_id to set
     */
    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    /**
     * @return String return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return String return the envolvido
     */
    public String getEnvolvido() {
        return envolvido;
    }

    /**
     * @param envolvido the envolvido to set
     */
    public void setEnvolvido(String envolvido) {
        this.envolvido = envolvido;
    }

    /**
     * @return String return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return String return the data_transacao
     */
    public String getData_transacao() {
        return data_transacao;
    }

    /**
     * @param data_transacao the data_transacao to set
     */
    public void setData_transacao(String data_transacao) {
        this.data_transacao = data_transacao;
    }

    /**
     * @return String return the data_pagamento
     */
    public String getData_pagamento() {
        return data_pagamento;
    }

    /**
     * @param data_pagamento the data_pagamento to set
     */
    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = data_pagamento;
    }

    /**
     * @return Float return the valor
     */
    public Float getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Float valor) {
        this.valor = valor;
    }

    /**
     * @return Boolean return the status
     */
    public Boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

}
