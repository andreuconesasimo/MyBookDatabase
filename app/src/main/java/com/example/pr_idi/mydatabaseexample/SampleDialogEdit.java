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

public class SampleDialogEdit extends DialogFragment {
    public static final String DIALOG_MESSAGE = "Are you sure you want to modify your book's evaluation?";
    public static final String EVALUATION_TEXT = "regular";
    public static final String BOOK_ID = "bookId";

    private String dialogMessage;
    private String evaluationText;
    private long bookId;

    public SampleDialogEdit(){

    }

    public static SampleDialogEdit newInstance(String dialogMessage, String text, long id){
        SampleDialogEdit fragment = new SampleDialogEdit();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change book's evaluation?");
        builder.setIcon(R.drawable.ic_warning_white_24dp);
        builder.setMessage(dialogMessage)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BookData bookData = new BookData(getActivity());
                        bookData.open();
                        int rowsAffected = bookData.updateBookEvaluation(bookId,evaluationText);
                        bookData.close();
                        if (rowsAffected == 1){
                            Toast toast = Toast.makeText(getActivity(), "Book's evaluation modified!", Toast.LENGTH_SHORT);
                            toast.getView().setBackgroundResource(R.color.colorAccent);
                            toast.getView().setPadding(10,10,10,10);
                            toast.show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
}
