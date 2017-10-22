package com.jokerwan.popupfiltermenu.popupmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jokerwan.popupfiltermenu.R;

import java.util.List;

/**
 * Created by ${JokerWan} on 2017/10/20.
 * WeChat: wjc398556712
 * Function: 仿猫眼电影多条件筛选菜单
 */

public class PopupFilterMenu extends LinearLayout {

    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线颜色
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int textSelectedColor = 0xffff0000;
    //tab未选中颜色
    private int textUnselectedColor = 0xff000000;
    //遮罩颜色
    private int maskColor = 0x88888888;
    //tab字体大小
    private int menuTextSize = 14;

    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;
    //菜单高度百分比
    private float menuHeighPercent = 0.5f;
    private DisplayMetrics metrics;

    public PopupFilterMenu(Context context) {
        this(context,null);
    }

    public PopupFilterMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PopupFilterMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context,AttributeSet attrs) {
        setOrientation(VERTICAL);

        //自定义属性
        int menuBackgroundColor = 0xffffffff;
        int underlineColor = 0xffcccccc;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PopupFilterMenu);
        underlineColor = a.getColor(R.styleable.PopupFilterMenu_underlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PopupFilterMenu_dividerColor, dividerColor);
        textSelectedColor = a.getColor(R.styleable.PopupFilterMenu_textSelectedColor, textSelectedColor);
        textUnselectedColor = a.getColor(R.styleable.PopupFilterMenu_textUnselectedColor, textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.PopupFilterMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.PopupFilterMenu_maskColor, maskColor);
        menuTextSize = a.getDimensionPixelSize(R.styleable.PopupFilterMenu_menuTextSize, menuTextSize);
        menuSelectedIcon = a.getResourceId(R.styleable.PopupFilterMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnselectedIcon = a.getResourceId(R.styleable.PopupFilterMenu_menuUnselectedIcon, menuUnselectedIcon);
        menuHeighPercent = a.getFloat(R.styleable.PopupFilterMenu_menuMenuHeightPercent,menuHeighPercent);
        a.recycle();

        //初始化tabMenuView并添加到PopupMune中
        tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView,0);

        //添加下划线到PopupMune中
        View line = new View(context);
        line.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp2Px(1.0f)));
        line.setBackgroundColor(underlineColor);
        addView(line,1);

        //初始化containerView并将其添加到PopupMune中
        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(containerView,2);

    }

    /**
     * 初始化PopupMenu
     * @param tabTexts
     * @param popupViews
     * @param contentView
     */
    public void initPopupMenu(@Nullable List<String> tabTexts,@Nullable List<View> popupViews,@Nullable View contentView){

        if(tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("tabTexts.size() should must equals popupViews.size()");
        }

        for (int i = 0;i < tabTexts.size();i++){
            addTabs(tabTexts,i);
        }

        containerView.addView(contentView,0);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        containerView.addView(maskView,1);
        maskView.setVisibility(GONE);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getScreenHeightPx(getContext()) * menuHeighPercent)));
        popupMenuViews.setVisibility(GONE);
        containerView.addView(popupMenuViews,2);

        for (int i = 0;i < popupViews.size();i++){
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i),i);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if(current_tab_position != -1) {
            TextView tabText = (TextView) tabMenuView.getChildAt(current_tab_position);
            tabText.setTextColor(textUnselectedColor);
            tabText.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(menuUnselectedIcon),null);
            popupMenuViews.setVisibility(GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_slide_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.mask_out));
            current_tab_position = -1;
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setText(text);
        }
    }

    /**
     * PopupMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 添加标题和分割线
     * @param tabTexts
     * @param i
     */
    private void addTabs(List<String> tabTexts, int i) {
        final TextView tab = new TextView(getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX,menuTextSize);
        tab.setGravity(Gravity.CENTER);
        tab.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        tab.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(menuUnselectedIcon),null);
        tab.setText(tabTexts.get(i));
        tab.setPadding(dp2Px(5),dp2Px(12),dp2Px(5),dp2Px(12));
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenu(tab);
            }
        });
        tabMenuView.addView(tab);

        //添加分割线
        if(i < tabTexts.size() - 1) {
            View divideLine = new View(getContext());
            divideLine.setLayoutParams(new LayoutParams(dp2Px(0.5f),LayoutParams.MATCH_PARENT));
            divideLine.setBackgroundColor(dividerColor);
            tabMenuView.addView(divideLine);
        }
    }

    private void switchMenu(TextView willSelectTab) {
        for (int i = 0;i < tabMenuView.getChildCount();i += 2){
            if(willSelectTab == tabMenuView.getChildAt(i)) {//选中的tab
                if(current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {//没有选中任何tab时有动画
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.popup_slide_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));

                    }
                    current_tab_position = i;
                    TextView tabText = (TextView) tabMenuView.getChildAt(i);
                    tabText.setTextColor(textSelectedColor);
                    tabText.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(menuSelectedIcon),null);
                    popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);//选中tab对应的pop可见
                }
            } else {//未选中的tab
                TextView tabText = (TextView) tabMenuView.getChildAt(i);
                tabText.setTextColor(textUnselectedColor);
                tabText.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(menuUnselectedIcon),null);
                popupMenuViews.getChildAt(i / 2).setVisibility(GONE);
            }
        }
    }


    public int dp2Px(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    public int getScreenHeightPx(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
}
