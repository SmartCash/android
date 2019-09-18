package cc.smartcash.wallet.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExplorerApiAddressBalanceWithTxs implements Serializable {
    @JsonProperty("AddressBalance")
    private ExplorerApiAddressBalance addressbalance;
    @JsonProperty("Txs")
    private List<ExplorerApiTxs> txs;

    public ExplorerApiAddressBalanceWithTxs() {
    }

    public ExplorerApiAddressBalanceWithTxs(ExplorerApiAddressBalance addressbalance, List<ExplorerApiTxs> txs) {
        this.addressbalance = addressbalance;
        this.txs = txs;
    }

    public ExplorerApiAddressBalance getAddressbalance() {
        return addressbalance;
    }

    public void setAddressbalance(ExplorerApiAddressBalance addressbalance) {
        this.addressbalance = addressbalance;
    }

    public List<ExplorerApiTxs> getTxs() {
        return txs;
    }

    public void setTxs(List<ExplorerApiTxs> txs) {
        this.txs = txs;
    }


}
