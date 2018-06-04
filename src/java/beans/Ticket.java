package beans;

import java.sql.Date;

public class Ticket {
    private int idTicket;
    private Date ticketDate;
    private int idTicketType;
    private int idStatus;
    private int idRequestType;
    private String comment;
    private int idUser;
    private int idOperator;
    private int idGroup;

    public Ticket() {
    }

    public int getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(int idTicket) {
        this.idTicket = idTicket;
    }

    public Date getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(Date ticketDate) {
        this.ticketDate = ticketDate;
    }

    public int getIdTicketType() {
        return idTicketType;
    }

    public void setIdTicketType(int idTicketType) {
        this.idTicketType = idTicketType;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdRequestType() {
        return idRequestType;
    }

    public void setIdRequestType(int idRequestType) {
        this.idRequestType = idRequestType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdOperator() {
        return idOperator;
    }

    public void setIdOperator(int idOperator) {
        this.idOperator = idOperator;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }
    
}
