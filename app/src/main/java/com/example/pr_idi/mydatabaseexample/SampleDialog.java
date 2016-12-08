package com.example.pr_idi.mydatabaseexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Andreu on 08/12/2016.
 */

public class SampleDialog extends DialogFragment {
    public static final String DIALOG_MESSAGE = "dialogMessage";
    public static final String EVALUATION_TEXT = "defaultEvaluation";
    public static final String BOOK_ID = "bookId";

    private String dialogMessage;
    private String evaluationText;
    private long bookId;

    // arguments are handled through factory method with bundles for lifecycle maintenance
    public SampleDialog(){

    }

    public static SampleDialog newInstance(String dialogMessage, String text, long id){
        SampleDialog fragment = new SampleDialog();
        Bundle args = new Bundle();
        args.putString(DIALOG_MESSAGE, dialogMessage);
        args.putString(EVALUATION_TEXT,text);
        args.putLong(BOOK_ID,id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dialogMessage = bundle.getString(DIALOG_MESSAGE);
            evaluationText = bundle.getString(EVALUATION_TEXT);
            bookId = bundle.getLong(BOOK_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMessage)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BookData bookData = new BookData(getActivity());
                        bookData.open();
                        bookData.updateBookEvaluation(bookId,evaluationText);
                        bookData.close();
                        Toast.makeText(getActivity(), dialogMessage + " " + evaluationText + " " + bookId, Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
