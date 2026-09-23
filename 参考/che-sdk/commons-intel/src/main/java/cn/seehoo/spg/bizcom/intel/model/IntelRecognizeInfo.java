package cn.seehoo.spg.bizcom.intel.model;



import java.util.List;

public class IntelRecognizeInfo {

    private String model;

    private List<Message> messages;

    private IntelModelConfig modelConfig;

    private ExtraBody extra_body = new ExtraBody();

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public IntelModelConfig getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(IntelModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public ExtraBody getExtra_body() {
        return extra_body;
    }

    public void setExtra_body(ExtraBody extra_body) {
        this.extra_body = extra_body;
    }
}
