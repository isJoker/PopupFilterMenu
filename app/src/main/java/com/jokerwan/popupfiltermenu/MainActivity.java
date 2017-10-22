package com.jokerwan.popupfiltermenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jokerwan.popupfiltermenu.popupmenu.PopupFilterMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private PopupFilterMenu popupFilterMenu;
    private String headers[] = {"品牌", "全城", "离我最近", "特色"};
    private List<View> popupViews = new ArrayList<>();

    private GirdAdapter cityAdapter;
    private ListDistanseAdapter diatanseAdapter;
    private FeatureAdapter featureAdapter;

    private String brand[] = {"全部", "星美国际影城", "大地影院", "万达影城", "保利国际影城", "金字塔国际影城", "全球影城", "华夏影城", "比高电影院", "星星国际影城", "其他"};
    public static final String[] areas = {"全部", "朝阳区", "海淀区", "丰台区", "大兴区", "东城区", "西城区", "通州区", "昌平区", "房山区", "义顺区", "石景山区", "门头沟", "怀柔区", "平谷区", "延庆区", "密云区"};
    public static final String[] area_subways = {"全部", "地铁1号线", "地铁2号线", "地铁3号线", "地铁4号线", "地铁5号线", "地铁6号线", "地铁7号线", "地铁8号线", "地铁9号线","地铁10号线","地铁11号线","地铁12号线","地铁13号线","地铁14号线"};
    private String diatanse[] = {"离我最近", "价格最低", "好评优先"};
    private String feature[] = {"全部", "IMAX厅", "中国巨幕厅", "4D厅", "4K厅", "4DX厅", "RealD厅", "巨幕厅", "3D厅"};


    private int featurePosition = 0;

    private LinearLayout llBusiness;
    private TextView tvBusiness;
    private View viewBusiness;
    private LinearLayout llSubway;
    private TextView tvSubway;
    private View viewSubway;
    private ListView lvBusiness;
    private ListView lvShowData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        popupFilterMenu = (PopupFilterMenu) findViewById(R.id.popup_menu);

        final ListView brandView = new ListView(this);
        cityAdapter = new GirdAdapter(this, Arrays.asList(brand));
        brandView.setDividerHeight(0);
        brandView.setAdapter(cityAdapter);

        View areaView = View.inflate(this,R.layout.item_adress,null);
        llBusiness = areaView.findViewById(R.id.ll_business);
        tvBusiness = areaView.findViewById(R.id.tv_business);
        viewBusiness = areaView.findViewById(R.id.view_business);
        llSubway = areaView.findViewById(R.id.ll_subway);
        tvSubway = areaView.findViewById(R.id.tv_subway);
        viewSubway = areaView.findViewById(R.id.view_subway);
        lvBusiness = areaView.findViewById(R.id.lv_business);
        lvShowData = areaView.findViewById(R.id.lv_show_data);

        tvBusiness.setTextColor(0xffff0000);
        viewBusiness.setVisibility(View.VISIBLE);
        tvSubway.setTextColor(0xff000000);
        viewSubway.setVisibility(View.INVISIBLE);

        final ListAddrAdapter listAdapter = new ListAddrAdapter(this, areas);
        lvBusiness.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvBusiness.setAdapter(listAdapter);
        lvShowData.setAdapter(new ListDataAdapter(this, area_subways));
        lvBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.changeSelected(position);
                listAdapter.notifyDataSetChanged();

            }
        });

        llBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBusiness.setTextColor(0xffff0000);
                viewBusiness.setVisibility(View.VISIBLE);
                tvSubway.setTextColor(0xff000000);
                viewSubway.setVisibility(View.INVISIBLE);

                listAdapter.refreshData(areas);
            }
        });

        llSubway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBusiness.setTextColor(0xff000000);
                viewBusiness.setVisibility(View.INVISIBLE);
                tvSubway.setTextColor(0xffff0000);
                viewSubway.setVisibility(View.VISIBLE);

                listAdapter.refreshData(area_subways);
            }
        });



        final ListView diatanseView = new ListView(this);
        diatanseView.setDividerHeight(0);
        diatanseAdapter = new ListDistanseAdapter(this, Arrays.asList(diatanse));
        diatanseView.setAdapter(diatanseAdapter);

        final View featureView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        GridView freture = featureView.findViewById(R.id.constellation);
        featureAdapter = new FeatureAdapter(this, Arrays.asList(feature));
        freture.setAdapter(featureAdapter);
        TextView ok = featureView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFilterMenu.setTabText(featurePosition == 0 ? headers[3] : feature[featurePosition]);
                popupFilterMenu.closeMenu();
            }
        });
        TextView reset = featureView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                featurePosition = 0;
                popupFilterMenu.setTabText(feature[0]);
                featureAdapter.setCheckItem(0);
                popupFilterMenu.closeMenu();
            }
        });

        //init popupViews
        popupViews.add(brandView);
        popupViews.add(areaView);
        popupViews.add(diatanseView);
        popupViews.add(featureView);

        //add item click event
        brandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                popupFilterMenu.setTabText(position == 0 ? headers[0] : brand[position]);
                popupFilterMenu.closeMenu();
            }
        });

        lvShowData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupFilterMenu.setTabText(position == 0 ? headers[1] : area_subways[position]);
                popupFilterMenu.closeMenu();
            }
        });

        diatanseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                diatanseAdapter.setCheckItem(position);
                popupFilterMenu.setTabText(position == 0 ? headers[2] : diatanse[position]);
                popupFilterMenu.closeMenu();
            }
        });

        freture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                featureAdapter.setCheckItem(position);
                featurePosition = position;
            }
        });

        //init context areaView
        ImageView contentView = new ImageView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setImageResource(R.mipmap.content);
        contentView.setScaleType(ImageView.ScaleType.FIT_XY);

        //init PopupMenu
        popupFilterMenu.initPopupMenu(Arrays.asList(headers), popupViews, contentView);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (popupFilterMenu.isShowing()) {
            popupFilterMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
