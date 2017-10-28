package me.milechen.qicheng.Fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

public class MainFragment extends Fragment {

    protected OnFragmentInteractionListener mListener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteractionBannerClicked(int position);
    }
}
