/*
 * Copyright (c) 2019 Martin Pfeffer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pepperonas.yahama.app.fragments;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pepperonas.yahama.app.BuildConfig;
import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.model.RadioContent;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;

import java.util.ArrayList;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.ViewHolder> {

    private static final String TAG = "RadioAdapter";

    private MainActivity mMain;
    private WebradioFragment mWrf;
    private ArrayList<RadioContent> mRadioContent;

    RadioAdapter(MainActivity main, WebradioFragment wrf, ArrayList<RadioContent> radioContent) {
        mMain = main;
        mWrf = wrf;
        mRadioContent = radioContent;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCv;
        ImageView mIv;
        TextView mTv;

        ViewHolder(View v) {
            super(v);
            mCv = v.findViewById(R.id.cv_radio_item);
            mIv = v.findViewById(R.id.iv_radio_item);
            mTv = v.findViewById(R.id.tv_radio_item);
        }
    }

    private void invokeAction(int pos, String name) {
        Log.d(TAG, "invokeAction  " + "" + Commands.LIST_CLICK("NET_RADIO", (pos + 1)));

        String xmlInfo = mWrf.getTmpInfo();

        int xmlPos = BuildConfig.DEBUG ? -1 : 1;

        if (xmlInfo.contains(name)) {
            String target = xmlInfo.split(name)[0].replace("><Txt>", "");

            xmlPos = Character.getNumericValue(target.charAt((target.length() - 1)));
            Log.w(TAG, "invokeAction " + target + "  |   -> " + xmlPos);

        }

        mMain.runCtrlrTask(false, Commands.LIST_CLICK("NET_RADIO", xmlPos), Const.LONGCLICKED_STATION);
    }

    @Override
    public RadioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String name = mRadioContent.get(position).getName();

        Drawable d = mRadioContent.get(position).getIcon();
        if (d != null) holder.mIv.setImageDrawable(d);

        holder.mCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invokeAction(position, name);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWrf.mrProper();
                    }
                }, 400);

            }
        });

        holder.mTv.setText(name);

        if (name.equals(mMain.getString(R.string.no_radio_data))) {
            holder.mCv.setClickable(false);
        }

    }

    @Override
    public int getItemCount() {
        return mRadioContent.size();
    }

}
