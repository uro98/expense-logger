package com.yujotseng.expenselogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AnalysisGridAdapter extends BaseAdapter {

    private Context context;
    private final String[] analysisNames;
    private final int[] analysisImages;

    public AnalysisGridAdapter(Context context, String[] analysisNames, int[] analysisImage) {
        this.context = context;
        this.analysisNames = analysisNames;
        this.analysisImages = analysisImage;
    }

    @Override
    public int getCount() {
        return analysisNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String analysis = analysisNames[i];
        int analysisImage = analysisImages[i];

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.analysis_grid_layout, null);
        }

        ImageView gridImage = view.findViewById(R.id.gridImage);
        TextView analysisName = view.findViewById(R.id.analysisName);

        gridImage.setImageResource(analysisImage);
        analysisName.setText(analysis);

        return view;
    }
}
