package cc.smartcash.wallet.Activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import cc.smartcash.wallet.Adapters.CoinSpinnerAdapter
import cc.smartcash.wallet.Fragments.DashboardFragment
import cc.smartcash.wallet.Fragments.ReceiveFragment
import cc.smartcash.wallet.Fragments.SendFragment
import cc.smartcash.wallet.Fragments.TransactionFragment
import cc.smartcash.wallet.Models.Coin
import cc.smartcash.wallet.R
import cc.smartcash.wallet.Utils.KEYS
import cc.smartcash.wallet.Utils.SmartCashApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var mToolbar: Toolbar? = null
    private var mNavigationView: BottomNavigationView? = null
    private var btnExit: ImageView? = null
    private var btnSettings: ImageView? = null
    private var walletTxt: TextView? = null
    private var walletConverted: TextView? = null
    private var adapter: CoinSpinnerAdapter? = null
    private var smartCashApplication: SmartCashApplication? = null
    private var linearLayoutBkp: LinearLayout? = null
    private var txtPin: String? = null

    private val pin: String?
        get() =
            if (intent.getStringExtra(KEYS.KEY_PIN) != null && intent.getStringExtra(KEYS.KEY_PIN).isNotEmpty()) {
                intent.getStringExtra(KEYS.KEY_PIN)
            } else {
                null
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)
        setContentView(R.layout.activity_main)

        if (smartCashApplication == null)
            smartCashApplication = SmartCashApplication(applicationContext)

        this.txtPin = pin

        setUI()
        setBtnSettingsClick()
        setIfNeedToBackUpTheWallet()
    }

    private fun setIfNeedToBackUpTheWallet() {
        if (smartCashApplication!!.msk != null) {
            linearLayoutBkp!!.visibility = View.VISIBLE
            return
        }
        linearLayoutBkp!!.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        setWalletValue()
    }

    private fun setBtnSettingsClick() {
        btnSettings!!.setOnClickListener {
            val settingsDialog = AlertDialog.Builder(this)
            val settingsView = layoutInflater.inflate(R.layout.settings_modal, null)
            val btnClose = settingsView.findViewById<Button>(R.id.settings_modal_button_close)
            val currentPriceSpinner = settingsView.findViewById<Spinner>(R.id.settings_modal_current_price_spinner)
            val forgotPinBtn = settingsView.findViewById<TextView>(R.id.settings_modal_forgot_pin_btn)
            val createPinBtn = settingsView.findViewById<TextView>(R.id.settings_modal_create_pin)

            if (smartCashApplication?.AppPreferences?.withoutPin!!) forgotPinBtn.visibility = View.GONE else createPinBtn.visibility = View.GONE

            createPinBtn.setOnClickListener {

                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.main_dialog_create_new_ping_title))
                        .setMessage(getString(R.string.main_dialog_create_new_ping_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes) { _, _ -> navigateToLogin() }
                        .setNegativeButton(android.R.string.no, null).show()
            }

            forgotPinBtn.setOnClickListener {
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.main_dialog_forgot_pin_title))
                        .setMessage(getString(R.string.main_dialog_forgot_pin_message))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes) { _, _ -> navigateToLogin() }
                        .setNegativeButton(android.R.string.no, null).show()
            }

            setSpinner(currentPriceSpinner)
            settingsDialog.setView(settingsView)
            val dialog = settingsDialog.create()
            btnClose.setOnClickListener {
                dialog.hide()
                setWalletValue()
            }
            dialog.show()
        }
    }

    private fun setUI() {

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        mNavigationView = findViewById(R.id.navigationView)
        mNavigationView!!.setOnNavigationItemSelectedListener(this)
        val dashboardFragment = DashboardFragment.newInstance()
        openFragment(dashboardFragment)
        walletTxt = findViewById(R.id.toolbar_wallet_txt)
        walletConverted = findViewById(R.id.toolbar_wallet_converted_txt)
        btnExit = findViewById(R.id.toolbar_button_exit)
        btnSettings = findViewById(R.id.toolbar_button_settings)
        setBtnExitClick()
        linearLayoutBkp = findViewById(R.id.bkpwallet)
        setLinearLayoutBkpClick()

    }

    private fun setLinearLayoutBkpClick() {
        linearLayoutBkp!!.setOnClickListener {

            val msk = smartCashApplication!!.getDecryptedMSK(pin!!)

            val alert = AlertDialog.Builder(this)
                    .setTitle(getString(R.string.main_dialog_backup_wallet_title))
                    .setMessage(getString(R.string.main_dialog_backup_wallet_message))

            val frameView = FrameLayout(this)
            alert.setView(frameView)

            val alertDialog = alert.create()
            val inflater = alertDialog.layoutInflater
            val dialogLayout = inflater.inflate(R.layout.wallet_dialog_backup, frameView)

            val txtMSC = dialogLayout.findViewById<EditText>(R.id.txtMSC)
            val lblMSC = dialogLayout.findViewById<TextView>(R.id.wallet_dialog_bkp_labelMSC)
            lblMSC.text = msk

            val btnGoToCheckMSC = dialogLayout.findViewById<Button>(R.id.wallet_dialog_bkp_btnGoToCheckMSC)
            val btnCheckMSC = dialogLayout.findViewById<Button>(R.id.btnCheckMSC)

            btnGoToCheckMSC.setOnClickListener {
                lblMSC.visibility = View.GONE
                btnGoToCheckMSC.visibility = View.GONE

                btnCheckMSC.visibility = View.VISIBLE
                txtMSC.visibility = View.VISIBLE
            }

            btnCheckMSC.setOnClickListener {
                if (msk.trim { it <= ' ' } != txtMSC.text.toString().trim { it <= ' ' })
                    txtMSC.error = getString(R.string.main_dialog_master_security_code_error_message)
                else {
                    val builderDeleteMSK = AlertDialog.Builder(this)
                    builderDeleteMSK.setTitle(getString(R.string.main_dialog_master_security_code_title))
                    builderDeleteMSK.setMessage(getString(R.string.main_dialog_master_security_code_message))

                    builderDeleteMSK.setPositiveButton(getString(R.string.main_dialog_master_security_code_positive_button)) { _, _ ->

                        smartCashApplication!!.deleteMSK()
                        linearLayoutBkp!!.visibility = View.GONE
                        alertDialog.cancel()

                    }
                    builderDeleteMSK.setNegativeButton(getString(R.string.main_dialog_master_security_code_negative_button)) { _, _ ->

                    }

                    val dialogDeleteMSK = builderDeleteMSK.create()
                    dialogDeleteMSK.show()
                }
            }

            txtMSC.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                    if (msk.trim { it <= ' ' } != txtMSC.text.toString().trim { it <= ' ' })
                        txtMSC.error = getString(R.string.main_dialog_master_security_code_error_message)

                }
            })


            alertDialog.show()
        }
    }

    private fun setBtnExitClick() = this.btnExit!!.setOnClickListener {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.main_dialog_logout_title))
                .setMessage(getString(R.string.main_dialog_logout_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes) { _, _ -> navigateToLogin() }
                .setNegativeButton(android.R.string.no, null).show()
    }

    private fun navigateToLogin() {
        Toast.makeText(this@MainActivity, getString(R.string.main_redirect_to_login_toast), Toast.LENGTH_SHORT).show()
        smartCashApplication!!.deleteMSK()
        smartCashApplication!!.deleteSharedPreferences()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
    }

    private fun setSpinner(currentPriceSpinner: Spinner) {

        val coins = smartCashApplication?.AppPreferences?.coins

        this.adapter = CoinSpinnerAdapter(this, android.R.layout.simple_spinner_item, coins!!)

        currentPriceSpinner.adapter = this.adapter

        setSelectedCoinOnSpinner(currentPriceSpinner, coins)

        currentPriceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View,
                                        position: Int, id: Long) =
                    saveSelectedCoin(adapter!!.getItem(position))

            override fun onNothingSelected(adapter: AdapterView<*>) {}
        }
    }

    private fun saveSelectedCoin(coin: Coin?) {

        smartCashApplication!!.saveActualSelectedCoin(this@MainActivity, coin!!)
        setWalletValue()
        reloadCurrentFragment()
    }

    private fun setSelectedCoinOnSpinner(currentPriceSpinner: Spinner, coins: ArrayList<Coin>) {
        val selectedCoin = smartCashApplication!!.AppPreferences.ActualSelectedCoin
        coins.indices.forEach { i ->
            if (selectedCoin.value == coins[i].value && selectedCoin.name == coins[i].name) currentPriceSpinner.setSelection(i)
        }
        setWalletValue()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dash -> {
                val dashFragment = DashboardFragment.newInstance()
                openFragment(dashFragment)
                setWalletValue()
            }
            R.id.nav_receive -> {
                val receiveFragment = ReceiveFragment.newInstance()
                openFragment(receiveFragment)
                setWalletValue()
            }
            R.id.nav_send -> {
                val sendFragment = SendFragment.newInstance()
                openFragment(sendFragment)
                setWalletValue()
            }
            R.id.nav_trans -> {
                val transactionFragment = TransactionFragment.newInstance()
                openFragment(transactionFragment)
                setWalletValue()
            }
        }

        return true
    }

    fun setWalletValue() {

        val user = smartCashApplication?.AppPreferences?.user ?: return

        val wallets = user.wallet ?: return

        val selectedCoin = smartCashApplication?.AppPreferences?.ActualSelectedCoin ?: return

        val coins = smartCashApplication?.AppPreferences?.coins ?: return

        var amount: Double? = 0.0

        for (wallet in wallets) amount = amount?.plus(wallet.balance!!)

        if (selectedCoin.name!!.equals(getString(R.string.default_fiat), ignoreCase = true) && coins.isNotEmpty()) {
            for (auxCoin in coins) {
                if (auxCoin.name!!.equals(selectedCoin.name!!, ignoreCase = true)) {
                    selectedCoin.value = auxCoin.value
                    smartCashApplication!!.saveActualSelectedCoin(this, auxCoin)
                    break
                }
            }
        }

        walletTxt!!.text = smartCashApplication!!.formatNumberByDefaultCrypto(amount!!)

        if (selectedCoin.name == getString(R.string.default_crypto)) {
            val currentPrice = smartCashApplication!!.getCurrentPrice()
            walletConverted!!.text = smartCashApplication!!.formatNumberBySelectedCurrencyCode(smartCashApplication!!.getCurrentValueByRate(amount, currentPrice!![0].value!!))
        } else {
            walletConverted!!.text = smartCashApplication!!.formatNumberBySelectedCurrencyCode(smartCashApplication!!.getCurrentValueByRate(amount, selectedCoin.value!!))
        }

    }

    private fun reloadCurrentFragment() {
        when (supportFragmentManager.findFragmentById(R.id.container)) {
            is DashboardFragment -> openFragment(DashboardFragment.newInstance())
            is ReceiveFragment -> openFragment(ReceiveFragment.newInstance())
            is SendFragment -> openFragment(SendFragment.newInstance())
            is TransactionFragment -> openFragment(TransactionFragment.newInstance())
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        val TAG: String? = MainActivity::class.java.simpleName
    }

    override fun onBackPressed() {
        //Toast.makeText(applicationContext, "Back press disabled!", Toast.LENGTH_SHORT).show()
    }
}