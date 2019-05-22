package NetStuff.Mail;

public enum MailService {

    GMAIL("smtp.gmail.com","587"),
    MAIL_RU("smtp.mail.ru","465");


    private String host;
    private String port;

    MailService(String host, String port){
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}
