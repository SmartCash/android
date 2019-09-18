package cc.smartcash.wallet.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRecoveryKey {

    @JsonProperty("recoveryKey")
    private String recoveryKey;
    @JsonProperty("termsVersion")
    private String termsVersion;
    @JsonProperty("termsText")
    private String termsText;

    @JsonProperty("recoveryKey")
    public String getRecoveryKey() {
        return recoveryKey;
    }

    @JsonProperty("recoveryKey")
    public void setRecoveryKey(String recoveryKey) {
        this.recoveryKey = recoveryKey;
    }

    @JsonProperty("termsVersion")
    public String getTermsVersion() {
        return termsVersion;
    }

    @JsonProperty("termsVersion")
    public void setTermsVersion(String termsVersion) {
        this.termsVersion = termsVersion;
    }

    @JsonProperty("termsText")
    public String getTermsText() {
        return termsText;
    }

    @JsonProperty("termsText")
    public void setTermsText(String termsText) {
        this.termsText = termsText;
    }

}
