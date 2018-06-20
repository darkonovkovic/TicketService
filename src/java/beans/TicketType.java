package beans;

public class TicketType {
    private int idticket_type;
    private String ticket_type;
    private boolean editable;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public TicketType() {
    }

    public int getIdticket_type() {
        return idticket_type;
    }

    public void setIdticket_type(int idticket_type) {
        this.idticket_type = idticket_type;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }
    
    public void edit() {
        if (editable) {
            editable = false;
        } else {
            editable = true;
        }
    }
}
