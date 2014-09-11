package com.entboost.im.function;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.entboost.im.R;
import com.entboost.im.base.EbFragment;

public class FunctionListFragment extends EbFragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = onCreateEbView(R.layout.fragment_function, inflater,
				container);
		return view;
	}

	@Override
	public void refreshPage() {
		super.refreshPage();
	}
	
}
