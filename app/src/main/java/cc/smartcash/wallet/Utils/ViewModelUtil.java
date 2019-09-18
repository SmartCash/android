package cc.smartcash.wallet.Utils;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import cc.smartcash.wallet.ViewModels.CurrentPriceViewModel;
import cc.smartcash.wallet.ViewModels.TransactionViewModel;
import cc.smartcash.wallet.ViewModels.UserViewModel;
import cc.smartcash.wallet.ViewModels.WalletViewModel;

public class ViewModelUtil<T> {

    private CurrentPriceViewModel currentPriceViewModel;
    private UserViewModel userViewModel;
    private TransactionViewModel transactionViewModel;
    private WalletViewModel walletViewModel;

    public static <T extends ViewModel> T getViewModel(Fragment owner, Class<T> type) {
        //return new ViewModelProvider(owner).get(type);

        return ViewModelProviders.of(owner).get(type);

    }

    public CurrentPriceViewModel getCurrentPriceViewModel(Fragment owner) {

        if (currentPriceViewModel == null)
            currentPriceViewModel = ViewModelUtil.getViewModel(owner, CurrentPriceViewModel.class);

        return currentPriceViewModel;
    }

    public UserViewModel getUserViewModel(Fragment owner) {

        if (userViewModel == null)
            userViewModel = ViewModelUtil.getViewModel(owner, UserViewModel.class);

        return userViewModel;
    }

    public TransactionViewModel getTransactionViewModel(Fragment owner) {

        if (transactionViewModel == null)
            transactionViewModel = ViewModelUtil.getViewModel(owner, TransactionViewModel.class);

        return transactionViewModel;
    }

    public WalletViewModel getWalletViewModel(Fragment owner) {

        if (walletViewModel == null)
            walletViewModel = ViewModelUtil.getViewModel(owner, WalletViewModel.class);

        return walletViewModel;
    }

}
