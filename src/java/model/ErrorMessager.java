package model;

public class ErrorMessager {
    
    private Integer statusCode;
    private String statusMessage;
    private String erro;

    public ErrorMessager (){
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    @Override
    public String toString() {
        return "ErrorTeste{" + "statusCode=" + statusCode + ", statusMessage=" + statusMessage + ", erro=" + erro + '}';
    }
    
    
    
}
