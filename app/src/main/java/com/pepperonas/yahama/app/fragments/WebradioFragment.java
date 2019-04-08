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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.pepperonas.yahama.app.MainActivity;
import com.pepperonas.yahama.app.R;
import com.pepperonas.yahama.app.model.AmpYaRxV577;
import com.pepperonas.yahama.app.model.RadioContent;
import com.pepperonas.yahama.app.utility.Commands;
import com.pepperonas.yahama.app.utility.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Pfeffer (pepperonas)
 */
public class WebradioFragment extends Fragment implements RecyclerView.OnItemTouchListener {

    private static final String TAG = "WebradioFragment";
    private ArrayList<RadioContent> mRadioContent;
    private RadioAdapter mAdapter;
    private MainActivity mMain;
    private GestureDetectorCompat mGestureDetector;

    private LinearLayoutManager mLayoutManager;

    private String mTmpInfo = "";

    public static WebradioFragment newInstance(int i) {
        WebradioFragment fragment = new WebradioFragment();

        Bundle args = new Bundle();
        args.putInt("the_id", i);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_webradio, container, false);
        mMain = (MainActivity) getActivity();
        mMain.setTitle(getString(R.string.webradio));
        mMain.runCtrlrTask(false, Commands.SELECT_INPUT(AmpYaRxV577.INPUT_NET_RADIO));

        mGestureDetector = new GestureDetectorCompat(getActivity(), new RecyclerViewOnGestureListener(this));

        mRadioContent = new ArrayList<RadioContent>();

        RecyclerView recyclerView = v.findViewById(R.id.radio_recycler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 0) {

                    int x = mLayoutManager.findFirstVisibleItemPosition();
                    mMain.runCtrlrTask(false, Commands.JUMP_LINE(x));
                }

            }
        });

        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RadioAdapter((MainActivity) getActivity(), this, mRadioContent);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    public void mrProper() {
        mRadioContent.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "created()");
    }

    public void onOverscrollDetected(int direction) {
        switch (direction) {

            case Const.OVERSCROLL_TOP:
                break;

            case Const.OVERSCROLL_RIGHT:
                mMain.runCtrlrTask(false, Commands.LIST_DIRECTION("NET_RADIO", AmpYaRxV577.MC_RIGHT));
                break;

            case Const.OVERSCROLL_BOTTOM:
                break;

            case Const.OVERSCROLL_LEFT:
                mMain.runCtrlrTask(false, Commands.LIST_RETURN("NET_RADIO"));
                mrProper();
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private List<RadioContent> generateEmptyCards(int amount) {
        ArrayList<RadioContent> emptyList = new ArrayList<RadioContent>();
        for (int i = 0; i < amount; i++) {
            emptyList.add(new RadioContent(getString(R.string.no_radio_data)));
        }
        return emptyList;
    }

    public void initRadioList(int maxLine, List<String> content, String info) {
        mTmpInfo = info;

        mRadioContent.clear();
        mRadioContent.addAll(generateEmptyCards(maxLine));
        int i = 0;
        for (String s : content) {
            Log.d(TAG, "initRadioList  " + i + ": " + s);
            int pos = mLayoutManager.findFirstVisibleItemPosition() + i;

            if (pos <= 0) pos = 0;

            if (mRadioContent.size() > pos) {
                mRadioContent.set(pos, new RadioContent(s));
            } else {
                mRadioContent.add(new RadioContent(s));
            }

            i++;
        }
        mAdapter.notifyDataSetChanged();
    }

    public String getTmpInfo() {
        return mTmpInfo;
    }
}