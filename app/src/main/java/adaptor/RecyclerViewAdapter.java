package adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contacts.MainActivity;
import com.example.contacts.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.widget.Filter;
import android.widget.Filterable;

import Model.Contact;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {
    private Context context;
    private List<Contact> contactList;
    private List<Contact> filteredContactlist;

    public RecyclerViewAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.filteredContactlist = contactList;
    }

   //Where to get a single card as viewHolder object
    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder((view));
    }
//What will happen when we  get a view holder object.
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.contactname.setText(contact.getName());
        holder.phonenumber.setText(contact.getPhoneNumber());

        Bitmap ImageBitmap= BitmapFactory.decodeByteArray(contact.getImage(), 0, contact.getImage().length);
        holder.iconbutton.setImageBitmap(ImageBitmap);



    }
    //How many items.
    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filteredContactlist);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact item : filteredContactlist) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            contactList.clear();
            contactList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

            //VIEW HOLDER CLASS
        class ViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{
        public TextView contactname;
        public TextView phonenumber;
        public ImageView iconbutton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            contactname = itemView.findViewById(R.id.name);
            phonenumber = itemView.findViewById(R.id.phone_number);

            iconbutton = itemView.findViewById(R.id.imageView);
            iconbutton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Contact contact = contactList.get(position);
            String name = contact.getName();
            String number = contact.getPhoneNumber();


            Toast toast = Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(context, displayContacts.class);

            intent.putExtra("RName", name);
            intent.putExtra("RNumber",number);
            intent.putExtra("RId",contact.getId());
            intent.putExtra("RImage",contact.getImage());
            context.startActivity(intent);

        }
    }
}
