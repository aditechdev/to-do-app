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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.aditya.letsdoit.DialogCloseListener;
import in.aditya.letsdoit.R;
import in.aditya.letsdoit.model.RVModel;
import in.aditya.letsdoit.utils.DataBaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionButtonDialog";
    private EditText newTask;
    private Button saveButton;
    private DataBaseHandler dataBaseHandler;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_entry, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newTask = getView().findViewById(R.id.et_new_task);
        saveButton = getView().findViewById(R.id.btn_new_task);

        dataBaseHandler = new DataBaseHandler(getActivity());
        dataBaseHandler.openDataBase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTask.setText(task);
            if (task.length()>0)
                saveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.design_default_color_primary_dark));
        }
        newTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                }else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.design_default_color_primary_dark));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTask.getText().toString();
                if (finalIsUpdate){
                    dataBaseHandler.updateTask(bundle.getInt("id"), text);
                }else {
                    RVModel task = new RVModel();
                    task.setTask(text);
                    task.setStatus(0);
                }

                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(DialogInterface dialogInterface){
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialogInterface);
        }
    }
}
