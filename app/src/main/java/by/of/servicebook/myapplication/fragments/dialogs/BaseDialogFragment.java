package by.of.servicebook.myapplication.fragments.dialogs;

import android.app.DialogFragment;

/**
 * Created by s.ankuda on 5/4/2015.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected NoticeDialogListener mListener;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public interface CustomNoticeDialogListener extends NoticeDialogListener{
        public void onDialogNeutralClick(DialogFragment dialog);
    }

    public void setmListener(NoticeDialogListener mListener) {
        this.mListener = mListener;
    }
}

