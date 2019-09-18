package cc.smartcash.wallet.Services;

import cc.smartcash.wallet.Models.SendPayment;
import cc.smartcash.wallet.Models.TransactionResponse;
import cc.smartcash.wallet.Models.WalletPaymentFeeRequest;
import cc.smartcash.wallet.Models.WebWalletRootResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WalletService {

    @POST("wallet/sendpayment")
    Call<TransactionResponse> sentPayment(@Header("Authorization") String auth, @Body SendPayment sendPayment);

    @POST("wallet/paymentfee")
    Call<WebWalletRootResponse<Double>> getFee(@Header("Authorization") String auth, @Body WalletPaymentFeeRequest feeRequest);
}