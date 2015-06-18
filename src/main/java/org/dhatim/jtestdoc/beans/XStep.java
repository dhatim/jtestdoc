package org.dhatim.jtestdoc.beans;

import java.util.ArrayList;

public class XStep {

    private String step;
    private ArrayList<String> expectedResult;

    public XStep() {
    }

    public XStep(String step, ArrayList<String> expectedResult) {
        super();
        this.step = step;
        this.expectedResult = expectedResult;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public ArrayList<String> getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(ArrayList<String> expectedResult) {
        this.expectedResult = expectedResult;
    }

}
