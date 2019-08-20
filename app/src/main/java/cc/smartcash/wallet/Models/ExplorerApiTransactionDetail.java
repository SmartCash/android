package cc.smartcash.wallet.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExplorerApiTransactionDetail implements Serializable {

    @JsonProperty("Transaction")
    private ExplorerApiTransaction transaction;
    @JsonProperty("Inputs")
    private List<ExplorerApiTransactionInput> inputs;
    @JsonProperty("Outputs")
    private List<ExplorerApiTransactionOutput> outputs;

    public ExplorerApiTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(ExplorerApiTransaction transaction) {
        this.transaction = transaction;
    }

    public List<ExplorerApiTransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<ExplorerApiTransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<ExplorerApiTransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ExplorerApiTransactionOutput> outputs) {
        this.outputs = outputs;
    }


}
