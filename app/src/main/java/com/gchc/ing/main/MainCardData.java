package com.gchc.ing.main;

import android.graphics.Color;

import com.gchc.ing.R;

/**
 * Created by Lincoln on 18/05/16.
 */
public class MainCardData {
    private CardE cardE;
    private int value;
    private int maxValue;
    private int cardColor;
    private int cardImage;
    private boolean isVisible;

    public MainCardData(CardE cardE) {
        setCardE(cardE);
        settingValue();
    }

    private void settingValue() {
        if (cardE == CardE.ADD) {
            setValue(100);
        }else if (cardE == CardE.SUGAR) {
            setValue(99);
            setMaxValue(100);
            setCardColor(Color.parseColor("#F9C4DF"));
            setCardImage(R.drawable.icon_main_sugar);
        }  else if (cardE == CardE.WEIGHT) {
            setValue(0);
            setMaxValue(100);
            setCardColor(Color.parseColor("#E9D0B5"));
            setCardImage(R.drawable.icon_main_weight);
        } else if (cardE == CardE.FOOD) {
            setValue(0);
            setMaxValue(100);
            setCardColor(Color.parseColor("#7BC060"));
            setCardImage(R.drawable.icon_main_food);
        }
    }

    public enum CardE {
        ADD("추가")    // ADD(mContext.getString(R.string.text_add));
        , SUGAR("혈당")   // SUGAR(mContext.getString(R.string.text_sugar))
        , WEIGHT("체중")  // WEIGHT(mContext.getString(R.string.text_weight))
        , FOOD("식사");   // FOOD(mContext.getString(R.string.text_food))


        private String mCardName;
        CardE(String cardName) {
                mCardName = cardName;
        }

        public String getCardName() {
            return mCardName;
        }

    }

    public CardE getCardE() {
        return cardE;
    }

    public void setCardE(CardE cardE) {
        this.cardE = cardE;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    public int getCardImage() {
        return cardImage;
    }

    public void setCardImage(int cardImage) {
        this.cardImage = cardImage;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
