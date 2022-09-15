package com.rpcb.rubberpavementcalcbot.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity(name = "pavcalbot")
public class User {

    @Id
    private Long chatId;

    private String userName;

    private Timestamp timeregstered;

    private Timestamp timeLastUse;

    private double consumption_binder_top;

    private double consumptionBinderLower;

    private double consumptionBlackRubber;

    private double consumptionEPDM;

    private double consumptionSolvent;

    private double priceBinder;

    private double priceBlackRubber;

    private double priceColorRubber;

    private double priceEPDM;

    private double pricePrimer;

    private double priceSolvent;

    private double priceBinderTerraway;

    private double priceStone;

    private double pricePastColor;

    private double priceColorBinder;

    private double priceEPDMlittle;

}
