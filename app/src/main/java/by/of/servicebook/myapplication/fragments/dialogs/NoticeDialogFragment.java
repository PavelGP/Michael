package by.of.servicebook.myapplication.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by s.ankuda on 5/4/2015.
 */
public class NoticeDialogFragment extends BaseDialogFragment {

    public static final String TAG = "NoticeDialogFragment.TAG";

    public static final String MESSAGE_TITLE = "MESSAGE_TITLE";
    public static final String MESSAGE_CONTENT = "MESSAGE_CONTENT";


    public static DialogFragment newInstance(String messageTitle, String messageContent,  NoticeDialogListener noticeDialogListener) {
        BaseDialogFragment noticeDialogFragment = new NoticeDialogFragment();
        noticeDialogFragment.setmListener(noticeDialogListener);
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(MESSAGE_TITLE, messageTitle);
        args.putString(MESSAGE_CONTENT, messageContent);
        noticeDialogFragment.setArguments(args);

        return noticeDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String messageContent = (getArguments().getString(MESSAGE_CONTENT)== null)? "" : getArguments().getString(MESSAGE_CONTENT) ;
        String messageTitle = getArguments().getString(MESSAGE_TITLE);


        messageTitle = (messageTitle == null) ? "" : messageTitle;

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(messageTitle);
        builder.setMessage(messageContent)
                .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) mListener.onDialogPositiveClick(NoticeDialogFragment.this);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) mListener.onDialogNegativeClick(NoticeDialogFragment.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
