package info.mayuragarkar.calculator;

/**
 * Created by agark on 12/30/2015.
 */
public enum CustomPagerEnum {

    BASIC_CALC(R.string.basic_func, R.layout.view_basic_calculations),
    ADVANCED_CALC(R.string.advanced_func, R.layout.view_adv_calculations),
    //ADVANCED_CALC_1(R.string.advanced_func, R.layout.view_adv_calculations_1),
    ADVANCED_CALC_2(R.string.advanced_func, R.layout.view_adv_calculations_2),
    ADVANCED_CALC_3(R.string.advanced_func, R.layout.view_adv_calculations_3);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}