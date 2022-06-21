package clustering;

public class Method {
    private int methodID;

    public int getMethodID() {
        return methodID;
    }

    public void setMethodID(int methodID) {
        this.methodID = methodID;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    private String sequence;

    Method(int methodID, String sequence)

    {
        this.methodID = methodID;
        this.sequence = sequence;

    }

}
