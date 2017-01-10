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

public class SampleDialogDelete extends DialogFragment {
    public static final String DIALOG_MESSAGE = "Are you sure you want to delete the book?";
    public static final String BOOK_ID = "bookId";

    private String dialogMessage;
    private long bookId;

    // arguments are handled through factory method with bundles for lifecycle maintenance
    public SampleDialogDelete(){

    }

    public static SampleDialogDelete newInstance(String dialogMessage, long id){
        SampleDialogDelete fragment = new SampleDialogDelete();
        Bundle args = new Bundle();
        args.putString(DIALOG_MESSAGE, dialogMessage);
        args.putLong(BOOK_ID,id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dialogMessage = bundle.getString(DIALOG_MESSAGE);
            bookId = bundle.getLong(BOOK_ID);
        }

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete book?");
        builder.setIcon(R.drawable.ic_warning_white_24dp);
        builder.setMessage(dialogMessage)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BookData bookData = new BookData(getActivity());
                        bookData.open();
                        int rowsAffected = bookData.deleteBook(bookId);
                        bookData.close();
                        if (rowsAffected == 1){
                            Toast.makeText(getActivity(), "Book was deleted successfully.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                        }
                        MainActivity callingActivity = (MainActivity) getActivity();
                        callingActivity.carregarVistaPrincipal();
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
