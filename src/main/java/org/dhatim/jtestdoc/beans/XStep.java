package org.dhatim.jtestdoc.beans;

import java.util.List;

/**
 * The XStep class is the model for the processed steps
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class XStep {

    private String step;
    private List<String> expectedResult;

    public XStep() {
    }

    public XStep(String step, List<String> expectedResult) {
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

    public List<String> getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(List<String> expectedResult) {
        this.expectedResult = expectedResult;
    }

}
