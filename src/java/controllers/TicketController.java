package controllers;

import beans.Operator;
import beans.Ticket;
import beans.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class TicketController {

    private Integer id;
    private Integer fk_idstatus;
    private Integer fk_idrequest_type;
    private String comment;
    private Integer fk_id_user;
    private Integer fk_idoperators;
    private Integer idgroup;
    private Integer fk_idticket_type;
    private boolean renderTicketTable;
    private String ticketTypeName;
    private String statusName;
    private String groupName;
    private boolean isOperator;
    private Ticket currentTicket;
    private String newComment;
    private Integer idQuestion;
    private String questionSubject;
    private String answer;

    public Integer getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestionSubject() {
        return questionSubject;
    }

    public void setQuestionSubject(String questionSubject) {
        this.questionSubject = questionSubject;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Ticket getCurrentTicket() {
        return currentTicket;
    }

    public void setCurrentTicket(Ticket currentTicket) {
        this.currentTicket = currentTicket;
    }

    public boolean isRenderTicketTable() {
        return renderTicketTable;
    }

    public void setRenderTicketTable(boolean renderTicketTable) {
        this.renderTicketTable = renderTicketTable;
    }

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFk_idstatus() {
        return fk_idstatus;
    }

    public void setFk_idstatus(Integer fk_idstatus) {
        this.fk_idstatus = fk_idstatus;
    }

    public Integer getFk_idrequest_type() {
        return fk_idrequest_type;
    }

    public void setFk_idrequest_type(Integer fk_idrequest_type) {
        this.fk_idrequest_type = fk_idrequest_type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    }

    public Integer getFk_id_user() {
        return fk_id_user;
    }

    public void setFk_id_user(Integer fk_id_user) {
        this.fk_id_user = fk_id_user;
    }

    public Integer getFk_idoperators() {
        return fk_idoperators;
    }

    public void setFk_idoperators(Integer fk_idoperators) {
        this.fk_idoperators = fk_idoperators;
    }

    public Integer getIdgroup() {
        return idgroup;
    }

    public void setIdgroup(Integer idgroup) {
        this.idgroup = idgroup;
    }

    public Integer getFk_idticket_type() {
        return fk_idticket_type;
    }

    public void setFk_idticket_type(Integer fk_idticket_type) {
        this.fk_idticket_type = fk_idticket_type;
    }

    public void renderTable() {
        renderTicketTable = true;
    }

    public void clear() {
        id = null;
        fk_idstatus = null;
        fk_idrequest_type = null;
        comment = null;
        fk_id_user = null;
        fk_idoperators = null;
        idgroup = null;
        fk_idticket_type = null;
        newComment = null;
        currentTicket = null;
        groupName = null;
        questionSubject = null;
        idQuestion = null;
        answer = null;
    }
    List<Ticket> allTickets;

    public List<Ticket> getAllTickets() {
        return allTickets;
    }

    public void closeTable() {
        if (isOperator) {
            isOperator = false;
        }
        renderTicketTable = false;
    }

    public void addTicketUser(User user) {
        currentUser = user;
    }

    public void setAllTickets(List<Ticket> allTickets) {
        this.allTickets = allTickets;
    }

    public int getFirstQuestion(int fk_idticket_type) {
        Connection conn;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select fk_id_questions from first_questions where fk_idticket_type = " + fk_idticket_type;
            ResultSet rs = stmt.executeQuery(query);
            int idQ = 0;
            while (rs.next()) {
                idQ = rs.getInt("fk_id_questions");
            }
            return idQ;
        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void insertTicket() {
        Connection conn = null; 
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            fk_idoperators = (int) sesija.getAttribute("operatorId");
            comment = (comment == null) ? "" : comment;
            idgroup = (idgroup == null) ? 0 : idgroup;

            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into tickets values (null, CURRENT_TIMESTAMP, ?, ?,'" + comment + "', ?, ?, " + idgroup + ", ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, fk_idstatus);
            ps.setInt(2, fk_idrequest_type);
            ps.setInt(3, fk_id_user);
            ps.setInt(4, fk_idoperators);
            ps.setInt(5, fk_idticket_type);
            ps.executeUpdate();
            renderTicketTable = false;
        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            RequestContext.getCurrentInstance().execute("PF('questionsDialog').show()");
            idQuestion = getFirstQuestion(fk_idticket_type);
            convertIdToQuestion(idQuestion);
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void convertIdToQuestion(int idQ) {
        Connection conn = null; 
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from questions where id_questions = " + idQ;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                setQuestionSubject(rs.getString("question"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }

    }

    public int getTicketId(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from tickets where fk_id_user=" + id + " order by id_ticket desc limit 1";
            ResultSet rs = stmt.executeQuery(query);
            int idTicket = 0;
            while (rs.next()) {
                idTicket = rs.getInt("id_ticket");
            }
            return idTicket;
        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return 0;
    }

    public void getNextQuestion() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select id_question2 from question_associations where answer = \"" + answer + "\" and id_question1 =" + idQuestion + " and fk_id_ticket_type=" + fk_idticket_type;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                idQuestion = rs.getInt("id_question2");
            } else {
                idQuestion = null;
            }

        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void insertAnswer() throws IOException {
        Connection conn = null;
        int idTicket = getTicketId(fk_id_user);
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "insert into user_answers (user_answer, fk_id_user, fk_operators_id, fk_id_ticket, fk_id_question) values(?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, answer);
            ps.setInt(2, fk_id_user);
            ps.setInt(3, fk_idoperators);
            ps.setInt(4, idTicket);
            ps.setInt(5, idQuestion);
            ps.executeUpdate();
            getNextQuestion();
            if (idQuestion == null || idQuestion == 0) {
                RequestContext.getCurrentInstance().execute("PF('questionsDialog').hide()");
                RequestContext.getCurrentInstance().execute("PF('thankYouDialog').show()");
                clear();
            } else {
                convertIdToQuestion(idQuestion);
                answer = "";
            }

        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void renderUserTickets(User user) {
        currentUser = user;
        listUserTickets(user.getId());
        renderTable();
    }

    public void listTickets(int id) {
        if (isOperator) {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            int operatorId = (Integer) sesija.getAttribute("operatorId");
            renderOperatorTickets(operatorId);
        } else {
            listUserTickets(id);
            isOperator = false;
            renderTable();
        }
    }

    public void listUserTickets(int id) {
        if (isOperator) {
            isOperator = false;
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from tickets where fk_id_user=" + id;

            ResultSet rs = stmt.executeQuery(query);
            allTickets = new ArrayList<Ticket>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setIdTicket(rs.getInt("id_ticket"));
                ticket.setTicketDate(rs.getDate("ticket_date"));
                ticket.setIdStatus(rs.getInt("fk_idstatus"));
                ticket.setIdRequestType(rs.getInt("fk_idrequest_type"));
                ticket.setComment(rs.getString("comment"));
                ticket.setIdUser(rs.getInt("fk_id_user"));
                ticket.setIdOperator(rs.getInt("fk_idoperators"));
                ticket.setIdGroup(rs.getInt("idgroup"));
                ticket.setIdTicketType(rs.getInt("fk_idticket_type"));
                allTickets.add(ticket);
            }
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            clear();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void renderOperatorTickets(int id) {
        listOperatorTickets(id);
        isOperator = true;
        renderTable();

    }

    public void listOperatorTickets(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from tickets where fk_idoperators=" + id;

            ResultSet rs = stmt.executeQuery(query);
            allTickets = new ArrayList<Ticket>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setIdTicket(rs.getInt("id_ticket"));
                ticket.setTicketDate(rs.getDate("ticket_date"));
                ticket.setIdStatus(rs.getInt("fk_idstatus"));
                ticket.setIdRequestType(rs.getInt("fk_idrequest_type"));
                ticket.setComment(rs.getString("comment"));
                ticket.setIdUser(rs.getInt("fk_id_user"));
                ticket.setIdOperator(rs.getInt("fk_idoperators"));
                ticket.setIdGroup(rs.getInt("idgroup"));
                ticket.setIdTicketType(rs.getInt("fk_idticket_type"));
                allTickets.add(ticket);
            }
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            clear();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public String takeTicketTypeById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from ticket_type where idticket_type=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ticketTypeName = rs.getString("ticket_type");
            }
            return ticketTypeName;
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String takeStatusById(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from status where idstatus=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                statusName = rs.getString("status_type");
            }
            return statusName;
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String takeGroupById(Integer id) {
        Connection conn = null;
        try {
            if (id == 0 || id == null) {
                return "No group";
            }
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            String query = "select * from ticket_service.group where idgroup=" + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String grName = "";
            while (rs.next()) {
                grName = rs.getString("subject");
            }
            return grName;
        } catch (SQLException ex) {
            System.err.println("Error");
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }

    public String displayName() {
        if (isOperator) {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            return (String) sesija.getAttribute("username");
        } else {
            return currentUser.getFirstName() + " " + currentUser.getLastName();
        }
    }

//    GET TICKET BY ID
    public void setCurrentTicketById(int id) {
        for (int i = 0; i < allTickets.size(); i++) {
            if (allTickets.get(i).getIdTicket() == id) {
                currentTicket = allTickets.get(i);
                fk_idrequest_type = currentTicket.getIdRequestType();
                fk_idstatus = currentTicket.getIdStatus();
            }
        }
    }

    public Ticket findTicket(int id) {
        for (int i = 0; i < allTickets.size(); i++) {
            if (allTickets.get(i).getIdTicket() == id) {
                return allTickets.get(i);
            }
        }
        return null;
    }

    public void updateTicket() {
        Connection conn = null;
        try {
            String closed = (fk_idstatus == 3) ? "Ticket closed" : " ";
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "update tickets set fk_idstatus='" + fk_idstatus + "', ";
            query += "idgroup=" + idgroup + ", ";
            query += "comment= (concat(comment,'\\n',current_timestamp(),'\\n','" + newComment + "','\\n','" + closed + "'))";
            query += " where id_ticket = " + currentTicket.getIdTicket();
            stmt.executeUpdate(query);

            Ticket updatedTicket = findTicket(currentTicket.getIdTicket());
            updatedTicket.setIdStatus(currentTicket.getIdStatus());
            updatedTicket.setComment(currentTicket.getComment());

            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getFlash().setKeepMessages(true);
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Ticket updated", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            clear();
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    //    RELOADS PAGE
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public User getUserByTicketId(int id) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db.DB.connectionString, db.DB.user, db.DB.password);
            Statement stmt = conn.createStatement();
            String query = "select * from users where id_user=" + id;
            ResultSet rs = stmt.executeQuery(query);
            User tempUser = new User();
            while (rs.next()) {
                tempUser.setFirstName(rs.getString("first_name"));
                tempUser.setLastName(rs.getString("last_name"));
                tempUser.setPhoneNumber(rs.getString("phone_number"));
                tempUser.setId(rs.getInt("id_user"));
                tempUser.setEmail(rs.getString("email"));
                tempUser.setAddress(rs.getString("address"));
                tempUser.setIdCity(rs.getInt("id_city"));
            }
            return tempUser;
        } catch (SQLException ex) {
            Logger.getLogger(TicketController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { conn.close(); } catch (Exception e) { /* ignored */ }
        }
        return null;
    }
}
