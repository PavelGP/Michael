package by.of.servicebook.myapplication.fragments.dialogs;

import android.app.DialogFragment;

/**
 * Created by s.ankuda on 5/4/2015.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected BaseNoticeDialogListener mListener;

    public interface BaseNoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    public interface NoticeDialogListener extends  BaseNoticeDialogListener{
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    public interface FullNoticeDialogListener extends NoticeDialogListener{
        public void onDialogNeutralClick(DialogFragment dialog);
    }

    public void setListener(BaseNoticeDialogListener mListener) {
        this.mListener = mListener;
    }

    public BaseNoticeDialogListener getListener() {
        return mListener;
    }
}

