package beans;

public class QuestionAssociation {

    private Question firstQuestion;
    private Question secondQuestion;
    private String answer;
    private int fk_id_ticket_type;
    private boolean editable;
    private boolean renderEdit;
    private boolean renderedMenu;
    private boolean renderedInput;
    private boolean renderedOutput;
    private boolean renderedOutputMenu;

    public QuestionAssociation() {
    }

    public boolean isRenderedInput() {
        return renderedInput;
    }

    public void setRenderedInput(boolean renderedInput) {
        this.renderedInput = renderedInput;
    }

    public boolean isRenderedOutput() {
        return renderedOutput;
    }

    public void setRenderedOutput(boolean renderedOutput) {
        this.renderedOutput = renderedOutput;
    }

    public boolean isRenderedOutputMenu() {
        return renderedOutputMenu;
    }

    public void setRenderedOutputMenu(boolean renderedOutputMenu) {
        this.renderedOutputMenu = renderedOutputMenu;
    }

    public boolean isRenderedMenu() {
        return renderedMenu;
    }

    public void setRenderedMenu(boolean renderedMenu) {
        this.renderedMenu = renderedMenu;
    }

    public boolean isRenderEdit() {
        return renderEdit;
    }

    public void setRenderEdit(boolean renderEdit) {
        this.renderEdit = renderEdit;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Question getFirstQuestion() {
        return firstQuestion;
    }

    public void setFirstQuestion(Question firstQuestion) {
        this.firstQuestion = firstQuestion;
    }

    public Question getSecondQuestion() {
        return secondQuestion;
    }

    public void setSecondQuestion(Question secondQuestion) {
        this.secondQuestion = secondQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getFk_id_ticket_type() {
        return fk_id_ticket_type;
    }

    public void setFk_id_ticket_type(int fk_id_ticket_type) {
        this.fk_id_ticket_type = fk_id_ticket_type;
    }

    public void edit() {
        if (renderEdit && editable) {
            editable = false;
            renderedInput = false;
            renderedOutput = true;
        } else if (renderEdit && !editable) {
            editable = true;
            renderedInput = true;
            renderedOutput = false;
        } else {
            renderedInput = false;
            renderedOutput = false;
        }
    }

    public void renderMenu() {
        if (!renderEdit && editable) {
            editable = false;
            renderedMenu = false;
            renderedOutputMenu = true;
        } else if (!renderEdit && !editable) {
            editable = true;
            renderedMenu = true;
            renderedOutputMenu = false;
        } else {
            renderedMenu = false;
            renderedOutputMenu = false;
        }
    }

    public boolean renderEditButton() {
        if (renderEdit && !editable) {
            return true;
        } else {
            return false;
        }
    }

    public boolean renderUpdateEditButton() {
        if (renderEdit && editable) {
            return true;
        } else {
            return false;
        }
    }

    public boolean renderAddButton() {
        if (!renderEdit && !editable) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean renderUpdateAddButton() {
        if (!renderEdit && editable) {
            return true;
        } else {
            return false;
        }
    }    

}
