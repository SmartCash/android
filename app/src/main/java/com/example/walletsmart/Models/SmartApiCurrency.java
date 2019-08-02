package com.example.walletsmart.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmartApiCurrency  implements Serializable {

    @JsonProperty("FJD")
    private double fjd;

    @JsonProperty("MXN")
    private double mxn;

    @JsonProperty("STD")
    private double std;

    @JsonProperty("SCR")
    private double scr;

    @JsonProperty("CDF")
    private double cdf;

    @JsonProperty("BBD")
    private double bbd;

    @JsonProperty("GTQ")
    private double gtq;

    @JsonProperty("CLP")
    private double clp;

    @JsonProperty("HNL")
    private double hnl;

    @JsonProperty("UGX")
    private double ugx;

    @JsonProperty("ZAR")
    private double zar;

    @JsonProperty("TND")
    private double tnd;

    @JsonProperty("STN")
    private double stn;

    @JsonProperty("CUC")
    private double cuc;

    @JsonProperty("BSD")
    private double bsd;

    @JsonProperty("SLL")
    private double sll;

    @JsonProperty("SDG")
    private double sdg;

    @JsonProperty("IQD")
    private double iqd;

    @JsonProperty("CUP")
    private double cup;

    @JsonProperty("GMD")
    private double gmd;

    @JsonProperty("TWD")
    private double twd;

    @JsonProperty("RSD")
    private double rsd;

    @JsonProperty("DOP")
    private double dop;

    @JsonProperty("KMF")
    private double kmf;

    @JsonProperty("MYR")
    private double myr;

    @JsonProperty("FKP")
    private double fkp;

    @JsonProperty("XOF")
    private double xof;

    @JsonProperty("GEL")
    private double gel;

    @JsonProperty("BTC")
    private double btc;

    @JsonProperty("UYU")
    private double uyu;

    @JsonProperty("MAD")
    private double mad;

    @JsonProperty("CVE")
    private double cve;

    @JsonProperty("TOP")
    private double top;

    @JsonProperty("AZN")
    private double azn;

    @JsonProperty("OMR")
    private double omr;

    @JsonProperty("PGK")
    private double pgk;

    @JsonProperty("KES")
    private double kes;

    @JsonProperty("SEK")
    private double sek;

    @JsonProperty("CNH")
    private double cnh;

    @JsonProperty("BTN")
    private double btn;

    @JsonProperty("UAH")
    private double uah;

    @JsonProperty("GNF")
    private double gnf;

    @JsonProperty("ERN")
    private double ern;

    @JsonProperty("MZN")
    private double mzn;

    @JsonProperty("SVC")
    private double svc;

    @JsonProperty("ARS")
    private double ars;

    @JsonProperty("QAR")
    private double qar;

    @JsonProperty("IRR")
    private double irr;

    @JsonProperty("MRO")
    private double mro;

    @JsonProperty("XPD")
    private double xpd;

    @JsonProperty("CNY")
    private double cny;

    @JsonProperty("THB")
    private double thb;

    @JsonProperty("UZS")
    private double uzs;

    @JsonProperty("XPF")
    private double xpf;

    @JsonProperty("MRU")
    private double mru;

    @JsonProperty("BDT")
    private double bdt;

    @JsonProperty("LYD")
    private double lyd;

    @JsonProperty("BMD")
    private double bmd;

    @JsonProperty("RUB")
    private double rub;

    @JsonProperty("USD")
    private double usd;

    @JsonProperty("CHF")
    private double chf;

    @JsonProperty("AUD")
    private double aud;

    @JsonProperty("EUR")
    private double eur;

    @JsonProperty("JPY")
    private double jpy;

    @JsonProperty("GBP")
    private double gbp;

    @JsonProperty("NZD")
    private double nzd;

    @JsonProperty("BRL")
    private double brl;

    public double getFjd() {
        return fjd;
    }

    public void setFjd(double fjd) {
        this.fjd = fjd;
    }

    public double getMxn() {
        return mxn;
    }

    public void setMxn(double mxn) {
        this.mxn = mxn;
    }

    public double getStd() {
        return std;
    }

    public void setStd(double std) {
        this.std = std;
    }

    public double getScr() {
        return scr;
    }

    public void setScr(double scr) {
        this.scr = scr;
    }

    public double getCdf() {
        return cdf;
    }

    public void setCdf(double cdf) {
        this.cdf = cdf;
    }

    public double getBbd() {
        return bbd;
    }

    public void setBbd(double bbd) {
        this.bbd = bbd;
    }

    public double getGtq() {
        return gtq;
    }

    public void setGtq(double gtq) {
        this.gtq = gtq;
    }

    public double getClp() {
        return clp;
    }

    public void setClp(double clp) {
        this.clp = clp;
    }

    public double getHnl() {
        return hnl;
    }

    public void setHnl(double hnl) {
        this.hnl = hnl;
    }

    public double getUgx() {
        return ugx;
    }

    public void setUgx(double ugx) {
        this.ugx = ugx;
    }

    public double getZar() {
        return zar;
    }

    public void setZar(double zar) {
        this.zar = zar;
    }

    public double getTnd() {
        return tnd;
    }

    public void setTnd(double tnd) {
        this.tnd = tnd;
    }

    public double getStn() {
        return stn;
    }

    public void setStn(double stn) {
        this.stn = stn;
    }

    public double getCuc() {
        return cuc;
    }

    public void setCuc(double cuc) {
        this.cuc = cuc;
    }

    public double getBsd() {
        return bsd;
    }

    public void setBsd(double bsd) {
        this.bsd = bsd;
    }

    public double getSll() {
        return sll;
    }

    public void setSll(double sll) {
        this.sll = sll;
    }

    public double getSdg() {
        return sdg;
    }

    public void setSdg(double sdg) {
        this.sdg = sdg;
    }

    public double getIqd() {
        return iqd;
    }

    public void setIqd(double iqd) {
        this.iqd = iqd;
    }

    public double getCup() {
        return cup;
    }

    public void setCup(double cup) {
        this.cup = cup;
    }

    public double getGmd() {
        return gmd;
    }

    public void setGmd(double gmd) {
        this.gmd = gmd;
    }

    public double getTwd() {
        return twd;
    }

    public void setTwd(double twd) {
        this.twd = twd;
    }

    public double getRsd() {
        return rsd;
    }

    public void setRsd(double rsd) {
        this.rsd = rsd;
    }

    public double getDop() {
        return dop;
    }

    public void setDop(double dop) {
        this.dop = dop;
    }

    public double getKmf() {
        return kmf;
    }

    public void setKmf(double kmf) {
        this.kmf = kmf;
    }

    public double getMyr() {
        return myr;
    }

    public void setMyr(double myr) {
        this.myr = myr;
    }

    public double getFkp() {
        return fkp;
    }

    public void setFkp(double fkp) {
        this.fkp = fkp;
    }

    public double getXof() {
        return xof;
    }

    public void setXof(double xof) {
        this.xof = xof;
    }

    public double getGel() {
        return gel;
    }

    public void setGel(double gel) {
        this.gel = gel;
    }

    public double getBtc() {
        return btc;
    }

    public void setBtc(double btc) {
        this.btc = btc;
    }

    public double getUyu() {
        return uyu;
    }

    public void setUyu(double uyu) {
        this.uyu = uyu;
    }

    public double getMad() {
        return mad;
    }

    public void setMad(double mad) {
        this.mad = mad;
    }

    public double getCve() {
        return cve;
    }

    public void setCve(double cve) {
        this.cve = cve;
    }

    public double getTop() {
        return top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public double getAzn() {
        return azn;
    }

    public void setAzn(double azn) {
        this.azn = azn;
    }

    public double getOmr() {
        return omr;
    }

    public void setOmr(double omr) {
        this.omr = omr;
    }

    public double getPgk() {
        return pgk;
    }

    public void setPgk(double pgk) {
        this.pgk = pgk;
    }

    public double getKes() {
        return kes;
    }

    public void setKes(double kes) {
        this.kes = kes;
    }

    public double getSek() {
        return sek;
    }

    public void setSek(double sek) {
        this.sek = sek;
    }

    public double getCnh() {
        return cnh;
    }

    public void setCnh(double cnh) {
        this.cnh = cnh;
    }

    public double getBtn() {
        return btn;
    }

    public void setBtn(double btn) {
        this.btn = btn;
    }

    public double getUah() {
        return uah;
    }

    public void setUah(double uah) {
        this.uah = uah;
    }

    public double getGnf() {
        return gnf;
    }

    public void setGnf(double gnf) {
        this.gnf = gnf;
    }

    public double getErn() {
        return ern;
    }

    public void setErn(double ern) {
        this.ern = ern;
    }

    public double getMzn() {
        return mzn;
    }

    public void setMzn(double mzn) {
        this.mzn = mzn;
    }

    public double getSvc() {
        return svc;
    }

    public void setSvc(double svc) {
        this.svc = svc;
    }

    public double getArs() {
        return ars;
    }

    public void setArs(double ars) {
        this.ars = ars;
    }

    public double getQar() {
        return qar;
    }

    public void setQar(double qar) {
        this.qar = qar;
    }

    public double getIrr() {
        return irr;
    }

    public void setIrr(double irr) {
        this.irr = irr;
    }

    public double getMro() {
        return mro;
    }

    public void setMro(double mro) {
        this.mro = mro;
    }

    public double getXpd() {
        return xpd;
    }

    public void setXpd(double xpd) {
        this.xpd = xpd;
    }

    public double getCny() {
        return cny;
    }

    public void setCny(double cny) {
        this.cny = cny;
    }

    public double getThb() {
        return thb;
    }

    public void setThb(double thb) {
        this.thb = thb;
    }

    public double getUzs() {
        return uzs;
    }

    public void setUzs(double uzs) {
        this.uzs = uzs;
    }

    public double getXpf() {
        return xpf;
    }

    public void setXpf(double xpf) {
        this.xpf = xpf;
    }

    public double getMru() {
        return mru;
    }

    public void setMru(double mru) {
        this.mru = mru;
    }

    public double getBdt() {
        return bdt;
    }

    public void setBdt(double bdt) {
        this.bdt = bdt;
    }

    public double getLyd() {
        return lyd;
    }

    public void setLyd(double lyd) {
        this.lyd = lyd;
    }

    public double getBmd() {
        return bmd;
    }

    public void setBmd(double bmd) {
        this.bmd = bmd;
    }

    public double getRub() {
        return rub;
    }

    public void setRub(double rub) {
        this.rub = rub;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getChf() {
        return chf;
    }

    public void setChf(double chf) {
        this.chf = chf;
    }

    public double getAud() {
        return aud;
    }

    public void setAud(double aud) {
        this.aud = aud;
    }

    public double getEur() {
        return eur;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    public double getJpy() {
        return jpy;
    }

    public void setJpy(double jpy) {
        this.jpy = jpy;
    }

    public double getGbp() {
        return gbp;
    }

    public void setGbp(double gbp) {
        this.gbp = gbp;
    }

    public double getNzd() {
        return nzd;
    }

    public void setNzd(double nzd) {
        this.nzd = nzd;
    }

    public double getBrl() {
        return brl;
    }

    public void setBrl(double brl) {
        this.brl = brl;
    }
}
