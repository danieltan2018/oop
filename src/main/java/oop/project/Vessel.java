package oop.project;

public class Vessel {

    String imoN;
    String fullVslM;
    String abbrVslM;
    String fullInVoyN;
    String inVoyN;
    String fullOutVoyN;
    String outVoyN;
    String shiftSeqN;
    String bthgDt;
    String unbthgDt;
    String berthN;
    String status;
    String abbrTerminalM;
    String changeCount;
    String originalTime;

    public Vessel() {
    }

    public Vessel(String imoN, String fullVslM, String abbrVslM, String fullInVoyN, String inVoyN, String fullOutVoyN,
            String outVoyN, String shiftSeqN, String bthgDt, String unbthgDt, String berthN, String status,
            String abbrTerminalM, String changeCount, String originalTime) {
        this.imoN = imoN;
        this.fullVslM = fullVslM;
        this.abbrVslM = abbrVslM;
        this.fullInVoyN = fullInVoyN;
        this.inVoyN = inVoyN;
        this.fullOutVoyN = fullOutVoyN;
        this.outVoyN = outVoyN;
        this.shiftSeqN = shiftSeqN;
        this.bthgDt = bthgDt;
        this.unbthgDt = unbthgDt;
        this.berthN = berthN;
        this.status = status;
        this.abbrTerminalM = abbrTerminalM;
        this.changeCount = changeCount;
        this.originalTime = originalTime;
    }

    public String getImoN() {
        return this.imoN;
    }

    public void setImoN(String imoN) {
        this.imoN = imoN;
    }

    public String getFullVslM() {
        return this.fullVslM;
    }

    public void setFullVslM(String fullVslM) {
        this.fullVslM = fullVslM;
    }

    public String getAbbrVslM() {
        return this.abbrVslM;
    }

    public void setAbbrVslM(String abbrVslM) {
        this.abbrVslM = abbrVslM;
    }

    public String getFullInVoyN() {
        return this.fullInVoyN;
    }

    public void setFullInVoyN(String fullInVoyN) {
        this.fullInVoyN = fullInVoyN;
    }

    public String getInVoyN() {
        return this.inVoyN;
    }

    public void setInVoyN(String inVoyN) {
        this.inVoyN = inVoyN;
    }

    public String getFullOutVoyN() {
        return this.fullOutVoyN;
    }

    public void setFullOutVoyN(String fullOutVoyN) {
        this.fullOutVoyN = fullOutVoyN;
    }

    public String getOutVoyN() {
        return this.outVoyN;
    }

    public void setOutVoyN(String outVoyN) {
        this.outVoyN = outVoyN;
    }

    public String getShiftSeqN() {
        return this.shiftSeqN;
    }

    public void setShiftSeqN(String shiftSeqN) {
        this.shiftSeqN = shiftSeqN;
    }

    public String getBthgDt() {
        return this.bthgDt;
    }

    public void setBthgDt(String bthgDt) {
        this.bthgDt = bthgDt;
    }

    public String getUnbthgDt() {
        return this.unbthgDt;
    }

    public void setUnbthgDt(String unbthgDt) {
        this.unbthgDt = unbthgDt;
    }

    public String getBerthN() {
        return this.berthN;
    }

    public void setBerthN(String berthN) {
        this.berthN = berthN;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAbbrTerminalM() {
        return this.abbrTerminalM;
    }

    public void setAbbrTerminalM(String abbrTerminalM) {
        this.abbrTerminalM = abbrTerminalM;
    }

    public String getChangeCount() {
        return this.changeCount;
    }

    public void setChangeCount(String changeCount) {
        this.changeCount = changeCount;
    }

    public String getOriginalTime() {
        return this.originalTime;
    }

    public void setOriginalTime(String originalTime) {
        this.originalTime = originalTime;
    }

    @Override
    public String toString() {
        return "{" + " imoN='" + getImoN() + "'" + ", fullVslM='" + getFullVslM() + "'" + ", abbrVslM='" + getAbbrVslM()
                + "'" + ", fullInVoyN='" + getFullInVoyN() + "'" + ", inVoyN='" + getInVoyN() + "'" + ", fullOutVoyN='"
                + getFullOutVoyN() + "'" + ", outVoyN='" + getOutVoyN() + "'" + ", shiftSeqN='" + getShiftSeqN() + "'"
                + ", bthgDt='" + getBthgDt() + "'" + ", unbthgDt='" + getUnbthgDt() + "'" + ", berthN='" + getBerthN()
                + "'" + ", status='" + getStatus() + "'" + ", abbrTerminalM='" + getAbbrTerminalM() + "'"
                + ", changeCount='" + getChangeCount() + "'" + ", originalTime='" + getOriginalTime() + "'" + "}";
    }

}