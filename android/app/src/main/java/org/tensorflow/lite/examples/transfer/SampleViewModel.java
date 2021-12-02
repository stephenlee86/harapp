package org.tensorflow.lite.examples.transfer;

public class SampleViewModel {
    private String sampleText;

    public SampleViewModel(final String sampleText) {
        setSampleText(sampleText);
    }
    
    public String getSampleText() {
        return sampleText;
    }

    public void setSampleText(final String sampleText) {
        this.sampleText = sampleText;
    }
}