package cc.smartcash.wallet.Models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmartApiCurrency(
        @JsonProperty("FJD")
        var fjd: Double = 0.toDouble(),

        @JsonProperty("MXN")
        var mxn: Double = 0.toDouble(),

        @JsonProperty("STD")
        var std: Double = 0.toDouble(),

        @JsonProperty("SCR")
        var scr: Double = 0.toDouble(),

        @JsonProperty("CDF")
        var cdf: Double = 0.toDouble(),

        @JsonProperty("BBD")
        var bbd: Double = 0.toDouble(),

        @JsonProperty("GTQ")
        var gtq: Double = 0.toDouble(),

        @JsonProperty("CLP")
        var clp: Double = 0.toDouble(),

        @JsonProperty("HNL")
        var hnl: Double = 0.toDouble(),

        @JsonProperty("UGX")
        var ugx: Double = 0.toDouble(),

        @JsonProperty("ZAR")
        var zar: Double = 0.toDouble(),

        @JsonProperty("TND")
        var tnd: Double = 0.toDouble(),

        @JsonProperty("STN")
        var stn: Double = 0.toDouble(),

        @JsonProperty("CUC")
        var cuc: Double = 0.toDouble(),

        @JsonProperty("BSD")
        var bsd: Double = 0.toDouble(),

        @JsonProperty("SLL")
        var sll: Double = 0.toDouble(),

        @JsonProperty("SDG")
        var sdg: Double = 0.toDouble(),

        @JsonProperty("IQD")
        var iqd: Double = 0.toDouble(),

        @JsonProperty("CUP")
        var cup: Double = 0.toDouble(),

        @JsonProperty("GMD")
        var gmd: Double = 0.toDouble(),

        @JsonProperty("TWD")
        var twd: Double = 0.toDouble(),

        @JsonProperty("RSD")
        var rsd: Double = 0.toDouble(),

        @JsonProperty("DOP")
        var dop: Double = 0.toDouble(),

        @JsonProperty("KMF")
        var kmf: Double = 0.toDouble(),

        @JsonProperty("MYR")
        var myr: Double = 0.toDouble(),

        @JsonProperty("FKP")
        var fkp: Double = 0.toDouble(),

        @JsonProperty("XOF")
        var xof: Double = 0.toDouble(),

        @JsonProperty("GEL")
        var gel: Double = 0.toDouble(),

        @JsonProperty("BTC")
        var btc: Double = 0.toDouble(),

        @JsonProperty("UYU")
        var uyu: Double = 0.toDouble(),

        @JsonProperty("MAD")
        var mad: Double = 0.toDouble(),

        @JsonProperty("CVE")
        var cve: Double = 0.toDouble(),

        @JsonProperty("TOP")
        var top: Double = 0.toDouble(),

        @JsonProperty("AZN")
        var azn: Double = 0.toDouble(),

        @JsonProperty("OMR")
        var omr: Double = 0.toDouble(),

        @JsonProperty("PGK")
        var pgk: Double = 0.toDouble(),

        @JsonProperty("KES")
        var kes: Double = 0.toDouble(),

        @JsonProperty("SEK")
        var sek: Double = 0.toDouble(),

        @JsonProperty("CNH")
        var cnh: Double = 0.toDouble(),

        @JsonProperty("BTN")
        var btn: Double = 0.toDouble(),

        @JsonProperty("UAH")
        var uah: Double = 0.toDouble(),

        @JsonProperty("GNF")
        var gnf: Double = 0.toDouble(),

        @JsonProperty("ERN")
        var ern: Double = 0.toDouble(),

        @JsonProperty("MZN")
        var mzn: Double = 0.toDouble(),

        @JsonProperty("SVC")
        var svc: Double = 0.toDouble(),

        @JsonProperty("ARS")
        var ars: Double = 0.toDouble(),

        @JsonProperty("QAR")
        var qar: Double = 0.toDouble(),

        @JsonProperty("IRR")
        var irr: Double = 0.toDouble(),

        @JsonProperty("MRO")
        var mro: Double = 0.toDouble(),

        @JsonProperty("XPD")
        var xpd: Double = 0.toDouble(),

        @JsonProperty("CNY")
        var cny: Double = 0.toDouble(),

        @JsonProperty("THB")
        var thb: Double = 0.toDouble(),

        @JsonProperty("UZS")
        var uzs: Double = 0.toDouble(),

        @JsonProperty("XPF")
        var xpf: Double = 0.toDouble(),

        @JsonProperty("MRU")
        var mru: Double = 0.toDouble(),

        @JsonProperty("BDT")
        var bdt: Double = 0.toDouble(),

        @JsonProperty("LYD")
        var lyd: Double = 0.toDouble(),

        @JsonProperty("BMD")
        var bmd: Double = 0.toDouble(),

        @JsonProperty("RUB")
        var rub: Double = 0.toDouble(),

        @JsonProperty("USD")
        var usd: Double = 0.toDouble(),

        @JsonProperty("CHF")
        var chf: Double = 0.toDouble(),

        @JsonProperty("AUD")
        var aud: Double = 0.toDouble(),

        @JsonProperty("EUR")
        var eur: Double = 0.toDouble(),

        @JsonProperty("JPY")
        var jpy: Double = 0.toDouble(),

        @JsonProperty("GBP")
        var gbp: Double = 0.toDouble(),

        @JsonProperty("NZD")
        var nzd: Double = 0.toDouble(),

        @JsonProperty("BRL")
        var brl: Double = 0.toDouble()

) : Serializable 