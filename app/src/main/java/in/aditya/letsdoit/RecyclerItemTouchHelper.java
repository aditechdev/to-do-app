package in.aditya.letsdoit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import in.aditya.letsdoit.adapter.RVAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RVAdapter rvAdapter;

    public RecyclerItemTouchHelper(RVAdapter rvAdapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.rvAdapter = rvAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }



    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(rvAdapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Do you want to delete the task?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rvAdapter.deleteItem(position);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rvAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            rvAdapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        if (dX>0){
            icon = ContextCompat.getDrawable(rvAdapter.getContext(), R.drawable.ic_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(rvAdapter.getContext(), R.color.design_default_color_primary_dark));
        }else {
            icon = ContextCompat.getDrawable(rvAdapter.getContext(), R.drawable.ic_delete_24);
            background = new ColorDrawable(Color.RED);

        }

        int iconMrgin = (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

//        Swiping to the right
        if (dX>0){
            int iconLeft = itemView.getLeft() + iconMrgin;
            int iconRight = itemView.getLeft() + iconMrgin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());

        }
//        Swiping to the left

        else if (dX<0){
            int iconLeft = itemView.getRight() + iconMrgin;
            int iconRight = itemView.getRight() + iconMrgin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int)dX)- backgroundCornerOffset,itemView.getTop(), itemView.getRight() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());

        }

        else {
            background.setBounds(0,0,0,0);
        }

    background.draw(canvas);
    icon.draw(canvas);
    }

}
