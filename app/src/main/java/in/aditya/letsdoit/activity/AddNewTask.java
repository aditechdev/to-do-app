package in.aditya.letsdoit.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.aditya.letsdoit.OnDialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {

    // TODO 1: REMOVE WARNING


    //    -----------------------------------------------------TAG DECLARATION    ------------------------------------------------------------ //

    public static final String TAG = "AddNewTask";

    //    ---------------------------------------------------VARIABLE DECLARATION   ------------------------------------------------------------ //


    private EditText mEditText;
    private Button mSaveButton;
    private DataBaseHandler myDb;

    //      CONSTRUCTOR

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_entry, container, false);
        return v;
    }

    //    ------------------------------------------------- ON VIEW CREATED METHOD     ------------------------------------------------------- //

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.et_new_task);
        mSaveButton = view.findViewById(R.id.btn_new_task);
        myDb = new DataBaseHandler(getActivity());

        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            //      DISABLE BUTTON

            if (task.length() > 0) {
                mSaveButton.setEnabled(false);
            }
        }

        //      TO CHECK TEXT CHANGE

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.GRAY);
                } else {
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();

                //   TO UPDATE THE TASK

                if (finalIsUpdate) {
                    myDb.updateTask(bundle.getInt("id"), text);
                }

                //      TO CREATE NEW TASK
                else {
                    RVModel item = new RVModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDb.insertTask(item);
                }
                dismiss();
            }
        });
    }

    //    ------------------------------------------------ ON DISMISS ACTIVITY   ------------------------------------------ //

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();

        if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }
}
