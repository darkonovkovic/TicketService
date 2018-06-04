package beans;

public class RequestType {
    private int idrequest_type;
    private String request_type;
    private boolean editable;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    

    public RequestType() {
    }

    public int getIdrequest_type() {
        return idrequest_type;
    }

    public void setIdrequest_type(int idrequest_type) {
        this.idrequest_type = idrequest_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public void edit() {
        if (editable) {
            editable = false;
        } else {
            editable = true;
        }
    }   
}
