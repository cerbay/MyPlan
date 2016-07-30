package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import percept.myplan.AppController;
import percept.myplan.POJO.Contact;
import percept.myplan.POJO.ContactDisplay;
import percept.myplan.R;
import percept.myplan.customviews.RoundedImageView;

/**
 * Created by percept on 16/7/16.
 */

public class ContactHelpListAdapter extends RecyclerView.Adapter<ContactHelpListAdapter.ContactHelpListHolder> {


    public List<ContactDisplay> LIST_HELPCONTACT;
    ImageLoader imageLoader;

    public class ContactHelpListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView TV_HELPCONTACT;
        public RoundedImageView IMG_CONTACT;

        public ContactHelpListHolder(View itemView) {
            super(itemView);
            TV_HELPCONTACT = (TextView) itemView.findViewById(R.id.tvContactName);
            IMG_CONTACT = (RoundedImageView) itemView.findViewById(R.id.imgContact);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public ContactHelpListAdapter(List<ContactDisplay> helpContactList) {
        this.LIST_HELPCONTACT = helpContactList;
        imageLoader = AppController.getInstance().getImageLoader();
    }


    @Override
    public ContactHelpListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.helpcontact_list_item, parent, false);
        return new ContactHelpListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactHelpListHolder holder, int position) {
        ContactDisplay _contact = LIST_HELPCONTACT.get(position);
        holder.TV_HELPCONTACT.setText(_contact.getFirst_name());
//        holder.IMG_CONTACT.setImageBitmap();
        imageLoader.get(_contact.getCon_image(), new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    holder.IMG_CONTACT.setImageBitmap(response.getBitmap());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.LIST_HELPCONTACT.size();
    }
}
