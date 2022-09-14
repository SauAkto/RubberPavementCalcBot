package com.rpcb.rubberpavementcalcbot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Setter
@Getter
@Entity(name = "user_tel_bot")
public class User {

    @Id
    private Long chatId;

    @Column(name="user_name")
    private String userName;

    @Column(name="timeregstered")
    private Timestamp timeregstered;

    @Column(name="timelastuse")
    private Timestamp timeLastUse;

    @Column(name="consumption_binder_top")
    private double consumption_binder_top;

    @Column(name="consumption_binder_lower")
    private double consumptionBinderLower;

    @Column(name="consumption_black_rubber")
    private double consumptionBlackRubber;

    @Column(name="consumption_epdm")
    private double consumptionEPDM;

    @Column(name="consumption-solvent")
    private double consumptionSolvent;

    @Column(name="price-binder")
    private double priceBinder;

    @Column(name="price-black-rubber")
    private double priceBlackRubber;

    @Column(name="price_color_rubber")
    private double priceColorRubber;

    @Column(name="price_epdm")
    private double priceEPDM;

    @Column(name="price_primer")
    private double pricePrimer;

    @Column(name="price_solvent")
    private double priceSolvent;

    @Column(name="price_binder_terraway")
    private double priceBinderTerraway;

    @Column(name="price_stone")
    private double priceStone;

    @Column(name="price_past_color")
    private double pricePastColor;

    @Column(name="price_color_binder")
    private double priceColorBinder;

    @Column(name="price_epdm_little")
    private double priceEPDMlittle;

}
