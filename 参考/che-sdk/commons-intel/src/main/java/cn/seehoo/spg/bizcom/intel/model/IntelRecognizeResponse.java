package cn.seehoo.spg.bizcom.intel.model;


import java.util.List;

public class IntelRecognizeResponse {

    private String id;
    private String created;
    private String model;
    private List<Choice> choices;
    private IntelError error;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public IntelError getError() {
        return error;
    }

    public void setError(IntelError error) {
        this.error = error;
    }
}
