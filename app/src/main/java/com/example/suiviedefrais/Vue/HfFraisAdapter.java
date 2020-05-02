package com.example.suiviedefrais.Vue;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.suiviedefrais.Controleur.Control;
import com.example.suiviedefrais.Model.FraisHf;
import com.example.suiviedefrais.Model.FraisMois;
import com.example.suiviedefrais.R;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HfFraisAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private Control control;
    private Context context;
    private final List<FraisHf> lesFrais;
    private final int key;


    public HfFraisAdapter(Context context, List<FraisHf> lesFrais, int key){
        this.context = context;
        this.control = Control.getInstance(null);
        this.inflater = LayoutInflater.from(context);
        this.lesFrais = lesFrais;
        this.key = key;
    }

    @Override
    public int getCount() {
        return lesFrais.size();
    }

    @Override
    public Object getItem(int index) {
        return lesFrais.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    /**
     * Affichage dans la liste
     */
    @Override
    public View getView(final int index, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_liste, parent, false);
            holder.setTxtListJour((TextView) convertView.findViewById(R.id.txtListJour));
            holder.setTxtListMontant((TextView) convertView.findViewById(R.id.txtListMontant));
            holder.setTxtListMotif((TextView) convertView.findViewById(R.id.txtListMotif));
            holder.setBtnSuppr((ImageButton) convertView.findViewById(R.id.cmdSuppHf));
            holder.getBtnSuppr().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FraisMois frais = control.getData(key);
                    List<FraisHf> fraisHfData = Objects.requireNonNull(control.getData(key)).getLesFraisHf();
                    fraisHfData.remove(index);
                    control.insertDataIntoFraisF(key, frais);
                    // on mes a jours la liste
                    notifyDataSetChanged();
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // initialisation des valeurs de la ligne
        holder.getTxtListJour().setText(String.format(Locale.FRANCE, "%d", lesFrais.get(index).getJour()));
        holder.getTxtListMontant().setText(String.format(Locale.FRANCE, "%.2f", lesFrais.get(index).getMontant()));
        holder.getTxtListMotif().setText(lesFrais.get(index).getMotif());
        return convertView;
    }


    private static class ViewHolder {

        private TextView txtListJour;
        private TextView txtListMontant;
        private TextView txtListMotif;
        private ImageButton cmdSuppHf;

        public TextView getTxtListJour() {
            return txtListJour;
        }

        public void setTxtListJour(TextView txtListJour) {
            this.txtListJour = txtListJour;
        }

        public TextView getTxtListMontant() {
            return txtListMontant;
        }

        public void setTxtListMontant(TextView txtListMontant) {
            this.txtListMontant = txtListMontant;
        }

        public TextView getTxtListMotif() {
            return txtListMotif;
        }

        public void setTxtListMotif(TextView txtListMotif) {
            this.txtListMotif = txtListMotif;
        }

        public ImageButton getBtnSuppr() {
            return cmdSuppHf;
        }

        public void setBtnSuppr(ImageButton cmdSuppHf) {
            this.cmdSuppHf = cmdSuppHf;
        }
    }
}
